package com.example.pequenospasos.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.pequenospasos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    backgroundColor: Color = Color(0xFF2383D0),
    modifier: Modifier = Modifier,
    showProfileIcon: Boolean = false,
    onProfileClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 22.sp
            )
        },
        actions = {
            if (showProfileIcon) {
                IconButton(onClick = { onProfileClick?.invoke() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_child),
                        contentDescription = "Perfil",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
    )
}
