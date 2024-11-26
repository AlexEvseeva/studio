package ua.rikutou.studio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.rikutou.studio.R

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onOk: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
            ,
            text = stringResource(R.string.deleteMessage),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
            ,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { onOk() }
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    color = Color.White
                )
            }
            Button(
                onClick = { onCancel() }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.White
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteDialogPreview(modifier: Modifier = Modifier) {
    DeleteDialog(
        modifier = Modifier,
        onOk = {},
        onCancel = {}
    )
}