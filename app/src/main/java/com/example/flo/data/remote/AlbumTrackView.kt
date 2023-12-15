package com.example.flo.data.remote

interface AlbumTrackView {
    fun onGetAlbumTrackLoading()
    fun onGetAlbumTrackSuccess(code: Int, result: TrackResponse)
    fun onGetAlbumTrackFailure(code: Int, message: String)
}