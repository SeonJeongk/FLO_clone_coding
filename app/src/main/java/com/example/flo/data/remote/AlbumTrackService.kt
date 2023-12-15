package com.example.flo.data.remote

import android.util.Log
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumTrackService {
    private lateinit var albumTrackView: AlbumTrackView

    fun setAlbumTrackView(albumTrackView: AlbumTrackView){
        this.albumTrackView = albumTrackView
    }

    fun getAlbumTrack(albumIdx: Int){
        val albumService = getRetrofit().create(AlbumRetrofitInterfaces::class.java)

        albumTrackView.onGetAlbumTrackLoading()

        albumService.getAlbumTrack(albumIdx).enqueue(object : Callback<TrackResponse> {

            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if(response.isSuccessful && response.code() == 200){
                    val albumResponse: TrackResponse = response.body()!!

                    Log.d("TRACK_RESPONSE", albumResponse.toString())

                    when(val code = albumResponse.code){
                        1000 -> {
                            albumTrackView.onGetAlbumTrackSuccess(code, albumResponse!!)
                        }
                        else -> albumTrackView.onGetAlbumTrackFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                albumTrackView.onGetAlbumTrackFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}