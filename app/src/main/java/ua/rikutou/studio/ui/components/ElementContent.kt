package ua.rikutou.studio.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ElementContent(
    modifier: Modifier = Modifier,
    label: String,
    name: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(end = 8.dp),
            text = "$label: "
        )

        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
            text = name
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ElementContentPreview(modifier: Modifier = Modifier) {
    ElementContent(label = "adress", name = "Kharkiv...")
}