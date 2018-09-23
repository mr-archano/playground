package io.archano.playground.common.api

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitConfiguration private constructor(
        private val httpclient: OkHttpClient,
        private val moshi: Moshi,
        private val retrofit: Retrofit) {

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun edit(): Builder {
        return Builder(httpclient.newBuilder(), moshi.newBuilder(), retrofit.newBuilder())
    }

    class Builder constructor(private val httpClientBuilder: OkHttpClient.Builder,
                              private val moshiBuilder: Moshi.Builder,
                              private val retrofitBuilder: Retrofit.Builder) {

        fun withNetworkInterceptor(interceptor: Interceptor): Builder {
            httpClientBuilder.addInterceptor(interceptor)
            return this
        }

        fun withBaseUrl(url: String): Builder {
            retrofitBuilder.baseUrl(url)
            return this
        }

        fun build(): RetrofitConfiguration {
            val httpClient = httpClientBuilder.build()
            val moshi = moshiBuilder.build()
            val retrofit = retrofitBuilder
                    .client(httpClient)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
            return RetrofitConfiguration(httpClient, moshi, retrofit)
        }
    }
}