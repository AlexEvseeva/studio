package ua.rikutou.studio.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import ua.rikutou.studio.R

@Composable
fun Item(
    modifier: Modifier = Modifier,
    imageURL: String? = null,
    title: String,
    checked: Boolean = false,
    comment: String,
    onItemClick: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(4.dp)
            .clickable {
                onItemClick()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageURL?.let {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    ,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = title
            )
        } ?: run {
            Spacer(
                modifier = Modifier
                    .size(50.dp)
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
        Icon(
            modifier = Modifier.clickable {
                onAddToCart()
            },
            painter = painterResource(R.drawable.cart),
            contentDescription = null,
            tint = if(checked) Color.Gray else Color.Black
        )

    }
}

@Composable
@Preview (showBackground = true)
private fun ItemPreview(modifier: Modifier = Modifier) {
    Item(
        modifier = Modifier,
        imageURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Field_Hamois_Belgium_Luc_Viatour.jpg/280px-Field_Hamois_Belgium_Luc_Viatour.jpg",
        title = "image",
        comment = "from internet"
    )
}