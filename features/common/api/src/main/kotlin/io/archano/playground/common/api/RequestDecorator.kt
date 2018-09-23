package io.archano.playground.common.api

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request

interface RequestDecorator {

    fun decorate(request: Request): Request

    fun asInterceptor() : Interceptor {
        return Interceptor { chain : Chain ->
            chain.proceed(decorate(chain.request()))
        }
    }
}
