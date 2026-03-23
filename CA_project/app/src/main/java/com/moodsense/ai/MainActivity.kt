package com.moodsense.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.moodsense.ai.ui.navigation.MoodSenseNavGraph
import com.moodsense.ai.ui.theme.MoodSenseTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the MoodSense AI application.
 * Sets up the Compose UI and navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MoodSenseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodSenseNavGraph()
                }
            }
        }
    }
}
