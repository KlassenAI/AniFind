package com.android.anifind.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.NO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AnimeDao {

    @Query("SELECT * FROM animes WHERE is_favorite=:favorite")
    fun getFavoriteAnimes(favorite: Boolean = true): LiveData<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE watch_status=:status")
    fun getAnimesByStatus(status: WatchStatus): LiveData<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE is_favorite =:bool AND watch_status =:status ORDER BY update_date DESC")
    fun getRecentAnime(bool: Boolean = false, status: WatchStatus = NO): LiveData<List<AnimeEntity>>

    @Query("SELECT * FROM animes WHERE id=:id")
    fun getAnime(id: Int): Flowable<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(animeEntity: AnimeEntity): Completable

    @Update
    fun update(animeEntity: AnimeEntity): Completable

    @Delete
    fun delete(animeEntity: AnimeEntity): Completable

    @Query("DELETE FROM animes WHERE is_favorite =:bool AND watch_status =:status")
    fun deleteRecentAnime(bool: Boolean = false, status: WatchStatus = NO): Completable
}