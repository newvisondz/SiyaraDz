package com.newvisiondz.sayaradz.services

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.CacheControl
import com.newvisiondz.sayaradz.Utils.NetworkUtils


class NetworkConnectionInterceptor(var context: Context): Interceptor {
    var networkUtils = NetworkUtils()
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (!networkUtils.isOnline(context)) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }
}