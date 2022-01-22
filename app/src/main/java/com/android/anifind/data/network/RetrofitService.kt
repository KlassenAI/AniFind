package com.android.anifind.data.network

import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("animes")
    fun requestAnimes(@QueryMap params: Map<String, String>): Single<List<Anime>>

    @GET("animes/{id}")
    fun requestAnimeInfo(@Path("id") id: Int): Single<AnimeInfo>
}