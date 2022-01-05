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

    fun getSingle(params: Map<String, String> = mapOf("limit" to "50")) = service.getSingle(params)

    fun searchSingle(search: String? = null): Observable<PagingData<Anime>> {
        val map = hashMapOf<String, String>()
        search?.let { map["search"] = it }
        map["limit"] = "20"
        return getPager(map)
    }

    fun requestFilterAnimes(map: HashMap<String, String>): Observable<PagingData<Anime>> {
        return getPager(map)
    }

    private fun getPager(map: HashMap<String, String>) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { AnimePagingSource(this, map) }
    ).observable
}