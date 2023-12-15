package com.example.flo.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 여러 화면에서 공통적으로 쓰이는 형식이기 때문에 해당 파일에 따로 함수를 모아둔다

const val BASE_URL = "https://edu-api-test.softsquared.com" //연결할 주소로, 맨 뒤에 슬래시 빼는거 주의

fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}