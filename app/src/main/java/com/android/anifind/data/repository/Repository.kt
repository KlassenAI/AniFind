package com.android.anifind.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava3.observable
import com.android.anifind.data.network.RetrofitService
import com.android.anifind.data.paging.AnimePagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val service: RetrofitService) {

    fun getSingle(params: Map<String, String> = mapOf("limit" to "20")) = service.requestAnimes(params)

    fun requestAnimes(request: String) = getPager(getBaseMap(request))

    fun requestFilterAnimes(map: HashMap<String, String>) = getPager(map)

    private fun getBaseMap(request: String) = hashMapOf("limit" to "20", "search" to request)

    private fun getPager(map: HashMap<String, String>) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { AnimePagingSource(service, map) }
    ).observable
}