package com.rahmitufanoglu.mygames.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *  Database configuration with one entity
 */
@Entity(tableName = "games")
data class Game(@ColumnInfo(name = "title")
                var title: String?,
                @ColumnInfo(name = "description")
                var description: String?,
                @ColumnInfo(name = "release")
                var release: String?,
                @ColumnInfo(name = "image")
                var image: String?,
                @ColumnInfo(name = "wiki")
                val wiki: String?,
                @ColumnInfo(name = "shop")
                val shop: String?,
                @ColumnInfo(name = "cheat1")
                val cheat1: String?,
                @ColumnInfo(name = "cheat2")
                val cheat2: String?,
                @ColumnInfo(name = "cheat3")
                val cheat3: String?) {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
