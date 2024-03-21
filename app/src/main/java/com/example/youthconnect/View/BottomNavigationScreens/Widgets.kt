package com.example.youthconnect.View.BottomNavigationScreens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youthconnect.ui.theme.Blue
import com.example.youthconnect.ui.theme.Red


import androidx.compose.material.Card
import androidx.compose.ui.Alignment.Companion.BottomCenter
import com.example.youthconnect.ui.theme.Blue50
import com.example.youthconnect.ui.theme.Red50


@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean) {
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isCurrentUser) Red50 else Blue50 ,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = message,
            textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}