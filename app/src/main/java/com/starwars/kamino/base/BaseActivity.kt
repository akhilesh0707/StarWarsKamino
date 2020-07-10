package com.starwars.kamino.base

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.starwars.kamino.di.utils.Injectable
import com.starwars.kamino.utils.isNetworkConnected
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onStart() {
        super.onStart()
        onNetworkConnectivityChange(connected = isNetworkConnected(this))
        registerNetworkConnectivityListener()
    }

    override fun onStop() {
        super.onStop()
        connectivityManager?.let {
            unregisterNetworkConnectivityListener()
        }
    }

    private fun unregisterNetworkConnectivityListener() {
        connectivityManager.unregisterNetworkCallback(networkAvailabilityListener)
    }


    private fun registerNetworkConnectivityListener() {
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkAvailabilityListener)
    }

    private val networkAvailabilityListener: ConnectivityManager.NetworkCallback = @RequiresApi(
        Build.VERSION_CODES.LOLLIPOP
    )

    object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network?) {
            super.onAvailable(network)
            runOnUiThread{
                onNetworkConnectivityChange(connected = true)
            }
        }
        override fun onLost(network: Network?) {
            super.onLost(network)
            runOnUiThread {
                onNetworkConnectivityChange(connected = false)
            }
        }
    }

    protected open fun onNetworkConnectivityChange(connected : Boolean){
        Timber.d("[onNetworkConnectivityChanged] connected=  $connected")
    }
}