package com.example.youthconnect.View.Components


import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.youthconnect.Model.Sealed.ItemsBottomBar
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ui.theme.Yellow


@Composable
fun BottomNavigation(
    navController: NavHostController
){
    val menu_items = listOf(
        ItemsBottomBar.NewsBottom,
        ItemsBottomBar.QuizBottom,
        ItemsBottomBar.ChatBottom
    )

    BottomAppBar {
        NavigationBar {
            menu_items.forEach{ item ->
                val selected = currentRoute(navController) == item.ruta
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.ruta) },
                    icon = {
                        Icon( imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (item.title == "News") Red else if (item.title == "Quiz") Green else Yellow // Cambia el color del icono seleccionado
                        )
                    },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }
}


@Composable
fun currentRoute(navController: NavController) : String? =
    navController.currentBackStackEntryAsState().value?.destination?.route