package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.android.anifind.data.db.AnimeDao
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
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
    companion object {
        private val limit = "limit" to "20"
        private val anons = "status" to "anons"
        private val latest = "status" to "latest"
        private val ongoing = "status" to "ongoing"
        private val airedOn = "order" to "aired_on"
        private val popularity = "order" to "popularity"
    }

    fun requestAnimes(request: String) = getPager(hashMapOf(limit, "search" to request))
    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)
    fun requestLatestAnimes() = getPager(hashMapOf(limit, latest, airedOn))
    fun requestAnonsAnimes() = getPager(hashMapOf(limit, anons, popularity))
    fun requestOnGoingAnimes() = getPager(hashMapOf(limit, ongoing, popularity))
    fun requestPopularAnimes() = getPager(hashMapOf(limit, popularity))

    private fun getPager(map: HashMap<String, String>): Observable<PagingData<Anime>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { AnimePagingSource(service, map) }
    ).observable

    fun getIsFavorite(id: Int) = dao.getIsFavorite(id)
    fun getAnimes() = dao.getAnimes()
    fun getFavoriteAnimes() = dao.getFavoriteAnimes()
    fun getCompletedAnimes() = dao.getAnimesWithStatus(COMPLETED)
    fun getDroppedAnimes() = dao.getAnimesWithStatus(DROPPED)
    fun getHoldAnimes() = dao.getAnimesWithStatus(HOLD)
    fun getPlannedAnimes() = dao.getAnimesWithStatus(PLANNED)
    fun getWatchingAnimes() = dao.getAnimesWithStatus(WATCHING)

    fun getAnime(id: Int) = dao.getAnime(id)
    fun insert(anime: Anime) = dao.insert(anime).sub()
    fun update(anime: Anime) = dao.update(anime).sub()
    fun delete(anime: Anime) = dao.delete(anime).sub()

    private fun Completable.sub() {
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}