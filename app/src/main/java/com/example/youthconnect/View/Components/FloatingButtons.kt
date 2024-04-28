package com.example.youthconnect.View.Components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.youthconnect.ui.theme.Purple40
import com.example.youthconnect.ui.theme.Purple80
import com.example.youthconnect.ui.theme.Violet

@Composable
fun FloatingButton(icon : ImageVector, onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        containerColor = Violet
    ) {
        Icon(icon, "Small floating action button.")
    }
}


@Composable
fun ExtendedFloatingButton(icon : ImageVector, text : String, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(icon, "Extended floating action button.") },
        text = { Text(text = text) },
        containerColor = Violet)
}