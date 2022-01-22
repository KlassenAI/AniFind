package com.android.anifind.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.domain.model.AnimeEntity
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimePagingSource @Inject constructor(
    private val service: RetrofitService,
    private val map: HashMap<String, String>
) : RxPagingSource<Long, AnimeEntity>() {

    override fun loadSingle(params: LoadParams<Long>): Single<LoadResult<Long, AnimeEntity>> {
        val page = params.key ?: 1
        map["page"] = page.toString()
        return service.requestAnimes(map)
            .subscribeOn(Schedulers.io())
            .map { list -> list.map { it.entity } }
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: List<AnimeEntity>, page: Long): LoadResult<Long, AnimeEntity> {
        return LoadResult.Page(
            data,
            if (page == 1.toLong()) null else page - 1,
            if (data.isEmpty()) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Long, AnimeEntity>): Long? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}