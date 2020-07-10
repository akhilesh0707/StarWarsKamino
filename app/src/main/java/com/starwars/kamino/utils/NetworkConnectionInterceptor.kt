package com.starwars.kamino.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.starwars.kamino.R
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkConnected(context.applicationContext)) {
            throw NoInternetException(context.getString(R.string.no_internet_connection))
        }
        return chain.proceed(chain.request())
    }
}


fun isNetworkConnected(applicationContext: Context): Boolean {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.let { it ->
        it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }
    return false
}
