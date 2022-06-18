package com.xinto.opencord.di

import android.content.Context
import com.hcaptcha.sdk.HCaptcha
import com.hcaptcha.sdk.HCaptchaConfig
import com.xinto.opencord.BuildConfig
import org.koin.dsl.module

val captchaModule = module {
    fun provideHCaptcha(context: Context): HCaptcha {
        val config = HCaptchaConfig.builder()
            .siteKey(BuildConfig.CAPTCHA_KEY) // doubt this will ever change
            .resetOnTimeout(true)
            .build()
        return HCaptcha.getClient(context).setup(config)
    }

    single { provideHCaptcha(get()) }
}