package com.virdapp.listingpost.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val clear_dark = Color(0xFFA05162)
val dark_btn = Color(0xFF222427)

// define your colors for dark theme
val light_btn = Color(android.graphics.Color.parseColor("#E9F0F4"))
val light_bg = Color(android.graphics.Color.parseColor("#F6F8F9"))
val clear_light = Color(0xFFF1C8D1)

sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color
)  {
    object Night: ThemeColors(
        background = Color.Black,
        surface = dark_btn,
        primary = clear_dark,
        text = Color.White
    )
    object Day: ThemeColors(
        background = light_bg,
        surface = light_btn,
        primary = clear_light,
        text = Color.Black
    )


}