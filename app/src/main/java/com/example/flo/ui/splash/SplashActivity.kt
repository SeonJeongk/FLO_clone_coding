package com.example.flo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.AutoLoginView
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.signin.LoginActivity

class SplashActivity : AppCompatActivity(), AutoLoginView {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        //1초동안 Splash 화면 보여준다
        handler.postDelayed({
            tryLogin()    // 자동 로그인
        }, 1000)
    }

    private fun getToken(): String? {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        return spf.getString("jwt", null)
    }

    private fun tryLogin(){
        val authService = AuthService()
        authService.setAutoLoginView(this)

        val token = getToken()
        authService.autoLogin(token)    // autoLogin 호출
    }

    override fun onAutoLoginSuccess(code: Int) {
        // 자동 로그인 성공
        startActivity(Intent(this, MainActivity::class.java))
        Log.d("자동 로그인 성공", getToken().toString())
        finish()
    }

    override fun onAutoLoginFailure(code: Int) {
        // 자동 로그인 실패
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}