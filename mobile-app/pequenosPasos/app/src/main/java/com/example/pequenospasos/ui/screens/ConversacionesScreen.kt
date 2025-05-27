package com.example.pequenospasos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.navigation.NavHostController
import com.example.pequenospasos.data.model.Educador
import com.example.pequenospasos.data.model.Mensaje
import com.example.pequenospasos.viewmodel.MensajesViewModel

import com.example.pequenospasos.R
import com.example.pequenospasos.ui.components.CustomTopBar

@Composable
fun ConversacionesScreen(
    navController: NavHostController,
    viewModel: MensajesViewModel,
    onChatClick: (Long) -> Unit, // Ahora pasaremos el ID del educador
    padreId: Long
) {
    val educadores by viewModel.educadores.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.cargarEducadoresConConversacion(padreId)
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Mensajes",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(educadores) { educador ->
                ConversacionItem(educador) {
                    onChatClick(educador.id)
                }
            }
        }
    }
}

@Composable
fun ConversacionItem(educador: Educador, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2B3E8)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_educadora),
                contentDescription = "Educadora",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${educador.nombre} ${educador.apellidos}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Toca para ver la conversación",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}