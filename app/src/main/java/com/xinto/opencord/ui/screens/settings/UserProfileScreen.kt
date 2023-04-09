package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.size.Size
import com.xinto.opencord.R
import com.xinto.opencord.ui.components.OCAsyncImage
import com.xinto.opencord.ui.screens.divider
import com.xinto.opencord.ui.screens.section

context(LazyListScope)

@OptIn(ExperimentalLayoutApi::class)
fun userProfileScreen() {
    item {
        BoxWithConstraints(
            modifier = Modifier
                .fillParentMaxWidth()
                .heightIn(min = 240.dp, max = 600.dp),
        ) {
            OCAsyncImage(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillParentMaxWidth()
                    .height(minHeight / 2),
                url = "https://pbs.twimg.com/media/FUNI2HbXwAAEtyp.jpg",
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart),
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .padding(8.dp)
                ) {
                    OCAsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                            .clickable {
                                // Show a bottom sheet
                            },
                        size = Size(256, 256),
                        url = "https://pbs.twimg.com/media/FUNI2HbXwAAEtyp.jpg",
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { /*TODO*/ },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface,
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null,
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("opencord")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    ) {
                        append("#1234")
                    }
                },
                style = MaterialTheme.typography.titleLarge,
            )

            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                )
            }

            // Badges
            FlowRow(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                maxItemsInEachRow = 4,
            ) {
                @Composable
                fun badge(url: String) {
                    OCAsyncImage(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                            .clip(CircleShape),
                        url = url,
                    )
                }

                badge("https://preview.redd.it/0oaxe7asyw461.jpg?auto=webp&s=f88005a202db447bc1029ac97f161b3d37666693")
                badge("https://pbs.twimg.com/profile_images/1584258394977931265/lrZvCd27_400x400.jpg")
                badge("https://i.redd.it/hexrtw5zxk541.jpg")
                badge("https://preview.redd.it/38m4cld3cyt41.png?width=640&crop=smart&auto=webp&v=enabled&s=b5536900fc8abbab49ee851260d9c157d4ff2d66")
            }
        }
    }

    divider()

    section("About Me") {
        item {
            TextField(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(16.dp),
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Tap to add an about me")
                },
                minLines = 5,
            )
        }
    }
}
