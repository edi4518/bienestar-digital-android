package com.example.bienestar_digital_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bienestar_digital_android.core.common.Screen
import com.example.bienestar_digital_android.core.designsystem.BienestardigitalandroidTheme
import com.example.bienestar_digital_android.feature.cazanotificaciones.CazaNotificacionesScreen
import com.example.bienestar_digital_android.feature.decisionesconectadas.DecisionesConectadasView
import com.example.bienestar_digital_android.feature.decisionesconectadas.DecisionesConectadasViewModel
import com.example.bienestar_digital_android.feature.decisionesconectadas.DecisionesViewModelFactory
import com.example.bienestar_digital_android.feature.desmitificador.DesmitificadorScreen
import com.example.bienestar_digital_android.feature.hub.HubScreen
import com.example.bienestar_digital_android.feature.hub.HubUiEvent
import com.example.bienestar_digital_android.feature.hub.HubViewModel
import com.example.bienestar_digital_android.feature.onboarding.OnboardingEvent
import com.example.bienestar_digital_android.feature.onboarding.OnboardingViewModel
import com.example.bienestar_digital_android.feature.onboarding.ProfileScreen
import com.example.bienestar_digital_android.feature.organizador24h.Organizador24hScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BienestardigitalandroidTheme {
                val navController: NavHostController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "profile_route",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 0. Pantalla de Creación de Perfil / Onboarding
                        composable(route = "profile_route") {
                            val viewModel: OnboardingViewModel = viewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                            ProfileScreen(
                                uiState = uiState,
                                onEvent = { event ->
                                    viewModel.onEvent(event)

                                    // Cuando completa el perfil, pasamos el gamertag a la ruta del Hub
                                    if (event is OnboardingEvent.OnSubmitProfile) {
                                        val userTag = uiState.gamertag.ifBlank { "Recluta" }
                                        navController.navigate("${Screen.Hub.route}/$userTag") {
                                            popUpTo("profile_route") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

// 1. Pantalla Principal (Hub) recibiendo el parámetro del nombre
                        composable(
                            route = "${Screen.Hub.route}/{gamertag}"
                        ) { backStackEntry ->
                            val gamertag = backStackEntry.arguments?.getString("gamertag") ?: "Recluta"
                            val viewModel: HubViewModel = viewModel()

                            // Actualizar el nombre en el ViewModel del Hub con el que se trajo del perfil
                            viewModel.setUserName(gamertag)

                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                            HubScreen(
                                uiState = uiState,
                                onEvent = { event ->
                                    // Notificamos al ViewModel para que procese el evento
                                    viewModel.onEvent(event)

                                    // Gestionamos la navegación correspondiente
                                    viewModel.onEvent(event)

                                    when (event) {
                                        is HubUiEvent.OnContinuarNarrativaClick -> {
                                            navController.navigate(Screen.DecisionesConectadas.route)
                                        }

                                        is HubUiEvent.OnOrganizadorClick -> {
                                            navController.navigate(Screen.Organizador24h.route)
                                        }

                                        is HubUiEvent.OnMiniMisionClick -> {
                                            when (event.misionId) {
                                                "mito_verdad" -> navController.navigate(Screen.Desmitificador.route)
                                                "caza_notis" -> navController.navigate(Screen.CazaNotificaciones.route)
                                            }
                                        }

                                        else -> Unit
                                    }
                                }
                            )
                        }

                        // 2. Pantalla destino (Desmitificador)
                        composable(route = Screen.Desmitificador.route) {
                            DesmitificadorScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 3. Caza de Notificaciones
                        composable(route = Screen.CazaNotificaciones.route) {
                            CazaNotificacionesScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 4. Decisiones Conectadas (Narrativa)
                        composable(route = Screen.DecisionesConectadas.route) {
                            val context = LocalContext.current
                            val viewModel: DecisionesConectadasViewModel = viewModel(
                                factory = DecisionesViewModelFactory(context)
                            )

                            DecisionesConectadasView(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 5. Organizador 24 hs
                        composable(route = Screen.Organizador24h.route) {
                            Organizador24hScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}