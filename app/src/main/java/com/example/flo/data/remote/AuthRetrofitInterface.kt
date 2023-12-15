package com.example.flo.data.remote

import com.example.flo.data.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users") //서버의 "/users" 엔드포인트에 POST 요청을 보낸다
    fun signUp(@Body user: User): Call<AuthResponse> // HTTP 요청의 바디에 User 객체를 담아 서버에 전송

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login")   // 자동 로그인
    fun autoLogin(@Header("X-ACCESS-TOKEN") token: String?): Call<AuthResponse>
}