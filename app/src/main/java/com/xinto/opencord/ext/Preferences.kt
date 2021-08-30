package com.xinto.opencord.ext

import android.content.Context
import android.content.SharedPreferences

val Context.authPreferences: SharedPreferences
    get() = getSharedPreferences("user_auth", Context.MODE_PRIVATE)