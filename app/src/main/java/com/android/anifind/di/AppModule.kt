package com.android.anifind.di

import android.content.Context
import com.android.anifind.data.db.AppDatabase
import com.android.anifind.data.network.RetrofitInstance
import com.android.anifind.data.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getDb(context)

    @Provides
    @Singleton
    fun provideAnimeDao(appDatabase: AppDatabase) = appDatabase.animeDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitInstance.getRetrofit()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): RetrofitService = RetrofitInstance.getService(retrofit)
}
