package com.example.flo.ui.main.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.remote.AlbumTrackView
import com.example.flo.data.remote.AlbumTrackService
import com.example.flo.data.remote.TrackResponse
import com.example.flo.databinding.FragmentSongBinding
import com.google.gson.Gson

class SongFragment(private val albumIdx: Int) : Fragment(), AlbumTrackView {

    lateinit var binding: FragmentSongBinding
    private var gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        //내취향 mix 버튼
        binding.albumMixOnIv.setOnClickListener{
            setMixStatus(false)  //mix 꺼짐
        }
        binding.albumMixOffIv.setOnClickListener{
            setMixStatus(true)  //mix 켜짐
        }

        return binding.root
    }

        fun setMixStatus(isMixing : Boolean){
        if(isMixing){   //mix 사용
            binding.albumMixOnIv.visibility = View.VISIBLE
            binding.albumMixOffIv.visibility = View.GONE
        }
        else{   //mix 사용 안 함
            binding.albumMixOnIv.visibility = View.GONE
            binding.albumMixOffIv.visibility = View.VISIBLE
        }
    }

    // 앨범 수록곡 업데이트


    override fun onStart() {
        super.onStart()
        getAlbumTrack()
    }

    private fun getAlbumTrack(){
        val albumService = AlbumTrackService()
        albumService.setAlbumTrackView(this)

        Log.d("SONGLIST", "앨범Idx : " + albumIdx.toString())

        albumService.getAlbumTrack(albumIdx)
    }

    private fun initRecyclerView(result: TrackResponse){
        var floTrackAdapter = AlbumTrackRVAdapter(requireContext(), result)

        binding.albumMusicListRv.adapter = floTrackAdapter
    }
    override fun onGetAlbumTrackLoading() {
        binding.albumLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetAlbumTrackSuccess(code: Int, result: TrackResponse) {
        binding.albumLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetAlbumTrackFailure(code: Int, message: String) {
        binding.albumLoadingPb.visibility = View.GONE
        Log.d("Album-FRAG/AB-RESPONSE", message)
    }
}