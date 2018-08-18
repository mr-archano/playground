package com.letterboxd.authentication

import okhttp3.Request

internal interface RequestDecorator {

    fun decorate(request: Request): Request
}
