package com.example.youthconnect.View.BottomNavigationScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youthconnect.ViewModel.ChildViewModel
import com.example.youthconnect.ViewModel.NewsViewModel

@Composable
fun ChildProfileScreen(childId : String,
                       viewModel: ChildViewModel = viewModel(),
                       modifier : Modifier = Modifier.background(color = Color.White)
){


    val childViewModel: ChildViewModel = viewModel()


    val childState by childViewModel.childState.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        childViewModel.getCurrentUserById(childId)
    }
    if (childState.isNotEmpty()) {
        val child = childState.first()

        Text(child.FullName)
        Text(child.Course)
        Text(child.ID)
    }




}