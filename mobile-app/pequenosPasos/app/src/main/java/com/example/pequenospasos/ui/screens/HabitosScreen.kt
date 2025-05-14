package com.example.pequenospasos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenospasos.R
import com.example.pequenospasos.ui.components.CustomTopBar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenospasos.viewmodel.ComidaViewModel
import com.example.pequenospasos.viewmodel.SiestaViewModel
import com.example.pequenospasos.viewmodel.HigieneViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitosScreen(
    ninoId: Long,
    onComidaClick: () -> Unit,
    onHigieneClick: () -> Unit,
    onSiestaClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val comidaViewModel: ComidaViewModel = viewModel()
    val siestaViewModel: SiestaViewModel = viewModel()
    val higieneViewModel: HigieneViewModel = viewModel()

    LaunchedEffect(ninoId) {
        comidaViewModel.loadComidas(ninoId)
        siestaViewModel.loadSiestas(ninoId)
        higieneViewModel.loadHigienes(ninoId)
    }

    val comidasHoy by comidaViewModel.comidasDeHoy.collectAsState()
    val siestasHoy by siestaViewModel.siestasDeHoy.collectAsState()
    val higienesHoy by higieneViewModel.higienesDeHoy.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Hábitos de hoy",
                showProfileIcon = true,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hábitos", style = TextStyle(fontSize = 24.sp))
            Spacer(modifier = Modifier.height(16.dp))

            // Definición segura de última siesta, higiene y comida
            val ultimaSiesta = siestasHoy.firstOrNull()?.let {
                it.inicioSiesta?.takeIf { hora -> hora.length >= 16 }?.substring(11, 16) ?: "--:--"
            } ?: "--:--"

            val ultimaHigiene = higienesHoy.firstOrNull()?.let {
                it.fechaHora?.split("T")?.getOrNull(1)?.take(5) ?: "--:--"
            } ?: "--:--"

            val ultimaComida = comidasHoy.firstOrNull()?.let {
                it.horaComida?.split("T")?.getOrNull(1)?.take(5) ?: "--:--"
            } ?: "--:--"

            // Card de Comida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1E4FF))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val comida = comidasHoy.firstOrNull()
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Comida: ${comida?.descripcionComida ?: "Sin datos"}", style = TextStyle(fontSize = 18.sp, color = Color(0xFF6200EA)))
                        Text("Observaciones: ${comida?.observaciones ?: "-"}", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                        Text("Última comida: $ultimaComida", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                        TextButton(onClick = onComidaClick) {
                            Text("Mostrar más comidas")
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_comida),
                        contentDescription = "Comida",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Card de Siesta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val siesta = siestasHoy.firstOrNull()
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Última siesta: $ultimaSiesta", style = TextStyle(fontSize = 18.sp, color = Color(0xFF00796B)))
                        TextButton(onClick = onSiestaClick) {
                            Text("Mostrar más siestas")
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_siesta),
                        contentDescription = "Siesta",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Card de Higiene
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val higiene = higienesHoy.firstOrNull()
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Última higiene: $ultimaHigiene - ${higiene?.estado ?: "Sin datos"}", style = TextStyle(fontSize = 18.sp, color = Color(0xFFF57F17)))
                        TextButton(onClick = onHigieneClick) {
                            Text("Mostrar más higienes")
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_higiene),
                        contentDescription = "Higiene",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}
