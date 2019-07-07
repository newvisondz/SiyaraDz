package com.newvisiondz.sayara.services

import android.content.Context
import com.newvisiondz.sayara.utils.isOnline
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response


class NetworkConnectionInterceptor(var context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (!isOnline(context)) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }
}