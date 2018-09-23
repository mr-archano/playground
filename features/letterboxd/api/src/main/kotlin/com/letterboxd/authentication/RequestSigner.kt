package com.letterboxd.authentication

import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import okio.ByteString
import java.util.*

internal class RequestSigner private constructor(private val key: ByteString) {

    private val buffer = Buffer()

    companion object {

        val DEFAULT_LOCALE: Locale = Locale.US

        fun create(apiSecret: String): RequestSigner {
            return RequestSigner(ByteString.encodeUtf8(apiSecret))
        }
    }

    fun sign(request: Request): String {
        val method = request.method().toUpperCase(DEFAULT_LOCALE)
        val url = request.url()
        val encodedBody = encode(request.body())
        val preHashed = "$method\u0000$url\u0000$encodedBody"
        val signature = buffer.writeUtf8(preHashed)
                .hmacSha256(key)
                .hex()
                .toLowerCase(DEFAULT_LOCALE)
        buffer.clear()
        return signature
    }

    private fun encode(requestBody: RequestBody?): String {
        if (requestBody == null) {
            return ""
        }
        try {
            requestBody.writeTo(buffer)
            return buffer.readUtf8()
        } finally {
            buffer.clear()
        }
    }
}
