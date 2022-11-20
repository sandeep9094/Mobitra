package org.digital.tracking.api

import okhttp3.Interceptor
import okhttp3.Response
import org.digital.tracking.manager.SharedPrefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApolloAuthInterceptor @Inject constructor(
    private val sharedPrefs: SharedPrefs
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", sharedPrefs.authToken)
            .method(chain.request().method, chain.request().body)
            .build()

        return chain.proceed(request)
    }
}
