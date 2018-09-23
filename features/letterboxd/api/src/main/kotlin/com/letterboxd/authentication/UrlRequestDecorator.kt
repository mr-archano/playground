package com.letterboxd.authentication

import okhttp3.Request
import java.util.*
import java.util.concurrent.TimeUnit

internal class UrlRequestDecorator internal constructor(private val apiKey: String,
                                                        private val nonceFactory: NonceFactory) : RequestDecorator {
    companion object {

        fun create(apiKey: String): UrlRequestDecorator {
            val nonceFactory = object : NonceFactory {
                override fun createNonce(): String {
                    return UUID.randomUUID().toString()
                }

                override fun createTimestamp(): String {
                    return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
                }
            }
            return UrlRequestDecorator(apiKey, nonceFactory)
        }
    }

    override fun decorate(request: Request): Request {
        val decoratedUrl = request.url().newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("nonce", nonceFactory.createNonce())
                .addQueryParameter("timestamp", nonceFactory.createTimestamp())
                .build()
                .toString()
        return request.newBuilder()
                .url(decoratedUrl)
                .build()
    }

}

internal interface NonceFactory {

    fun createNonce(): String
    fun createTimestamp(): String
}
