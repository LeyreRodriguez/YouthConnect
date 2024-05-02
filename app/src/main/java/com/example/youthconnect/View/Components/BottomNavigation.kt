package com.example.youthconnect.View.Components


import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.youthconnect.Model.Sealed.ItemsBottomBar
import com.example.youthconnect.ui.theme.Blue
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ui.theme.Yellow


@Composable
fun BottomNavigation(
    navController: NavHostController
){
    val menuItems = listOf(
        ItemsBottomBar.NewsBottom,
        ItemsBottomBar.QuizBottom,
        ItemsBottomBar.ChatBottom,
        ItemsBottomBar.ProfileBottom
    )

    BottomAppBar(containerColor  = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.contentColorFor(Color.Transparent)) {
        NavigationBar(
            containerColor  = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.contentColorFor(Color.Transparent)
        ) {
            menuItems.forEach{ item ->
                val selected = currentRoute(navController) == item.ruta
                NavigationBarItem(
                    selected = selected,
                    colors = NavigationBarItemDefaults.colors(Color.Transparent) ,
                    onClick = { navController.navigate(item.ruta) },
                    icon = {
                        Icon( imageVector = item.icon,
                            contentDescription = item.title,
                            tint = when (item.title) {
                                "Noticias" -> Red
                                "Quiz" -> Green
                                "Chat" -> Yellow
                                else -> Blue
                            }
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