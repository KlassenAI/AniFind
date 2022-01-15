package com.android.anifind.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimePagingSource @Inject constructor(
    private val repository: Repository,
    private val map: HashMap<String, String>
) : RxPagingSource<Long, Anime>() {

    override fun loadSingle(params: LoadParams<Long>): Single<LoadResult<Long, Anime>> {
        val page = params.key ?: 1
        map["page"] = page.toString()
        return repository.requestAnime(map)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(animes: List<Anime>, page: Long): LoadResult<Long, Anime> {
        return LoadResult.Page(
            data = animes,
            prevKey = if (page == 1.toLong()) null else page - 1,
            nextKey = if (animes.isEmpty()) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Long, Anime>): Long? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

