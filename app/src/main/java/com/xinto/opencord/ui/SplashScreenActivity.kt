package com.xinto.opencord.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xinto.opencord.domain.manager.AccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    private val accountManager: AccountManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val targetActivity = if (accountManager.isLoggedIn())
                MainActivity::class.java
            else {
                LoginActivity::class.java
            }

            startActivity(Intent(this@SplashScreenActivity, targetActivity))
            finish()
        }
    }
}
