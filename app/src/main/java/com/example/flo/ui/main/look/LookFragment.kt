package com.example.flo.ui.main.look

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.R
import com.example.flo.ui.main.album.VideoFragment
import com.example.flo.databinding.FragmentLookBinding

class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        val fragmentManager = childFragmentManager

        // 초기화면은 차트
        fragmentManager.beginTransaction()
            .replace(R.id.look_frm, ChartFragment())
            .commitAllowingStateLoss()

        // 차트
        binding.lookMenuChartTv.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.look_frm, ChartFragment())
                .commitAllowingStateLoss()

            resetTabStyles()
            binding.lookMenuChartTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuChartTv.setTextColor(Color.WHITE)
        }
        // 영상
        binding.lookMenuVideoTv.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.look_frm, VideoFragment())
                .commitAllowingStateLoss()

            resetTabStyles()
            binding.lookMenuVideoTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuVideoTv.setTextColor(Color.WHITE)
        }
        // 장르
        binding.lookMenuGenreTv.setOnClickListener {
            //화면 띄우기

            resetTabStyles()
            binding.lookMenuGenreTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuGenreTv.setTextColor(Color.WHITE)
        }
        // 상황
        binding.lookMenuSituationTv.setOnClickListener {
            //화면 띄우기

            resetTabStyles()
            binding.lookMenuSituationTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuSituationTv.setTextColor(Color.WHITE)
        }
        // 분위기
        binding.lookMenuAtmosphereTv.setOnClickListener {
            //화면 띄우기

            resetTabStyles()
            binding.lookMenuAtmosphereTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuAtmosphereTv.setTextColor(Color.WHITE)
        }
        // 오디오
        binding.lookMenuAudioTv.setOnClickListener {
            //화면 띄우기

            resetTabStyles()
            binding.lookMenuAudioTv.setBackgroundResource(R.drawable.textview_blue_radius)
            binding.lookMenuAudioTv.setTextColor(Color.WHITE)
        }

        return binding.root
    }

    // 탭의 스타일을 회색으로 초기화하는 함수
    private fun resetTabStyles() {
        binding.lookMenuChartTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuChartTv.setTextColor(Color.parseColor("#696969"))
        binding.lookMenuVideoTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuVideoTv.setTextColor(Color.parseColor("#696969"))
        binding.lookMenuGenreTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuGenreTv.setTextColor(Color.parseColor("#696969"))
        binding.lookMenuSituationTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuSituationTv.setTextColor(Color.parseColor("#696969"))
        binding.lookMenuAtmosphereTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuAtmosphereTv.setTextColor(Color.parseColor("#696969"))
        binding.lookMenuAudioTv.setBackgroundResource(R.drawable.textview_gray_radius)
        binding.lookMenuAudioTv.setTextColor(Color.parseColor("#696969"))
    }
}