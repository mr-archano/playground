package io.archano.playground.common.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private val BASE_HTTP_CLIENT: OkHttpClient = OkHttpClient.Builder()
        .build()

private val BASE_MOSHI: Moshi = Moshi.Builder()
        .build()

private val BASE_RETROFIT: Retrofit = Retrofit.Builder()
        .baseUrl("http://change.me")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

class RetrofitConfiguration(
        private val httpclient: OkHttpClient = BASE_HTTP_CLIENT,
        private val moshi: Moshi = BASE_MOSHI,
        private val retrofit: Retrofit = BASE_RETROFIT) {

    fun newBuilder(): Builder {
        return Builder(httpclient.newBuilder(), moshi.newBuilder(), retrofit.newBuilder())
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    class Builder constructor(private val httpClientBuilder: OkHttpClient.Builder,
                              private val moshiBuilder: Moshi.Builder,
                              private val retrofitBuilder: Retrofit.Builder) {

        fun withRequestDecorator(decorator: RequestDecorator): Builder {
            httpClientBuilder.addInterceptor(decorator.asInterceptor())
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

        fun <T> create(service: Class<T>): T {
            return build().create(service)
        }
    }
}
