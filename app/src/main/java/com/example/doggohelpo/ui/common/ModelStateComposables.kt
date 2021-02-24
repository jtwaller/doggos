package com.example.doggohelpo.ui.common

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.doggohelpo.features.ErrorState

@Composable
fun ErrorModelState(state: ErrorState) = Fullscreen {
    Text(state.err.message ?: "An error has occurred")
}

@Composable
fun LoadingModelState() = Fullscreen {
    CircularProgressIndicator()
}