package com.example.flo.data.remote

import android.util.Log
import com.example.flo.data.entities.User
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: AutoLoginView){
        this.autoLoginView = autoLoginView
    }

    // 회원가입 관리
    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        // API 호출 및 enqueue로 응답 처리
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {   // 응답 옴
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()   // 회원가입 성공
                    else -> signUpView.onSignUpFailure(resp.message)   //회원가입 실패
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {    // 응답이 안 옴
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })
        Log.d("SIGNUP", "HELLO")
    }

    // 로그인 관리
    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        // API 호출 및 enqueue로 응답 처리
        authService.login(user).enqueue(object: Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {   // 응답 옴
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(val code = resp.code){
                    1000-> {
                        loginView.onLoginSuccess(code, resp.result!!)
                    }
                    else-> loginView.onLoginFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {    // 응답이 안 옴
                Log.d("LOGIN/FAILURE", t.message.toString())
            }
        })
        Log.d("LOGIN", "HELLO")
    }

    fun autoLogin(token: String?){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        // API 호출 및 enqueue로 응답 처리
        authService.autoLogin(token).enqueue(object: Callback<AuthResponse>{

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AUTO LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> {
                        // 자동 로그인 성공
                        autoLoginView.onAutoLoginSuccess(code)
                    }
                    else -> {
                        // 자동 로그인 실패
                        autoLoginView.onAutoLoginFailure(code)
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("AUTO LOGIN/FAILURE", t.message.toString())
            }

        })
        Log.d("AUTO LOGIN", "HELLO")
    }
}