package com.example.flo.ui.main.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.remote.LookView
import com.example.flo.data.remote.FloChartResult
import com.example.flo.data.remote.SongService
import com.example.flo.databinding.FragmentLookChartBinding

class ChartFragment : Fragment(), LookView {
    lateinit var binding: FragmentLookChartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookChartBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun getSongs(){
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()
    }

    private fun initRecyclerView(result: FloChartResult){
        var floCharAdapter = SongRVAdapter(requireContext(), result)

        binding.chartSongListRv.adapter = floCharAdapter
    }

    override fun onGetSongLoading() {
        binding.chartLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.chartLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.chartLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}