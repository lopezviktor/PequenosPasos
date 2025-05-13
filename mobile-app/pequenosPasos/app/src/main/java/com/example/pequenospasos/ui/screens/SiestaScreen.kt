package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.SiestaViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiestaScreen(
    navController: NavHostController,
    viewModel: SiestaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val siestas by viewModel.siestas.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Siestas",
                showProfileIcon = true,
                onProfileClick = { navController.navigate("perfil_screen") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(siestas, key = { it.id }) { siesta ->
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Inicio: ${formatearFecha(siesta.inicioSiesta)}",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        siesta.finSiesta?.let {
                            Text(
                                text = "Fin: ${formatearFecha(it)}",
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                        siesta.observaciones?.let {
                            Text(
                                text = "Observaciones: $it",
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                        Text(
                            text = "Educador: ${siesta.educador}",
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSiestas(1) // Cambiar por el ID real del niño
    }
}