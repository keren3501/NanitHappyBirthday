package com.example.nanithappybirthday.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nanithappybirthday.R
import com.example.nanithappybirthday.model.Age
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.ui.theme.TextColor

@Composable
fun BirthdayScreen(birthdayData: BirthdayData) {
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
                Spacer(modifier = Modifier
                    .width(50.dp)
                    .weight(1f))

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AgeSection(
                        birthdayData.name,
                        birthdayData.getFormattedAge(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 20.dp, bottom = 15.dp)
                    )

                    BabyImageSection(
                        theme.babyImage,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier
                    .width(50.dp)
                    .weight(1f))
            }

            Image(
                painter = painterResource(theme.backgroundImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                alignment = Alignment.BottomCenter,
                contentScale = ContentScale.FillWidth
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
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left_swirls),
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Image(
                    painter = painterResource(id = ageAsset),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp).weight(2f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Image(
                    painter = painterResource(id = R.drawable.right_swirls),
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${age.getFormattedUnit()} old".uppercase(),
                textAlign = TextAlign.Center,
                color = TextColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BabyImageSection(
    babyImage: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = babyImage),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.drawable.nanit),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun PelicanPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("geffen", 1688544000, "pelican"))
    }
}

@Preview
@Composable
fun FoxPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("tagel", 1737072000, "fox"))
    }
}

@Preview
@Composable
fun ElephantPreview() {
    NanitHappyBirthdayTheme {
        BirthdayScreen(BirthdayData("keren", 1760044800, "elephant"))
    }
}
