package com.android.anifind.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.anifind.domain.model.Anime
import io.reactivex.rxjava3.core.Completable

@Dao
interface AnimeDao {

    @Query("SELECT * FROM animes")
    fun getAnimes(): LiveData<List<Anime>>

    @Query("SELECT * FROM animes WHERE id=:id")
    fun getAnime(id: Int): LiveData<Anime?>

    @Query("SELECT * FROM animes WHERE id=:id")
    fun getAnimeById(id: Int): Anime?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(anime: Anime): Completable

    @Update
    fun update(anime: Anime): Completable

    @Delete
    fun delete(anime: Anime): Completable
}