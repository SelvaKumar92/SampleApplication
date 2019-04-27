package com.tvs.sample.utilis

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.view.View

object SAHelper {

    fun verifyAvailableNetwork(activity: Context): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun showSnackBar(aMesage: String, aActivity: Activity) {
        Snackbar.make(aActivity.findViewById<View>(android.R.id.content), aMesage, Snackbar.LENGTH_SHORT).show()
    }

}