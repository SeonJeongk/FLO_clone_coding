package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterfaces {
    @GET("/songs")  //endpoint
    fun getSongs(): Call<SongResponse>
}