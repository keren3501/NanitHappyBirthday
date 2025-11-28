package com.example.nanithappybirthday.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
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

private const val UPLOAD_IMG_DEGREE = 45.0

private val BABY_IMG_SIZE = 200.dp
private val LOGO_HEIGHT = 20.dp
private val LOGO_TOP_MARGIN = 15.dp

fun calcAgeSectionOffset(totalScreenHeight: Dp): Dp {
    val babyImageSectionSize = (BABY_IMG_SIZE + LOGO_HEIGHT + LOGO_TOP_MARGIN)
    val availableSpaceForAgeSection = (totalScreenHeight / 2) - (babyImageSectionSize / 2)
    val offset = (babyImageSectionSize / 2) + (availableSpaceForAgeSection / 2)

    return -offset
}

fun calcUploadIconOffset(): DpOffset {
    val angleRadians = Math.toRadians(UPLOAD_IMG_DEGREE)
    val radius = BABY_IMG_SIZE / 2

    val xOffsetDp = (radius * cos(angleRadians).toFloat())
    val yOffsetDp = (radius * sin(angleRadians).toFloat())

    return DpOffset(xOffsetDp, -yOffsetDp)
}

@Composable
fun BirthdayScreen(
    birthdayData: BirthdayData,
    babyImagePath: String? = null,
    onUploadImageClicked: () -> Unit = {}
) {
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

                BoxWithConstraints(
                    modifier = Modifier
                        .width(BABY_IMG_SIZE)
                        .fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp)
                            .offset(y = calcAgeSectionOffset(maxHeight)),
                        contentAlignment = Alignment.Center
                    ) {
                        AgeSection(
                            birthdayData.name,
                            birthdayData.getFormattedAge()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BabyImageSection(theme, babyImagePath, onUploadImageClicked)

                        Spacer(modifier = Modifier.height(LOGO_TOP_MARGIN))

                        Image(
                            painter = painterResource(id = R.drawable.nanit),
                            contentDescription = null,
                            modifier = Modifier.height(LOGO_HEIGHT)
                        )
                    }
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
    val context = LocalContext.current
    val ageAsset = numberAssets[age.value]

    if (ageAsset != null) {
        Column(
            modifier = modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.today_is_msg, name.uppercase()),
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
                text = stringResource(R.string.old_msg, age.getFormattedUnit(context)),
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
            modifier = Modifier
                .clip(CircleShape)
                .size(BABY_IMG_SIZE),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = babyTheme.imageBorder),
            contentDescription = null,
        )

        val uploadIconOffset = calcUploadIconOffset()

        Image(
            painter = painterResource(id = babyTheme.uploadImage),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .offset(uploadIconOffset.x, uploadIconOffset.y)
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
