package com.finflio.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.finflio.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

val Inter = FontFamily(
    listOf(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold)
    )
)

val DMSans = FontFamily(
    listOf(
        Font(R.font.dmsans_bold, FontWeight.Bold),
        Font(R.font.dmsans_bolditalic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.dmsans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.dmsans_medium, FontWeight.Medium),
        Font(R.font.dmsans_regular, FontWeight.Normal),
        Font(R.font.dmsans_mediumitalic, FontWeight.Medium, FontStyle.Italic)
    )
)

val Poppins = FontFamily(
    listOf(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium)
    )
)

val Syne = FontFamily(
    listOf(
        Font(R.font.syne_regular, FontWeight.Normal),
        Font(R.font.syne_bold, FontWeight.Bold),
        Font(R.font.syne_extrabold, FontWeight.ExtraBold),
        Font(R.font.syne_medium, FontWeight.Medium),
        Font(R.font.syne_semibold, FontWeight.SemiBold)
    )
)
