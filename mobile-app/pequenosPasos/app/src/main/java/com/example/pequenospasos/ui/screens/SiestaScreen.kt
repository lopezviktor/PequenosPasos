package com.example.pequenospasos.ui.screens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.SiestaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiestaScreen(
    navController: NavHostController,
    viewModel: SiestaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val siestas by viewModel.siestas.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            selectedDate = LocalDate.of(year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val siestasFiltradas = if (selectedDate != null) {
        siestas.filter {
            LocalDate.parse(it.inicioSiesta.substring(0, 10)) == selectedDate
        }
    } else {
        siestas
    }

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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedDate?.format(dateFormatter) ?: "Filtrar por fecha",
                        modifier = Modifier.clickable { datePicker.show() },
                        color = Color(0xFF6B9D98),
                        fontWeight = FontWeight.Bold
                    )

                    if (selectedDate != null) {
                        Text(
                            text = "Limpiar",
                            modifier = Modifier.clickable { selectedDate = null },
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (siestasFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "No hay siestas registradas en esta fecha.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(siestasFiltradas.sortedByDescending { it.inicioSiesta }, key = { it.id }) { siesta ->
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F1FF)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "😴 Inicio: ${formatearFecha(siesta.inicioSiesta)}",
                                fontWeight = FontWeight.Bold
                            )
                            siesta.finSiesta?.let {
                                Text(
                                    text = "⏰ Fin: ${formatearFecha(it)}"
                                )
                            }
                            siesta.observaciones?.let {
                                Text(
                                    text = "💬 $it"
                                )
                            }
                            Text(
                                text = "👩‍🏫 ${siesta.educador}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSiestas(1) // Cambiar por el ID real del niño
    }
}