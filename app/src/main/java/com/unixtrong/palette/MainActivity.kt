package com.unixtrong.palette

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.unixtrong.palette.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            var pmsGranted by remember { mutableStateOf(false) }
            val pmsLauncher = rememberLauncherForActivityResult(RequestPermission()) {
                pmsGranted = it
            }
            LaunchedEffect(Unit) {
                pmsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            MyApplicationTheme {
                if (pmsGranted) {
                    MainScreen()
                }
            }
        }
    }
}