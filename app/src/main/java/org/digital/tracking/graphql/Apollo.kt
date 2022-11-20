package org.digital.tracking.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.NetworkTransport
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient
import org.digital.tracking.api.ApolloAuthInterceptor
import org.digital.tracking.utils.Constants

object Apollo {

    private var instance: ApolloClient? = null

    fun apolloClient(authInterceptor: ApolloAuthInterceptor): ApolloClient {
        if (instance != null) {
            return instance!!
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        instance = ApolloClient.Builder()
            .serverUrl(Constants.GRAPHQL_TRACKING_SERVER_URL)
            .webSocketServerUrl(Constants.GRAPHQL_TRACKING_SOCKET_SERVER_URL)
            .webSocketIdleTimeoutMillis(Constants.GRAPHQL_SOCKET_CONNECTION_TIMEOUT)
            .okHttpClient(okHttpClient)
            .build()

        return instance!!
    }

}
