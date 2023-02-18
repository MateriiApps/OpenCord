package com.xinto.opencord.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xinto.opencord.manager.AccountManager
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    private val accountManager: AccountManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (accountManager.isLoggedIn) {
            startActivity(Intent(this, AppActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
