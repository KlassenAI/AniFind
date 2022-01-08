package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import com.android.anifind.domain.model.Anime
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val service: RetrofitService) {

    fun requestAnimes(request: String) = getPager(hashMapOf("limit" to "20", "search" to request))

    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)

    fun requestLatestAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "latest", "order" to "aired_on"))

    fun requestOnGoingAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "ongoing", "order" to "popularity"))

    fun requestAnonsAnimes() =
        getPager(hashMapOf("limit" to "20", "status" to "anons", "order" to "popularity"))

    private fun getPager(map: HashMap<String, String>): Observable<PagingData<Anime>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { AnimePagingSource(service, map) }
    ).observable
}