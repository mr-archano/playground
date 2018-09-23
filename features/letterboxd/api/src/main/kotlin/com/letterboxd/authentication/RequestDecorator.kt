package com.letterboxd.authentication

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request

internal interface RequestDecorator {

    fun decorate(request: Request): Request

    fun asInterceptor() : Interceptor {
        return Interceptor { chain : Chain ->
            chain.proceed(decorate(chain.request()))
        }
    }
}
