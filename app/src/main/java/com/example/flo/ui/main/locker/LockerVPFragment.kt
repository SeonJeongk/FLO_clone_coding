package com.example.flo.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPFragment(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3    //fragment 3개 (저장한곡, 음악파일, 저장앨범)

    override fun createFragment(position: Int): Fragment {
        //선택한 tab에 따라 다른 fragment 보여주기
        return when(position){
            0 -> SavedSongFragment()
            1 -> SongFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}