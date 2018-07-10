package com.rahmitufanoglu.mygames.api

import com.rahmitufanoglu.mygames.LINK
import com.rahmitufanoglu.mygames.model.GameDataRepo
import retrofit2.Call
import retrofit2.http.GET

interface GameService {

    @GET(LINK)
    fun getGames(): Call<GameDataRepo>
}
