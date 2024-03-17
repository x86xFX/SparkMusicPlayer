package me.theek.spark.core.data.util

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ArtistCoverNetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = Credentials.basic(
            username = "F35_r01s@!10@kl4ls4qlgm",
            password = "11s@F35_r0lgsll@44k0qm!"
        )

        val request = Request.Builder()
            .url(chain.request().url)
            .addHeader("Authorization", credentials)
            .get()
            .build()

        return chain.proceed(request)
    }
}