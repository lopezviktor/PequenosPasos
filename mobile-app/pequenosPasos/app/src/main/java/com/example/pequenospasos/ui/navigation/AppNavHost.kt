package com.example.pequenospasos.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pequenospasos.ui.screens.ActividadesScreen
import com.example.pequenospasos.ui.screens.ComidaScreen
import com.example.pequenospasos.ui.screens.HabitosScreen
import com.example.pequenospasos.ui.screens.HigieneScreen
import com.example.pequenospasos.ui.screens.LoginScreen
import com.example.pequenospasos.ui.screens.MenuPrincipal
import com.example.pequenospasos.ui.screens.PerfilScreen
import com.example.pequenospasos.ui.screens.SeleccionHijoScreen
import com.example.pequenospasos.ui.screens.SiestaScreen
import com.example.pequenospasos.viewmodel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("seleccion_hijo_screen") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Nueva pantalla para seleccionar hijo
        composable("seleccion_hijo_screen") {
            SeleccionHijoScreen(
                navController = navController,
                loginViewModel = viewModel()
            )
        }

        // Pantalla de Menú Principal
        composable("menu_principal/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            MenuPrincipal(
                onComidaClick = { navController.navigate("comida_screen/$ninoId") },
                onHigieneClick = { navController.navigate("higiene_screen/$ninoId") },
                onSiestaClick = { navController.navigate("siesta_screen/$ninoId") },
                onHabitosClick = { navController.navigate("habitos_screen/$ninoId") },
                onProfileClick = { navController.navigate("perfil_screen") },
                onActividadesClick = { navController.navigate("actividades_screen") }
            )
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
            )
        }

        // Pantalla de Siesta
        composable("siesta_screen/{ninoId}") { backStackEntry ->
            val ninoId = backStackEntry.arguments?.getString("ninoId")?.toLongOrNull() ?: 0L
            SiestaScreen(
                navController = navController,
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
                onProfileClick = { navController.navigate("perfil_screen") }
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
            PerfilScreen()
        }
    }
}