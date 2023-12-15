package com.example.flo.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.SignUpView
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySignupBinding


class SignUpActivity : AppCompatActivity() , SignUpView {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSubmitBt.setOnClickListener{
            signUp()
        }
    }

    // 유저 정보 입력 값을 가져온다
    private fun getUser() : User {
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpEmailEt.text.toString()
        val pwd : String = binding.signUpPwEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        return User(email, pwd, name)
    }

    //회원가입 유효성 확인
   private fun signUp(){
        // 이메일 없음
        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpEmailEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        // 이름 없음
        if(binding.signUpNameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()

            return
        }
        // 비밀번호 없음
        if(binding.signUpPwEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        //비밀번호 불일치
        if(binding.signUpPwEt.text.toString() != binding.signUpPwConfirmEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())   // AuthService 호출
    }

    override fun onSignUpSuccess() {
        finish()    // 로그인 화면으로 이동
    }

    override fun onSignUpFailure(message: String) {
        binding.signUpEmailErrorTv.visibility = View.VISIBLE
        binding.signUpEmailErrorTv.setText(message)
        Log.d("signup-error", message)
    }
}
