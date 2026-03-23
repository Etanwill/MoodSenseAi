# MoodSense AI - Android Application

> A complete, production-ready Android app that detects emotions, classifies sounds, tracks mood patterns, and recommends music — all powered by on-device AI.

---

## 📦 Project Structure

```
MoodSenseAI/
├── app/
│   ├── src/main/
│   │   ├── java/com/moodsense/ai/
│   │   │   ├── MoodSenseApp.kt                  # Application class (Hilt)
│   │   │   ├── MainActivity.kt                  # Entry point
│   │   │   ├── ui/
│   │   │   │   ├── theme/                       # Material 3 theme, colors, typography
│   │   │   │   ├── navigation/Navigation.kt     # NavHost + all routes
│   │   │   │   ├── components/Components.kt     # Reusable UI components
│   │   │   │   └── screens/
│   │   │   │       ├── SplashScreen.kt
│   │   │   │       ├── OnboardingScreen.kt
│   │   │   │       ├── HomeScreen.kt            # Dashboard
│   │   │   │       ├── EmotionCameraScreen.kt   # ML Kit face detection
│   │   │   │       ├── SoundMonitorScreen.kt    # TFLite YAMNet
│   │   │   │       ├── JournalScreen.kt         # Mood journal + typing tracker
│   │   │   │       ├── InsightsScreen.kt        # Charts & patterns
│   │   │   │       └── MusicScreen.kt           # Spotify integration
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── MoodSenseDatabase.kt     # Room DB
│   │   │   │   │   ├── entities/Entities.kt     # DB tables
│   │   │   │   │   └── dao/Daos.kt              # DB queries
│   │   │   │   └── repository/
│   │   │   │       ├── Repositories.kt          # Data repositories
│   │   │   │       └── SpotifyApiModels.kt      # Retrofit + Spotify DTOs
│   │   │   ├── domain/model/Models.kt           # Domain models
│   │   │   ├── ml/
│   │   │   │   ├── FaceEmotionAnalyzer.kt       # ML Kit face analysis
│   │   │   │   └── SoundClassifier.kt           # TFLite YAMNet
│   │   │   ├── viewmodel/
│   │   │   │   ├── HomeViewModel.kt
│   │   │   │   └── ViewModels.kt                # All other ViewModels
│   │   │   ├── di/AppModule.kt                  # Hilt DI module
│   │   │   └── util/
│   │   │       ├── TypingTracker.kt             # Keyboard dynamics
│   │   │       └── InsightsAnalyzer.kt          # Pattern detection
│   │   ├── assets/                              # Place yamnet.tflite here
│   │   └── res/
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── gradle.properties
```

---

## 🚀 Setup Instructions

### Step 1: Open in Android Studio

1. Download and extract the ZIP file
2. Open **Android Studio** (Hedgehog or newer recommended)
3. Select **"Open an Existing Project"**
4. Navigate to the `MoodSenseAI/` folder and click **Open**
5. Wait for Gradle sync to complete

---

### Step 2: Add YAMNet TensorFlow Lite Model

The sound classifier uses the YAMNet model. Download it and add it to the project:

1. Download the model:
   ```
   https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/audio_classification/android/yamnet_v0.tflite
   ```

2. Rename the downloaded file to: `yamnet.tflite`

3. Place it in:
   ```
   app/src/main/assets/yamnet.tflite
   ```

4. Create the `assets/` folder if it doesn't exist:
   - Right-click `src/main/` → New → Directory → type `assets`

> **Note:** If you skip this step, the app will still work — it falls back to a mock sound classifier that cycles through categories for testing purposes.

---

### Step 3: Configure Spotify API

#### 3a. Create a Spotify Developer Account

1. Go to [https://developer.spotify.com/dashboard](https://developer.spotify.com/dashboard)
2. Log in or create a free account
3. Click **"Create App"**
4. Fill in:
   - App Name: `MoodSense AI`
   - App Description: `AI-powered mood music recommendation`
   - Redirect URI: `moodsenseai://callback`
5. Accept terms and click **Save**
6. On the app page, click **Settings** to find your **Client ID** and **Client Secret**

#### 3b. Add credentials to the project

Open `app/build.gradle` and replace the placeholder values:

```gradle
buildConfigField "String", "SPOTIFY_CLIENT_ID", '"YOUR_ACTUAL_CLIENT_ID_HERE"'
buildConfigField "String", "SPOTIFY_CLIENT_SECRET", '"YOUR_ACTUAL_CLIENT_SECRET_HERE"'
```

For example:
```gradle
buildConfigField "String", "SPOTIFY_CLIENT_ID", '"abc123def456ghi789"'
buildConfigField "String", "SPOTIFY_CLIENT_SECRET", '"xyz789uvw456rst123"'
```

> ⚠️ **Security Note:** Never commit your real credentials to public repositories. For production apps, use a backend proxy server instead of embedding secrets in the app.

---

### Step 4: Add MPAndroidChart Dependency

The Insights screen uses MPAndroidChart for charts. Add JitPack to your project:

In `settings.gradle`, add to `repositories`:
```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }   // ← Add this
    }
}
```

---

### Step 5: Build and Run

1. Connect an Android device (API 26+) or start an emulator
2. Click the **Run** button (▶) in Android Studio
3. Select your device/emulator
4. Wait for the build to complete (~2-3 minutes first time)

---

## 📱 Features Overview

### 1. Facial Emotion Detection
- Uses **Google ML Kit Face Detection** API
- Analyzes smile probability, eye openness, and facial contours
- Detects: Happy, Sad, Angry, Fear, Surprise, Disgust, Neutral
- Shows real-time confidence percentage
- **100% on-device, no internet required**

### 2. Ambient Sound Classification
- Uses **TensorFlow Lite** with **YAMNet** model
- Classifies: Speech, Music, Traffic, Nature, Silence, Ambient
- Shows live intensity bar
- Falls back to mock classification if model file is absent
- **100% on-device, no internet required**

### 3. Keyboard Dynamics Tracking
- Integrated into the Journal text field
- Tracks: Words per minute, backspace frequency, pause duration
- Auto-saves after each journal session
- Stored in **Room Database**

### 4. Mood Journal
- Rich text entry with mood score slider (1-10)
- 12 mood tag chips (Happy, Anxious, Tired, etc.)
- Full history with swipe-to-delete
- All data stored locally in **Room Database**

### 5. Mood Pattern Detection
- Analyzes emotion frequency patterns
- Detects time-of-day and day-of-week trends
- Identifies typing behavior correlations
- Shows mood trend direction (improving/declining)
- Generates up to 8 personalized insights

### 6. Music Recommendations
- Powered by **Spotify Web API**
- Searches by detected mood automatically
- Shows: Song name, artist, album art
- Tap any track to open in Spotify app
- Manual mood selector to browse by emotion

---

## 🏗️ Architecture

```
UI Layer (Jetpack Compose + ViewModel)
    ↓
Domain Layer (Models + Use Cases)
    ↓
Data Layer (Room DB + Retrofit)
    ↓
ML Layer (ML Kit + TFLite)
```

- **MVVM** architecture pattern
- **Hilt** for dependency injection
- **Kotlin Coroutines + Flow** for reactive data
- **StateFlow** for UI state management
- **Offline-first** design — app works without internet

---

## 🔧 Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room |
| Camera | CameraX |
| Face ML | Google ML Kit |
| Sound ML | TensorFlow Lite (YAMNet) |
| Networking | Retrofit + OkHttp |
| Image Loading | Coil |
| Permissions | Accompanist |

---

## 📋 Required Permissions

| Permission | Purpose |
|-----------|---------|
| `CAMERA` | Facial emotion detection via CameraX |
| `RECORD_AUDIO` | Ambient sound classification |
| `INTERNET` | Spotify API music recommendations |
| `ACCESS_NETWORK_STATE` | Check connectivity before API calls |

---

## 🐛 Troubleshooting

### Build Fails: "Could not resolve dependency"
→ Check that `maven { url 'https://jitpack.io' }` is in `settings.gradle`

### Sound classifier not working
→ Ensure `yamnet.tflite` is in `app/src/main/assets/`
→ Check microphone permission is granted in device settings

### Camera not showing
→ Must run on real device or emulator with camera enabled
→ Grant camera permission when prompted

### Spotify returns empty results
→ Verify Client ID and Secret are correct in `build.gradle`
→ Ensure internet permission is granted
→ Check that your Spotify app is in "Development mode" on the dashboard

### Hilt compilation error
→ Clean and rebuild: Build → Clean Project → Rebuild Project

---

## 🔒 Privacy

All emotion detection and sound classification runs **100% on-device**.  
No audio recordings or images are ever sent to any server.  
The only network calls are to Spotify's API for music search.  
All user data (journal entries, emotions, typing stats) is stored locally in Room DB.

---

## 📄 License

This project is for educational and personal use.  
Spotify integration subject to [Spotify Developer Terms](https://developer.spotify.com/terms/).  
ML Kit subject to [Google ML Kit Terms](https://developers.google.com/ml-kit/terms).

---

*Built with ❤️ using Kotlin, Jetpack Compose, and on-device AI*
