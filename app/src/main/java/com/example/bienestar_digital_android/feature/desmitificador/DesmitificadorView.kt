package com.example.bienestar_digital_android.feature.desmitificador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun DesmitificadorScreen (
    onNavigateBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F14))
            .padding(16.dp),
        contentAlignment = Alignment.Center

    ) {
        Text(
            text = "Mito o Verdad (Swipe & Truth",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFCCFF00)
        )
    }
}