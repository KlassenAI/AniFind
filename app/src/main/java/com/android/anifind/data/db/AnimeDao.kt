package com.android.anifind.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.WatchStatus
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AnimeDao {

    @Query("SELECT is_favorite FROM animes WHERE id = :id")
    fun getIsFavorite(id: Int): Flowable<Boolean?>

    @Query("SELECT * FROM animes")
    fun getAnimes(): LiveData<List<Anime>>

    @Query("SELECT * FROM animes WHERE is_favorite=:favorite")
    fun getFavoriteAnimes(favorite: Boolean = true): LiveData<List<Anime>>

    @Query("SELECT * FROM animes WHERE watch_status=:status")
    fun getAnimesWithStatus(status: WatchStatus): LiveData<List<Anime>>

    @Query("SELECT * FROM animes WHERE id=:id")
    fun getAnime(id: Int): Flowable<Anime?>

    @Query("SELECT * FROM animes WHERE id=:id")
    fun getAnimeById(id: Int): Anime?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(anime: Anime): Completable

    @Update
    fun update(anime: Anime): Completable

    @Delete
    fun delete(anime: Anime): Completable
}