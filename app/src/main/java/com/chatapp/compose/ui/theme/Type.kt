package com.chatapp.compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chatapp.compose.R

// Set of Material typography styles to start with
val varelaFont = FontFamily(Font(R.font.varela_regular))
val balsamiqSansRegular = FontFamily(Font(R.font.balsamiq_sans_regular))
val montserratAlternatesMedium = FontFamily(Font(R.font.montserrat_alternates_medium))
val macondoRegular = FontFamily(Font(R.font.macondo_regular))
val gamjaFlowerRegular = FontFamily(Font(R.font.gamjaflower_regular))
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = montserratAlternatesMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val chatAppDefaultTextStyle = TextStyle(
    fontFamily = balsamiqSansRegular,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
)

val titleLarge = TextStyle(
    fontFamily = balsamiqSansRegular,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
)

val labelSmall = TextStyle(
    fontFamily = montserratAlternatesMedium,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

val chatTextStyle = TextStyle(
    fontFamily = varelaFont,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
)

val smallTextStyle = TextStyle(
    fontFamily = balsamiqSansRegular,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
val normalTextStyle = TextStyle(
    fontFamily = balsamiqSansRegular,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
val mediumTextStyle = TextStyle(
    fontFamily = balsamiqSansRegular,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
