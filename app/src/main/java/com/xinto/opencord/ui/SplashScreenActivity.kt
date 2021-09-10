package com.xinto.opencord.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.xinto.opencord.ext.authPreferences
import com.xinto.opencord.util.currentAccountToken

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO Storing token in preferences is NOT a very good idea
        //TODO Must move this to somewhere else
        val token = authPreferences.getString("user_token", "") ?: ""

        if (token.isNotEmpty()) {
            currentAccountToken = token
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }

}