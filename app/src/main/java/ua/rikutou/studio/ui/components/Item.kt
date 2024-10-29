package ua.rikutou.studio.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun Item(
    modifier: Modifier = Modifier,
    imageURL: String? = null,
    title: String,
    comment: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color = Color.LightGray)
            .padding(2.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageURL?.let {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, color = Color.LightGray)
                    .padding(2.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = title
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1F)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = comment
            )
        }
    }
}

@Composable
@Preview (showBackground = true)
private fun ItemPreview(modifier: Modifier = Modifier) {
    Item(
        modifier = Modifier,
        imageURL = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.theemotionmachine.com%2Fthe-power-of-a-nice-view-feast-your-eyes-on-beauty%2F&psig=AOvVaw0JPb2DvN9eXCC_GQWVmIZ-&ust=1730310577365000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCPjD26yTtIkDFQAAAAAdAAAAABAE",
        title = "image",
        comment = "from internet"
    )
}