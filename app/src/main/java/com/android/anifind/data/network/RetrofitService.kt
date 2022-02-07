package com.android.anifind.data.network

import com.android.anifind.domain.model.AnimeResponse
import com.android.anifind.domain.model.AnimeInfo
import com.android.anifind.domain.model.Relate
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("animes")
    fun requestAnimes(@QueryMap params: Map<String, String>): Single<List<AnimeResponse>>

    @GET("animes/{id}")
    fun requestAnimeInfo(@Path("id") id: Int): Single<AnimeInfo>
}