package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

class AlbumResponse (
    @SerializedName("isSuccess") val isSuccess:Boolean,
    @SerializedName("code") val code:Int,
    @SerializedName("message") val message:String,
    @SerializedName("result") val result: FloAlbumResult?  //null 처리 해야 같이 사용 가능
)

data class FloAlbumResult(
    @SerializedName("albums") var albums: ArrayList<FloAlbumSongs>
)

data class FloAlbumSongs(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("coverImgUrl") val coverImgUrl: String
)