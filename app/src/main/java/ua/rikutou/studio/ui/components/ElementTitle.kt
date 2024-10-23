package ua.rikutou.studio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.rikutou.studio.R

@Composable
fun ElementTitle(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = modifier
                .weight(1f),
            text =title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Image(
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onClick()
                },
            painter = painterResource(R.drawable.edit),
            contentDescription = "Edit"
        )
    }


}

@Composable
@Preview(showBackground = true)
private fun ElementTitlePreview(modifier: Modifier = Modifier) {
    ElementTitle(title = "Black crane")
}