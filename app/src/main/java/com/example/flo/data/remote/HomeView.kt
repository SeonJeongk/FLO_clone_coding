package com.example.flo.data.remote

interface HomeView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: FloAlbumResult)
    fun onGetAlbumFailure(code: Int, message: String)
}