package com.android.anifind.data.network

import com.android.anifind.domain.model.Anime
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("animes")
    fun getAnimes(@QueryMap params: Map<String, String>): Observable<List<Anime>>
}