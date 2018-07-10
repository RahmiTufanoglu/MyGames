package com.rahmitufanoglu.mygames.api

import com.rahmitufanoglu.mygames.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object GameClient {

    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
        }
        return retrofit
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return getClient()!!.create(serviceClass)
    }
}
