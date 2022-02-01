package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava3.observable
import com.android.anifind.Constants.DATE_PATTERN
import com.android.anifind.Constants.LANG_RU
import com.android.anifind.data.db.AnimeDao
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus.*
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

    fun requestAnimes(request: String) = getPager(hashMapOf(limit, "search" to request))
    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)
    fun requestLatestAnimes() = getPager(hashMapOf(limit, latest, popularity))
    fun requestAnonsAnimes() = getPager(hashMapOf(limit, anons, popularity))
    fun requestOnGoingAnimes() = getPager(hashMapOf(limit, ongoing, popularity))

    private fun getPager(map: HashMap<String, String>) = Pager(
        PagingConfig(20), null, { AnimePagingSource(service, map) }
    ).observable

    fun requestAnimeInfo(id: Int) = service.requestAnimeInfo(id)
    fun requestSimilarAnime(id: Int) = service.requestSimilarAnime(id)
    fun requestRelatedAnime(id: Int) = service.requestRelatedAnime(id)

    fun getAnime(id: Int) = dao.getAnime(id)
    fun getRecentAnime() = dao.getRecentAnime()
    fun getFavoriteAnimes() = dao.getFavoriteAnimes()
    fun getCompletedAnimes() = dao.getAnimesByStatus(COMPLETED)
    fun getDroppedAnimes() = dao.getAnimesByStatus(DROPPED)
    fun getHoldAnimes() = dao.getAnimesByStatus(HOLD)
    fun getPlannedAnimes() = dao.getAnimesByStatus(PLANNED)
    fun getWatchingAnimes() = dao.getAnimesByStatus(WATCHING)

    fun deleteRecentAnime() {
        dao.deleteRecentAnime().subscribeOn(Schedulers.io()).subscribe()
    }

    fun insert(animeEntity: AnimeEntity) {
        val date = getCurrentDate()
        if (animeEntity.addDate.isEmpty()) animeEntity.addDate = date
        animeEntity.updateDate = date
        dao.insert(animeEntity).subscribeOn(Schedulers.io()).subscribe()
    }

    fun update(animeEntity: AnimeEntity) {
        val date = getCurrentDate()
        animeEntity.updateDate = date
        dao.update(animeEntity).subscribeOn(Schedulers.io()).subscribe()
    }

    fun delete(animeEntity: AnimeEntity) {
        dao.delete(animeEntity).subscribeOn(Schedulers.io()).subscribe()
    }

    private fun getCurrentDate() = SimpleDateFormat(DATE_PATTERN, Locale(LANG_RU)).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date())


}