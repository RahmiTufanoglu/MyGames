package com.rahmitufanoglu.mygames.model

import com.squareup.moshi.Json

data class Playstation(@Json(name = "id")
                       override var id: Int?,
                       @Json(name = "title")
                       override var title: String?,
                       @Json(name = "description")
                       override var description: String?,
                       @Json(name = "release")
                       override var release: String?,
                       @Json(name = "image")
                       override var image: String?,
                       @Json(name = "wiki")
                       override var wiki: String?,
                       @Json(name = "shop")
                       override var shop: String?,
                       @Json(name = "cheat1")
                       override var cheat1: String?,
                       @Json(name = "cheat2")
                       override var cheat2: String?,
                       @Json(name = "cheat3")
                       override var cheat3: String?
) : Platform()
