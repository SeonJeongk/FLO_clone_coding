package com.example.flo.data.remote

interface AutoLoginView {
    fun onAutoLoginSuccess(code: Int)
    fun onAutoLoginFailure(code: Int)
}