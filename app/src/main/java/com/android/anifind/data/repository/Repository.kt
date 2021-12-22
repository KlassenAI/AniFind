package com.android.anifind.data.repository

import com.android.anifind.data.network.RetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val service: RetrofitService) {

    fun getAnimes(params: Map<String, String> = mapOf("limit" to "50")) = service.getAnimes(params)
}