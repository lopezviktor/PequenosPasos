package com.example.pequenospasos.ui.screens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pequenospasos.ui.components.CustomTopBar
import com.example.pequenospasos.viewmodel.HigieneViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneScreen(
    navController: NavHostController,
    ninoId: Long,
    viewModel: HigieneViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val higienes by viewModel.higienes.collectAsState()

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

    val higienesFiltradas = if (selectedDate != null) {
        higienes.filter {
            LocalDate.parse(it.fechaHora.substring(0, 10)) == selectedDate
        }
    } else {
        higienes
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Higienes",
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
                        color = MaterialTheme.colorScheme.primary,
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

            if (higienesFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "No hay registros de higiene en esta fecha.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(higienesFiltradas.sortedByDescending { it.fechaHora }) { higiene ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🧼 ${higiene.estado}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            higiene.observaciones?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "💬 $it",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            val fechaFormateada = try {
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                                LocalDateTime.parse(higiene.fechaHora).format(formatter)
                            } catch (e: Exception) {
                                higiene.fechaHora
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🕒 $fechaFormateada",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "👩‍🏫 ${higiene.educador}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHigienes(ninoId)
    }
}
