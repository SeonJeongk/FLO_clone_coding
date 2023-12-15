package com.example.flo.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.LoginView
import com.example.flo.ui.signup.SignUpActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.data.remote.Result
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.ui.main.MainActivity

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 화면으로 이동
        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        // 로그인 처리
        binding.loginLoginBt.setOnClickListener {
            login()
        }
    }

    // 로그인 유효성 검사
    private fun login(){
        // 이메일, 비밀번호가 유효하면 db에 정보 유무 확인 후 로그인
        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
        val pwd : String = binding.loginPwEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, pwd, ""))
    }

    // 인자 값으로 받은 JWT값(유저 id 정보)을 SharedPreference에 저장
//    private fun saveJwt(jwt:Int){
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }
    private fun saveJwt2(jwt:String, userIdx: Int){
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.putInt("id", userIdx)
        editor.apply()
    }

    private fun startMainActivity(){
        val Intent = Intent(this, MainActivity::class.java)
        startActivity(Intent)
    }

    // 로그인 성공 시 메인 화면으로 돌아감
    override fun onLoginSuccess(code: Int, result: Result) {
        when(code){
            1000 -> {
                saveJwt2(result.jwt, result.userIdx)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        when(code){
            2015 -> {   // 이메일 입력 안 함
                binding.loginEmailErrorTv.visibility = View.VISIBLE
                binding.loginEmailErrorTv.text = message
            }
            2019 -> {   // 계정 없음
                binding.loginPwErrorTv.visibility = View.VISIBLE
                binding.loginPwErrorTv.text = message
            }
            3014 -> {   // 비밀번호 틀림
                binding.loginPwErrorTv.visibility = View.VISIBLE
                binding.loginPwErrorTv.text = message
            }
        }
    }
}