package com.example.flo.data.remote

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure(code: Int, message: String)
}