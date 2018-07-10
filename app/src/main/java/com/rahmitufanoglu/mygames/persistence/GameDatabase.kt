package com.rahmitufanoglu.mygames.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(Game::class)], version = 1)
abstract class GameDatabase : RoomDatabase() {

    abstract fun userDao(): GameDao

    companion object {
        private var INSTANCE: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, GameDatabase::class.java, "Game.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}
