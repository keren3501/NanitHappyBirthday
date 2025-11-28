package com.example.nanithappybirthday.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.nanithappybirthday.R
import com.example.nanithappybirthday.model.Age
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.ui.theme.TextColor
import java.io.File
import kotlin.math.cos
import kotlin.math.sin

private val MAIN_COLUMN_WIDTH = 200.dp
private const val UPLOAD_IMG_DEGREE = 45.0

@Composable
fun BirthdayScreen(birthdayData: BirthdayData, onUploadImageClicked: () -> Unit = {}) {
    val theme = themes[birthdayData.theme]

    if (theme != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.backgroundColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Spacer(
                    modifier = Modifier
                        .width(50.dp)
                )

                Column(
                    modifier = Modifier
                        .width(MAIN_COLUMN_WIDTH),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AgeSection(
                        birthdayData.name,
                        birthdayData.getFormattedAge(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 20.dp, bottom = 15.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BabyImageSection(theme, birthdayData.imagePath, onUploadImageClicked)

                        Spacer(modifier = Modifier.height(15.dp))

                        Image(
                            painter = painterResource(id = R.drawable.nanit),
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(
                    modifier = Modifier
                        .width(50.dp)
                )
            }

            Image(
                painter = painterResource(theme.backgroundImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
                alignment = Alignment.BottomCenter,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun AgeSection(name: String, age: Age, modifier: Modifier = Modifier) {
    val ageAsset = numberAssets[age.value]

    if (ageAsset != null) {
        Column(
            modifier = modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "today $name is".uppercase(),
                color = TextColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(13.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.left_swirls),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Image(
                    painter = painterResource(id = R.drawable.right_swirls),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                Image(
                    painter = painterResource(id = ageAsset),
                    contentDescription = null,
                    modifier = Modifier
                        .height(88.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${age.getFormattedUnit()} old".uppercase(),
                textAlign = TextAlign.Center,
                color = TextColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun BabyImageSection(
    babyTheme: ThemeAssets,
    imagePath: String?,
    onUploadImageClicked: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = if (imagePath != null && File(imagePath).exists())
                rememberAsyncImagePainter(model = imagePath)
            else painterResource(id = babyTheme.babyImage),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape).size(MAIN_COLUMN_WIDTH),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = babyTheme.imageBorder),
            contentDescription = null,
        )

        val angleRadians = Math.toRadians(UPLOAD_IMG_DEGREE)
        val radius = MAIN_COLUMN_WIDTH / 2

        val xOffsetDp = (radius * cos(angleRadians).toFloat())
        val yOffsetDp = (radius * sin(angleRadians).toFloat())

        Image(
            painter = painterResource(id = babyTheme.uploadImage),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .offset(xOffsetDp, -yOffsetDp)
                .clickable { onUploadImageClicked() }
        )
    }
}

@Preview
@Composable
fun PelicanPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("geffen", 1754946000000, "pelican"))
    }
}

@Preview
@Composable
fun FoxPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("tagel", 1754946000000, "fox"))
    }
}

@Preview
@Composable
fun ElephantPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("keren", 1754946000000, "elephant"))
    }
}
