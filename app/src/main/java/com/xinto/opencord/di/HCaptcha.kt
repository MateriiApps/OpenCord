package com.xinto.opencord.di

import android.content.Context
import com.hcaptcha.sdk.HCaptcha
import com.hcaptcha.sdk.HCaptchaConfig
import com.xinto.opencord.BuildConfig
import org.koin.dsl.module

val hcaptchaModule = module {
    fun provideHCaptcha(activity: Context): HCaptcha {
        val config = HCaptchaConfig.builder()
            .siteKey(BuildConfig.CAPTCHA_KEY) // doubt this will ever change
            .resetOnTimeout(true)
            .tokenExpiration(1000000L)
            .build()
        return HCaptcha.getClient(activity).setup(config)
    }

    single { params -> provideHCaptcha(params.get()) }
}
