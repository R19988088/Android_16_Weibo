package com.example.myweibo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private fun TextUnit.plusSp(value: Float): TextUnit =
    if (this == TextUnit.Unspecified) this else (this.value + value).sp

private fun TextStyle.defaultTextRoom(): TextStyle =
    copy(
        fontSize = fontSize.plusSp(2f),
        lineHeight = lineHeight.plusSp(3f),
    )

private val BaseTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Typography = BaseTypography.copy(
    displayLarge = BaseTypography.displayLarge.defaultTextRoom(),
    displayMedium = BaseTypography.displayMedium.defaultTextRoom(),
    displaySmall = BaseTypography.displaySmall.defaultTextRoom(),
    headlineLarge = BaseTypography.headlineLarge.defaultTextRoom(),
    headlineMedium = BaseTypography.headlineMedium.defaultTextRoom(),
    headlineSmall = BaseTypography.headlineSmall.defaultTextRoom(),
    titleLarge = BaseTypography.titleLarge.defaultTextRoom(),
    titleMedium = BaseTypography.titleMedium.defaultTextRoom(),
    titleSmall = BaseTypography.titleSmall.defaultTextRoom(),
    bodyLarge = BaseTypography.bodyLarge.defaultTextRoom(),
    bodyMedium = BaseTypography.bodyMedium.defaultTextRoom(),
    bodySmall = BaseTypography.bodySmall.defaultTextRoom(),
    labelLarge = BaseTypography.labelLarge.defaultTextRoom(),
    labelMedium = BaseTypography.labelMedium.defaultTextRoom(),
    labelSmall = BaseTypography.labelSmall.defaultTextRoom(),
)
