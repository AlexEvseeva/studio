package ua.rikutou.studio.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    imageURL: String
) {
    AsyncImage(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageURL)
            .build(),
        contentScale = ContentScale.FillBounds,
        contentDescription = null
    )
}