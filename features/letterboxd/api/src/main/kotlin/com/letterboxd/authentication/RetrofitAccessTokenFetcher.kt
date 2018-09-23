package com.letterboxd.authentication

import com.letterboxd.BuildConfig
import com.squareup.moshi.Moshi
import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

private const val LETTERBOX_BASE_URL = "https://api.letterboxd.com/api/v0/"

internal class RetrofitAccessTokenFetcher private constructor(private val backend: AccessTokenBackend) : AccessTokenFetcher {

    private val convertJson: Function<AccessTokenJson, AccessToken> = Function { json: AccessTokenJson ->
        AccessToken(json.access_token, RefreshToken(json.refresh_token))
    }

    companion object {

        fun create(): RetrofitAccessTokenFetcher {
            val urlRequestDecorator = UrlRequestDecorator.create(BuildConfig.API_KEY)
            val signingRequestDecorator = SignatureHeaderRequestDecorator(RequestSigner.create(BuildConfig.API_SECRET))
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor(urlRequestDecorator.asInterceptor())
                    .addNetworkInterceptor(signingRequestDecorator.asInterceptor())
                    .build()
            val backend = Retrofit.Builder()
                    .baseUrl(LETTERBOX_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                    .build()
                    .create(AccessTokenBackend::class.java)
            return RetrofitAccessTokenFetcher(backend)
        }
    }

    override fun createAccessToken(username: String, password: String): Single<AccessToken> {
        return backend.createAccessToken(username, password)
                .map(convertJson)
    }

    override fun refreshAccessToken(refreshToken: RefreshToken): Single<AccessToken> {
        return backend.refreshAccessToken(refreshToken.value)
                .map(convertJson)
    }
}

private data class AccessTokenJson(val access_token: String, val refresh_token: String)

private interface AccessTokenBackend {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("auth/token")
    fun createAccessToken(@Field("username") username: String,
                          @Field("password") password: String,
                          @Field("grant_type") grantType: String = "password"): Single<AccessTokenJson>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAccessToken(@Field("refresh_token") refreshToken: String,
                           @Field("grant_type") grantType: String = "refresh_token"): Single<AccessTokenJson>

}
