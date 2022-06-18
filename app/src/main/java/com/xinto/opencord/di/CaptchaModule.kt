package com.xinto.opencord.di

import android.content.Context
import com.hcaptcha.sdk.HCaptcha
import com.hcaptcha.sdk.HCaptchaConfig
import org.koin.dsl.module

val captchaModule = module {
    fun provideHCaptcha(context: Context): HCaptcha {
        val config = HCaptchaConfig.builder()
            .siteKey("f5561ba9-8f1e-40ca-9b5b-a0b3f719ef34") // doubt this will ever change
            .resetOnTimeout(true)
            .build()
        return HCaptcha.getClient(context).setup(config)
    }

    single { provideHCaptcha(get()) }
}