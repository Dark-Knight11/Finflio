package com.finflio.core.data.network.interceptor

import com.finflio.core.data.util.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().url.pathSegments[0] == "auth") {
            return chain.proceed(chain.request())
        }

        val requestBuilder = chain.request().newBuilder()
        runBlocking {
            val token = sessionManager.getUserData().first().token
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}