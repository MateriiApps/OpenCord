package com.xinto.opencord

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.xinto.opencord.util.currentAccount

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accountManager = AccountManager.get(this)
        val accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)
        if (accounts.isNotEmpty()) {
            currentAccount = accounts.first()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}