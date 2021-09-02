package com.xinto.opencord.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xinto.opencord.R

private val light = Font(R.font.inter_light, FontWeight.Light)
private val regular = Font(R.font.inter_regular, FontWeight.Normal)
private val medium = Font(R.font.inter_medium, FontWeight.Medium)
private val semibold = Font(R.font.inter_semibold, FontWeight.SemiBold)
private val bold = Font(R.font.inter_bold, FontWeight.Bold)

private val interFontFamily = FontFamily(light, regular, medium, semibold, bold)

val Typography = Typography(
    defaultFontFamily =  interFontFamily,
    h1 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    h2 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),
    h5 = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
    ),
    h6 = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.15.sp
    ),
    body1 = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    )
)