package com.example.flo.data.remote

import android.util.Log
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService() {
    private lateinit var homeView: HomeView

    fun setHomeView(homeView: HomeView){
        this.homeView = homeView
    }

    fun getAlbumList(){
        val albumService = getRetrofit().create(AlbumRetrofitInterfaces::class.java)

        homeView.onGetAlbumLoading()

        albumService.getAlbumList().enqueue(object : Callback<AlbumResponse> {

            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                if(response.isSuccessful && response.code() == 200){
                    val albumResponse: AlbumResponse = response.body()!!

                    Log.d("SONG_RESPONSE", albumResponse.toString())

                    when(val code = albumResponse.code){
                        1000 -> {
                            homeView.onGetAlbumSuccess(code, albumResponse.result!!)
                        }
                        else -> homeView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                homeView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}