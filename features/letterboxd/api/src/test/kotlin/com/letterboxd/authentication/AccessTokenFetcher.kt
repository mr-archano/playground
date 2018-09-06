package com.letterboxd.authentication

import io.reactivex.Single

internal interface AccessTokenFetcher {

    fun createAccessToken(username: String, password: String): Single<AccessToken>
    fun refreshAccessToken(refreshToken: RefreshToken): Single<AccessToken>
}
