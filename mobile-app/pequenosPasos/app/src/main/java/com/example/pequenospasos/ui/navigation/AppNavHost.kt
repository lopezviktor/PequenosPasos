package com.example.pequenospasos.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pequenospasos.data.network.RetrofitClient
import com.example.pequenospasos.data.repository.NotificacionesRepository
import com.example.pequenospasos.ui.screens.ActividadesScreen
import com.example.pequenospasos.ui.screens.ChatScreen
import com.example.pequenospasos.ui.screens.ComidaScreen
import com.example.pequenospasos.ui.screens.ConversacionesScreen
import com.example.pequenospasos.ui.screens.HabitosScreen
import com.example.pequenospasos.ui.screens.HigieneScreen
import com.example.pequenospasos.ui.screens.LoginScreen
import com.example.pequenospasos.ui.screens.MenuPrincipal
import com.example.pequenospasos.ui.screens.NotificacionesScreen
import com.example.pequenospasos.ui.screens.PerfilScreen
import com.example.pequenospasos.ui.screens.SeleccionHijoScreen
import com.example.pequenospasos.ui.screens.SiestaScreen
import com.example.pequenospasos.viewmodel.LoginViewModel
import com.example.pequenospasos.viewmodel.NotificacionesViewModel
import com.example.pequenospasos.viewmodel.NotificacionesViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        // Pantalla de Login
        composable("login") {
            val padre by loginViewModel.padre.collectAsState()
            val hijosCargados by loginViewModel.hijosCargados.collectAsState()

            LaunchedEffect(hijosCargados) {
                if (hijosCargados) {
                    navController.navigate("seleccion_hijo_screen") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                loginViewModel = loginViewModel
            )
        }

        // Pantalla para seleccionar hijo
        composable("seleccion_hijo_screen") {
            SeleccionHijoScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }

        // Pantalla de Menú Principal
        composable("menu_principal") {
            val nino by loginViewModel.ninoSeleccionado.collectAsState()
            val notificacionesNoLeidas by loginViewModel.notificacionesNoLeidas.collectAsState()
            val notificacionesViewModel: NotificacionesViewModel = viewModel(
                factory = NotificacionesViewModelFactory(
                    NotificacionesRepository(RetrofitClient.api)
                )
            )

            nino?.let {
                MenuPrincipal(
                    navController = navController,
                    nino = it,
                    nombre = it.nombre,
                    apellidos = it.apellidos,
                    onComidaClick = { navController.navigate("comida_screen/${it.id}") },
                    onHigieneClick = { navController.navigate("higiene_screen/${it.id}") },
                    onSiestaClick = { navController.navigate("siesta_screen/${it.id}") },
                    onHabitosClick = { navController.navigate("habitos_screen/${it.id}") },
                    onProfileClick = { navController.navigate("perfil_screen") },
                    onActividadesClick = { navController.navigate("actividades_screen") },
                    onNotificacionesClick = { navController.navigate("notificaciones_screen") },
                    onMensajesClick = { navController.navigate("conversaciones_screen") },
                    padre = loginViewModel.padre.value!!,
                    notificacionesViewModel = notificacionesViewModel
                )
            }
        }

        // Pantalla de Comida
        composable("comida_screen/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            ComidaScreen(
                navController = navController,
                ninoId = ninoId
            )
        }

        // Pantalla de Higiene
        composable("higiene_screen/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            HigieneScreen(
                navController = navController,
                ninoId = ninoId,
            )
        }

        // Pantalla de Siesta
        composable("siesta_screen/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            SiestaScreen(
                navController = navController,
                ninoId = ninoId,
            )
        }

        // Pantalla de Hábitos
        composable("habitos_screen/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            HabitosScreen(
                ninoId = ninoId,
                onComidaClick = { navController.navigate("comida_screen/$ninoId") },
                onHigieneClick = { navController.navigate("higiene_screen/$ninoId") },
                onSiestaClick = { navController.navigate("siesta_screen/$ninoId") },
                onProfileClick = { navController.navigate("perfil_screen") },
            )
        }

        // Pantalla de actividades
        composable("actividades_screen"){
            ActividadesScreen(
                navController = navController
            )
        }

        // Pantalla de Perfil
        composable("perfil_screen") {
            val padre by loginViewModel.padre.collectAsState()

            padre?.let {
                PerfilScreen(padre = it)
            }
        }

        // Pantalla de Notificaciones
        composable("notificaciones_screen") {
            val padre by loginViewModel.padre.collectAsState()
            padre?.let {
                val factory = NotificacionesViewModelFactory(
                    NotificacionesRepository(
                        RetrofitClient.api
                    )
                )
                val viewModel: NotificacionesViewModel = viewModel(factory = factory)
                NotificacionesScreen(
                    navController = navController,
                    padreId = it.id,
                    viewModel = viewModel
                )
            }
        }

        // Pantalla de Mensajes
        composable("conversaciones_screen") {
            val padre by loginViewModel.padre.collectAsState()
            padre?.let {
                ConversacionesScreen(
                    navController = navController,
                    viewModel = viewModel(),
                    onChatClick = { educadorId ->
                        navController.navigate("chat_screen/$educadorId")
                    },
                    padreId = it.id
                )
            }
        }

        // Pantalla de Chat
        composable("chat_screen/{conversacionId}") { backStackEntry ->
            val conversacionId =
                backStackEntry.arguments?.getString("conversacionId")?.toLongOrNull() ?: 0L
            val padre by loginViewModel.padre.collectAsState()
            padre?.let {
                ChatScreen(
                    navController = navController,
                    chatId = conversacionId,
                    viewModel = viewModel(),  // o el MensajesViewModel correcto
                    padre = it
                )
            }
        }
    }
}