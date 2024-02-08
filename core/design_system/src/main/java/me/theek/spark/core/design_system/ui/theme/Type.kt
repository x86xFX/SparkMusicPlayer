package me.theek.spark.core.design_system.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import me.theek.spark.core.design_system.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val InriaSans = GoogleFont(name = "Inria Sans")
val InriaSansFontFamily = FontFamily(
    Font(
        googleFont = InriaSans,
        fontProvider = provider
    )
)

val Nunito = GoogleFont(name = "Nunito")
val NunitoFontFamily = FontFamily(
    Font(
        googleFont = Nunito,
        fontProvider = provider
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)