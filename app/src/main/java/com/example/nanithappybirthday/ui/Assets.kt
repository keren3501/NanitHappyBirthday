package com.example.nanithappybirthday.ui

import androidx.compose.ui.graphics.Color
import com.example.nanithappybirthday.R
import com.example.nanithappybirthday.ui.theme.ElephantBackgroundColor
import com.example.nanithappybirthday.ui.theme.FoxBackgroundColor
import com.example.nanithappybirthday.ui.theme.PelicanBackgroundColor

data class ThemeAssets(
    val backgroundColor: Color,
    val backgroundImage: Int,
    val babyImage: Int,
    val uploadImage: Int
)

val themes: HashMap<String, ThemeAssets> = hashMapOf(
    Pair(
        "fox",
        ThemeAssets(
            FoxBackgroundColor,
            R.drawable.bg_fox,
            R.drawable.baby_image_fox,
            R.drawable.upload_photo_fox
        )
    ),
    Pair(
        "pelican",
        ThemeAssets(
            PelicanBackgroundColor,
            R.drawable.bg_pelican,
            R.drawable.baby_image_pelican,
            R.drawable.upload_photo_pelican
        )
    ),
    Pair(
        "elephant",
        ThemeAssets(
            ElephantBackgroundColor,
            R.drawable.bg_elephant,
            R.drawable.baby_image_elephant,
            R.drawable.upload_photo_elephant
        )
    )
)

val numberAssets: HashMap<Int, Int> = hashMapOf(
    Pair(0, R.drawable.zero),
    Pair(1, R.drawable.one),
    Pair(2, R.drawable.two),
    Pair(3, R.drawable.three),
    Pair(4, R.drawable.four),
    Pair(5, R.drawable.five),
    Pair(6, R.drawable.six),
    Pair(7, R.drawable.seven),
    Pair(8, R.drawable.eight),
    Pair(9, R.drawable.nine),
    Pair(10, R.drawable.ten),
    Pair(11, R.drawable.eleven),
    Pair(12, R.drawable.twelve),
)