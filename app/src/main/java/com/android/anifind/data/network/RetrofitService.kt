package com.android.anifind.data.network

import com.android.anifind.domain.model.Anime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("animes")
    fun getAnimes(@QueryMap params: Map<String, String>): Observable<List<Anime>>

    @GET("animes")
    fun getSingle(@QueryMap params: Map<String, String>): Single<List<Anime>>
}