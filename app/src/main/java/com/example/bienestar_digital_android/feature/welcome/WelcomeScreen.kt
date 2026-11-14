package com.example.bienestar_digital_android.feature.welcome

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bienestar_digital_android.R
import com.example.bienestar_digital_android.data.local.UserPreferencesManager
import kotlinx.coroutines.delay

private val MidnightBg = Color(0xFF0D131E)
private val CardBg = Color(0xFF131C2A)
private val GreenBorder = Color(0xFF53836B)
private val LimePrimary = Color(0xFFC3F400)
private val TextOnLime = Color(0xFF0F172A)
private val SubtitleGreen = Color(0xFF7CA88D)
private val MutedText = Color(0xFF6B7280)

@Composable
fun WelcomeScreen(
    onCreateProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    onContinueAsUserClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferencesManager(context) }

    // Estado de carga inicial de la aplicación
    var isLoading by remember { mutableStateOf(true) }
    var esUsuarioHabitual by remember { mutableStateOf(false) }
    var gamertagGuardado by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        delay(1800) // Carga inicial durante 1.8 segundos

        if (prefs.haCompletadoPerfil && prefs.gamertagGuardado.isNotBlank()) {
            esUsuarioHabitual = true
            gamertagGuardado = prefs.gamertagGuardado
        }
        isLoading = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBg)
    ) {
        // Fondo con brillo radial suave en el centro para dar ambientación
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x18C3F400),
                            Color(0x0D53836B),
                            Color.Transparent
                        ),
                        radius = 1100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 36.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. ENCABEZADO Y TÍTULOS
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "BIENESTAR DIGITAL",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.5.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Conecta con tu vida, no con la pantalla",
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    color = SubtitleGreen,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. LOGO CENTRAL
            Image(
                painter = painterResource(id = R.drawable.logo_bienestar),
                contentDescription = "Logo Bienestar Digital",
                modifier = Modifier
                    .size(230.dp)
                    .clip(RoundedCornerShape(32.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. SECCIÓN INFERIOR: CARGANDO VS BOTONES DUALES
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = isLoading, label = "welcome_loading_crossfade") { loading ->
                    if (loading) {
                        // Estado de Carga (animación de indicador giratorio)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = LimePrimary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Cargando aplicación...",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MutedText
                            )
                        }
                    } else {
                        // Estado de Botones
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (esUsuarioHabitual) {
                                // VISTA USUARIO HABITUAL
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(9999.dp))
                                        .background(LimePrimary)
                                        .clickable { onContinueAsUserClick(gamertagGuardado) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "CONTINUAR COMO ${gamertagGuardado.uppercase()}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextOnLime,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Text(
                                    text = "O cambiar de perfil",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MutedText,
                                    modifier = Modifier
                                        .clickable { onCreateProfileClick() }
                                        .padding(vertical = 2.dp)
                                )
                            } else {
                                // VISTA USUARIO NUEVO
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(9999.dp))
                                        .background(LimePrimary)
                                        .clickable { onCreateProfileClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "CREAR MI PERFIL",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextOnLime,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Text(
                                    text = "O iniciar sesión",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MutedText,
                                    modifier = Modifier
                                        .clickable { onLoginClick() }
                                        .padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
