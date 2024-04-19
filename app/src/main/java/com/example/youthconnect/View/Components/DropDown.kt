package com.example.youthconnect.View.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.PopupProperties
import com.example.youthconnect.Model.Object.Instructor

@Composable
fun CustomDropdownMenu(
    list: List<Instructor?>,
    defaultSelected: Instructor?,
    color: Color,
    modifier: Modifier = Modifier,
    onSelected: (Instructor?) -> Unit,
) {
    var expand by remember { mutableStateOf(false) }
    var stroke by remember { mutableStateOf(1) }
    var selectedOption by remember(defaultSelected) { mutableStateOf(defaultSelected) }

    Box(
        modifier = modifier
            .clickable {
                expand = !expand
                stroke = if (expand) 2 else 1
            },
        contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier.fillMaxWidth()){
            Text(
                text = selectedOption?.fullName ?: "No hay animador asignado a este niño",
                color = color,
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand",
                tint = Color.DarkGray,
                modifier = Modifier
                    .clickable {
                        expand = !expand
                        stroke = if (expand) 2 else 1
                    }
            )
        }

        DropdownMenu(
            expanded = expand,
            onDismissRequest = {
                expand = false
                stroke = 1
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            modifier = Modifier
                .background(Color.White)
        ) {

            list.forEachIndexed { _, item ->
                if (item != null) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.fullName,
                                color = color,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            selectedOption = item
                            onSelected(selectedOption)
                            expand = false
                            stroke = 1
                        }
                    )
                }
            }
        }
    }
}