package com.example.pequenospasos.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    backgroundColor: Color = Color(0xC7B8CCE7),
    modifier: Modifier = Modifier,
    showProfileIcon: Boolean = false,
    onProfileClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0F4F9E),
                fontSize = 32.sp
            )
        },
        actions = {
            if (showProfileIcon) {
                IconButton(onClick = { onProfileClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color(0xFF0F4F9E),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
    )
}
