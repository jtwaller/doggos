package com.example.doggohelpo.network

data class AuthInfo(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)