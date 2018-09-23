package com.letterboxd.authentication

import com.letterboxd.BuildConfig
import org.junit.Before
import org.junit.Test

class RetrofitAccessTokenFetcherTest {

    private lateinit var fetcher: RetrofitAccessTokenFetcher

    @Before
    fun setUp() {
        fetcher = RetrofitAccessTokenFetcher.create()
    }

    @Test
    fun shouldCreateToken() {
        val observer = fetcher.createAccessToken(BuildConfig.USERNAME, BuildConfig.PASSWORD).test()

        observer.assertValueCount(1)
        observer.assertComplete()
    }
}
