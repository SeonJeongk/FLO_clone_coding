package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumRetrofitInterfaces {
    @GET("/albums") // 앨범 리스트
    fun getAlbumList(): Call<AlbumResponse>

    @GET("/albums/{albumIdx}") //앨범 수록곡
    fun getAlbumTrack(@Path("albumIdx") albumIdx: Int): Call<TrackResponse>
}