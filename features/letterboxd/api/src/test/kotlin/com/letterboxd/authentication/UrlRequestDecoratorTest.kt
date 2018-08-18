package com.letterboxd.authentication

import com.google.common.truth.Truth.assertThat
import okhttp3.Request
import org.junit.Before
import org.junit.Test

class UrlRequestDecoratorTest {

    companion object {
        const val API_KEY = "API_KEY"
        const val NONCE = "NONCE"
        const val TIMESTAMP = "TIMESTAMP"
        @JvmField val REQUEST: Request = Request.Builder()
                .url("http://some.url")
                .build()
    }

    private lateinit var decorator: UrlRequestDecorator

    @Before
    fun setUp() {
        decorator = UrlRequestDecorator(API_KEY, object : NonceFactory {
            override fun createNonce(): String = NONCE
            override fun createTimestamp(): String = TIMESTAMP
        })
    }

    @Test
    fun shouldAppendApiKeyAsQueryParameter() {
        val decorated = decorator.decorate(REQUEST)

        assertThat(decorated.url().queryParameter("apikey")).isEqualTo(API_KEY)
    }

    @Test
    fun shouldAppendNonceAsQueryParameter() {
        val decorated = decorator.decorate(REQUEST)

        assertThat(decorated.url().queryParameter("nonce")).isEqualTo(NONCE)
    }

    @Test
    fun shouldAppendTimestampAsQueryParameter() {
        val decorated = decorator.decorate(REQUEST)

        assertThat(decorated.url().queryParameter("timestamp")).isEqualTo(TIMESTAMP)
    }
}
