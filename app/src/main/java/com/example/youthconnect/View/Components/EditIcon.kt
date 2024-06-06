package com.example.youthconnect.View.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.youthconnect.View.OverlaysAndMore.ModifyUsers


@Composable

fun EditIcon(currentUserType: String?, user: Any?, navController: NavController){
    var editUser by remember { mutableStateOf(false)  }

        Icon(
            imageVector = Icons.Outlined.Edit ,
            contentDescription = "Edit",
            tint = Color.Black,
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    editUser = true
                }
        )


    if (editUser) {
        user?.let {
            ModifyUsers(onDismiss = { editUser = false },
                it, navController
            )
        }
    }
}

