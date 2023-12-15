package com.example.flo.ui.main.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.ui.signin.LoginActivity
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        //보관함 tab 바꾸기 위해 Adater와 연결
        val lockerAdapter = LockerVPFragment(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){  //tablayout을 viewPager2와 연결해준다
                tab, position ->
            tab.text = information[position]
        }.attach()

        //로그인 화면으로 이동
        binding.lockerLoginTv.setOnClickListener{
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
//        initViews()
        initServerViews()
    }

    // 로그인 확인용
    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)   // 값이 없으면 0 반환
    }

    // 로그인 확인용 - 서버
    private fun getJwt2():Int{
        val spf = activity?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("id", 0)   // 값이 없으면 null 반환
    }

    private fun initViews(){
        val jwt : Int = getJwt()

        if(jwt == 0){   // 로그아웃 상태일 때
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener{
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }else{  // 로그인 상태일 때
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener{
                // 로그아웃 진행
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun initServerViews(){
        val id : Int = getJwt2()

        if(id == 0){   // 로그아웃 상태일 때
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener{
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }else{  // 로그인 상태일 때
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener{
                // 로그아웃 진행
//                logout()
                serverLogout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    // 로그아웃 하면 Jwt 초기화 시켜준다 (현 로그인 정보니까)
    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")    // 값 초기화
        editor.apply()
    }

    private fun serverLogout(){
        val spf = activity?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("id")    // 값 초기화
        editor.remove("jwt")
        editor.apply()
    }

}