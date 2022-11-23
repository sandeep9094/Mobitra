package org.digital.tracking.di

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.digital.tracking.api.ApolloAuthInterceptor
import org.digital.tracking.api.AuthorizationInterceptor
import org.digital.tracking.api.RetrofitManager
import org.digital.tracking.graphql.Apollo
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.DumVehicleAvgRunningResponse
import org.digital.tracking.utils.Constants
import java.io.InputStreamReader
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getSharePreferences(@ApplicationContext context: Context) = SharedPrefs(context)

    @Singleton
    @Provides
    fun getApiService(authInterceptor: AuthorizationInterceptor) = RetrofitManager.getApiService(authInterceptor)

    @Singleton
    @Provides
    fun getAuthInterceptor(sharedPrefs: SharedPrefs): AuthorizationInterceptor {
        return AuthorizationInterceptor(sharedPrefs)
    }

    @Singleton
    @Provides
    fun getApolloClient(authInterceptor: ApolloAuthInterceptor) = Apollo.apolloClient(authInterceptor)

    @Singleton
    @Provides
    fun getApolloAuthInterceptor(sharedPrefs: SharedPrefs) = ApolloAuthInterceptor(sharedPrefs)

    @Singleton
    @Provides()
    @Named("mapsApiKey")
    fun getMapsApiKey(@ApplicationContext context: Context): String {
        val ai: ApplicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val bundle = ai.metaData
        return bundle.getString("com.google.android.geo.API_KEY") ?: ""
    }

    @Singleton
    @Provides
    fun getDefaultFallbackLocation(): LatLng = LatLng(Constants.DEFAULT_INDIA_LAT, Constants.DEFAULT_INDIA_LONG)

    @Singleton
    @Provides
    fun getAssetManager(@ApplicationContext context: Context) = context.assets


}