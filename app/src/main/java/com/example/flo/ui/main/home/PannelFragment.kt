package com.example.flo.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentPannelBinding

class PannelFragment(val imgRes : Int): Fragment(){

    lateinit var binding: FragmentPannelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPannelBinding.inflate(inflater, container, false)

        binding.homePannelBackgroundIv.setImageResource(imgRes)  //전달받은 img 넣어주기

        return binding.root
    }
}