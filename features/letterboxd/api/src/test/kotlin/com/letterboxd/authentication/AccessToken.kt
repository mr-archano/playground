package com.letterboxd.authentication

data class AccessToken(val value: String, val refreshToken: RefreshToken)
