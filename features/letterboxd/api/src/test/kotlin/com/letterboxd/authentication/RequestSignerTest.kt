package com.letterboxd.authentication

import com.google.common.truth.Truth.assertThat
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Test

internal class RequestSignerTest {

    companion object {
        const val API_SECRET = "API_SECRET"
        const val URL = "http://some.url"
    }

    lateinit var signer: RequestSigner

    @Before
    fun setUp() {
        signer = RequestSigner.create(API_SECRET)
    }

    @Test
    fun shouldGenerateSignatureFromRequestWithUrlEncodedFormBody() {
        val body: RequestBody = FormBody.Builder()
                .add("one", "1")
                .add("two", "2")
                .add("three", "3")
                .build()
        val request = Request.Builder()
                .url(URL)
                .post(body)
                .build()

        val signature = signer.sign(request)

        assertThat(signature).isEqualTo("872d2e1000467d80e58965c6cd3fcb78410360282c4098eb0fadad7e726e3e38")
    }

    @Test
    fun shouldGenerateSignatureFromRequestWithoutBody() {
        val request = Request.Builder()
                .url(URL)
                .get()
                .build()

        val signature = signer.sign(request)

        assertThat(signature).isEqualTo("1f030c9a78ad19224f021a79b96401ecda59c56aa8f9b615dbb0a955ce171efa")
    }
}
