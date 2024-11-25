package ua.rikutou.studio.ui.components

import android.widget.CheckBox
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R
import ua.rikutou.studio.data.local.entity.UserEntity

@Composable
fun UserItem(
    user: UserEntity,
    onCheckChange: (UserEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(4.dp)
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 8.dp)
            ,
            text = user.name
        )
        Checkbox(
            checked = user.studioId?.let { it > 0 } ?: false,
            onCheckedChange = {
                onCheckChange(user)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(
        user = UserEntity(
            userId = 1L,
            name = "john.doe@gmail.com",
            studioId = 1L,
        )
    ) { }
}