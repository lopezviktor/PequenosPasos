package com.example.pequenospasos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pequenospasos.data.model.Educador
import com.example.pequenospasos.data.model.Mensaje
import com.example.pequenospasos.data.model.Padre
import com.example.pequenospasos.viewmodel.MensajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatId: Long,
    viewModel: MensajesViewModel,
    padre: Padre
) {
    val mensajes by viewModel.mensajes.observeAsState(emptyList())
    var nuevoMensaje by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.cargarMensajes(padre.id, chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chat",
                        color = Color(0xFF0F4F9E)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor =  Color(0xC7B8CCE7))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(mensajes.reversed()) { mensaje ->
                    val isEmisor = mensaje.emisorId == padre.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (isEmisor) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (isEmisor) MaterialTheme.colorScheme.primary else Color(
                                0x9E7DE082
                            ),
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = mensaje.contenido,
                                    color = if (isEmisor) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nuevoMensaje,
                    onValueChange = { nuevoMensaje = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") },
                    shape = RoundedCornerShape(24.dp)
                )
                IconButton(onClick = {
                    if (nuevoMensaje.isNotBlank()) {
                        viewModel.enviarMensaje(
                            Mensaje(
                                contenido = nuevoMensaje,
                                emisor = Padre(id = padre.id),
                                receptor = Educador(id = chatId),
                                estado = "NO_LEIDO"
                            )
                        )
                        nuevoMensaje = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}