package com.rahmitufanoglu.mygames.model

import com.squareup.moshi.Json
import java.util.*

data class GameDataRepo(@Json(name = "pc")
                        var pc: List<Pc> = ArrayList(),
                        @Json(name = "xbox")
                        var xbox: List<Xbox> = ArrayList(),
                        @Json(name = "playstation")
                        var playstation: List<Playstation> = ArrayList(),
                        @Json(name = "nintendo")
                        var nintendo: List<Nintendo> = ArrayList()
)
