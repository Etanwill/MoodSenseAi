package com.moodsense.ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moodsense.ai.ui.screens.*
import com.moodsense.ai.domain.model.Emotion

/**
 * Routes for the application
 */
object Screen {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val EmotionCamera = "emotion_camera"
    const val SoundMonitor = "sound_monitor"
    const val Journal = "journal"
    const val Music = "music"
    const val Insights = "insights"
    const val Therapist = "therapist"
}

/**
 * Main navigation graph for the MoodSense AI app.
 */
@Composable
fun MoodSenseNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding) {
            OnboardingScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home) {
            HomeScreen(
                onNavigateToCamera = { navController.navigate(Screen.EmotionCamera) },
                onNavigateToSound = { navController.navigate(Screen.SoundMonitor) },
                onNavigateToJournal = { navController.navigate(Screen.Journal) },
                onNavigateToInsights = { navController.navigate(Screen.Insights) },
                onNavigateToTherapist = { navController.navigate(Screen.Therapist) },
                onNavigateToMusic = { emotion -> 
                    navController.navigate("${Screen.Music}/${emotion.name}") 
                }
            )
        }

        composable(Screen.EmotionCamera) {
            EmotionCameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMusic = { emotion ->
                    navController.navigate("${Screen.Music}/${emotion.name}")
                }
            )
        }

        composable(Screen.SoundMonitor) {
            SoundMonitorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Journal) {
            JournalScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("${Screen.Music}/{emotionName}") { backStackEntry ->
            val emotionName = backStackEntry.arguments?.getString("emotionName")
            val emotion = try {
                Emotion.valueOf(emotionName ?: Emotion.NEUTRAL.name)
            } catch (e: Exception) {
                Emotion.NEUTRAL
            }
            
            MusicScreen(
                initialEmotion = emotion,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Insights) {
            InsightsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Therapist) {
            TherapistScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
