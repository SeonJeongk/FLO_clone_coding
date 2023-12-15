package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

class TrackResponse (
    @SerializedName("isSuccess") val isSuccess:Boolean,
    @SerializedName("code") val code:Int,
    @SerializedName("message") val message:String,
    @SerializedName("result") val result: ArrayList<FloAlbumTrack>  //null 처리 해야 같이 사용 가능
)

data class FloAlbumTrack(
    @SerializedName("songIdx") val songIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("isTitleSong") val isTitleSong: String
)