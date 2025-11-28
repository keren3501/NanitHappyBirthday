package com.example.nanithappybirthday.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nanithappybirthday.R

val BentonSans = FontFamily(
    Font(R.font.benton_sans, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = BentonSans,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
        letterSpacing = (-0.42).sp
    ),

    bodyMedium = TextStyle(
        fontFamily = BentonSans,
        fontWeight = FontWeight.W500,
        fontSize = 21.sp,
        letterSpacing = (-0.42).sp
    ),

    bodySmall = TextStyle(
        fontFamily = BentonSans,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        letterSpacing = (-0.36).sp
    )

)