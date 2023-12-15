package com.example.flo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class PannelVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    //해당 class에서만 사용되는 fragment list
    private var fragmentlist:ArrayList<Fragment> = ArrayList()

    //연결된 viewPager가 전달하는 데이터(fragment)의 개수 돌려준다
    override fun getItemCount(): Int = fragmentlist.size

    //연결된 viewPager의 fragment 개수만큼 생성
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    //함수가 실행되면서 비어있던 fragmentlist에 데이터를 넣어준다
    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)  //전달받은 fragment를 list에 넣어준다
        notifyItemInserted(fragmentlist.size-1) //추가한 fragment 위치
    }
}