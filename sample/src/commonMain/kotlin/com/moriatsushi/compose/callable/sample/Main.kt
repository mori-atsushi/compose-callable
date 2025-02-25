package com.moriatsushi.compose.callable.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Main(modifier: Modifier = Modifier) {
    MaterialTheme {
        SampleScreen(modifier = modifier)
    }
}
