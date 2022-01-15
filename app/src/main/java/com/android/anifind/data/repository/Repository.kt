package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.android.anifind.data.db.AnimeDao
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import com.android.anifind.domain.model.Anime
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val dao: AnimeDao,
    private val service: RetrofitService
) {
    fun getAnimes() = dao.getAnimes()
    fun getAnime(id: Int) = dao.getAnime(id)
    fun insert(anime: Anime) = dao.insert(anime).sub()
    fun update(anime: Anime) = dao.update(anime).sub()
    fun delete(anime: Anime) = dao.delete(anime).sub()

    fun requestAnime(map: HashMap<String, String>) = service.requestAnimes(map)

    fun requestAnimes(request: String) = getPager(hashMapOf("limit" to "20", "search" to request))

    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)

    fun requestLatestAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "latest", "order" to "aired_on"))

    fun requestOnGoingAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "ongoing", "order" to "popularity"))

    fun requestAnonsAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "anons", "order" to "popularity"))

    fun requestPopularAnimes() = getPager(hashMapOf("limit" to "20", "order" to "popularity"))

    private fun getPager(map: HashMap<String, String>): Observable<PagingData<Anime>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { AnimePagingSource(this, map) }
    ).observable

    private fun Completable.sub() {
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}