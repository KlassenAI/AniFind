package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava3.observable
import com.android.anifind.data.db.AnimeDao
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

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
        private val popularity = "order" to "popularity"
    }

    fun requestAnimeInfo(id: Int) = service.requestAnimeInfo(id)
    fun requestAnimes(request: String) = getPager(hashMapOf(limit, "search" to request))
    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)
    fun requestLatestAnimes() = getPager(hashMapOf(limit, latest, popularity))
    fun requestAnonsAnimes() = getPager(hashMapOf(limit, anons, popularity))
    fun requestOnGoingAnimes() = getPager(hashMapOf(limit, ongoing, popularity))
    fun requestPopularAnimes() = getPager(hashMapOf(limit, popularity))

    private fun getPager(map: HashMap<String, String>) = Pager(
        PagingConfig(20), null, { AnimePagingSource(service, map) }
    ).observable

    fun getAnimes() = dao.getAnimes()
    fun getFavoriteAnimes() = dao.getFavoriteAnimes()
    fun getCompletedAnimes() = dao.getAnimesByStatus(COMPLETED)
    fun getDroppedAnimes() = dao.getAnimesByStatus(DROPPED)
    fun getHoldAnimes() = dao.getAnimesByStatus(HOLD)
    fun getPlannedAnimes() = dao.getAnimesByStatus(PLANNED)
    fun getWatchingAnimes() = dao.getAnimesByStatus(WATCHING)

    fun getAnime(id: Int) = dao.getAnime(id)
    fun insert(animeEntity: AnimeEntity) {
        val date = getCurrentDate()
        animeEntity.addDate = date
        animeEntity.updateDate = date
        dao.insert(animeEntity).sub()
    }

    fun update(animeEntity: AnimeEntity) {
        val date = getCurrentDate()
        animeEntity.updateDate = date
        dao.update(animeEntity).sub()
    }
    fun delete(animeEntity: AnimeEntity) = dao.delete(animeEntity).sub()
    private fun Completable.sub() {
        subscribeOn(Schedulers.io()).subscribe()
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale("ru")).apply {
            timeZone = TimeZone.getDefault()
        }.format(Date())
    }
}