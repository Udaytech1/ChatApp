package com.chatapp.compose.ui.comman_widgets

import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chatapp.compose.ui.theme.WhiteColor

@Composable
fun CommanButton(textTitle: String, modifier: Modifier = Modifier, onButtonClicked: () -> Unit) {
    Button(
        onClick = {
            onButtonClicked()
        },
        modifier = modifier
    ) {
        Text(text = textTitle, color = WhiteColor)
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CommanButtonPreview() {
    CommanButton(textTitle = "Login") {

    }
}