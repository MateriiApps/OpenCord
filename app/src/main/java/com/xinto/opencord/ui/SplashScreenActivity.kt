package com.xinto.opencord.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xinto.opencord.manager.AccountManager
import com.xinto.opencord.manager.ActivityManager
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    private val accountManager: AccountManager by inject()
    private val activityManager: ActivityManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val activity = if (accountManager.isLoggedIn) AppActivity::class else LoginActivity::class

        finishAndRemoveTask()
        activityManager.startActivity(activity)
    }
}
