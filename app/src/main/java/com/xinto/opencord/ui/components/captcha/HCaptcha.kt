package com.xinto.opencord.ui.components.captcha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.hcaptcha.sdk.HCaptcha
import com.hcaptcha.sdk.HCaptchaError
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@Composable
fun HCaptcha(
    onSuccess: (token: String) -> Unit,
    onFailure: (HCaptchaError, code: Int) -> Unit,
) {
    val context = LocalContext.current
    val captcha: HCaptcha = get { parametersOf(context) }
    DisposableEffect(captcha) {
        captcha.verifyWithHCaptcha()
            .addOnSuccessListener {
                onSuccess(it.tokenResult)
            }
            .addOnFailureListener {
                onFailure(it.hCaptchaError, it.statusCode)
            }
        onDispose {}
    }
}
