package com.unixtrong.palette

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.unixtrong.palette.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var pmsGranted by remember { mutableStateOf(false) }
            val pmsLauncher = rememberLauncherForActivityResult(RequestPermission()) {
                pmsGranted = it
            }
            LaunchedEffect(Unit) {
                pmsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (pmsGranted) {
                        MainScreen()
                    }
                }
            }
        }
    }
}