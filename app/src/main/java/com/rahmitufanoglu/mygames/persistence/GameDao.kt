package com.rahmitufanoglu.mygames.persistence

import android.arch.persistence.room.*

/**
 *  Database configuration with one DAO (Data Access Object)
 */
@Dao
interface GameDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): List<Game>?

    @Query("SELECT title FROM games WHERE title = :title")
    fun findGameTitle(title: String?): List<Game>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game?)

    @Query("DELETE FROM games")
    fun deleteAllGames()

    @Delete
    fun deleteGame(game: Game?)
}
