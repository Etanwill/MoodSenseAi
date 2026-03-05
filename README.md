# MoodSense AI — Mood-Driven Music & Wellness Platform

![Platform](https://img.shields.io/badge/Platform-Android-green) ![Language](https://img.shields.io/badge/Language-Kotlin-purple) ![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-blue) ![AI](https://img.shields.io/badge/AI-TensorFlow%20Lite%20%7C%20GroqAIntegration-orange)

---

## Project Overview

A comprehensive Android application that uses real-time emotion detection to automatically match music to how the user feels, while also enabling song recognition from ambient audio and providing an AI-powered personal wellness therapist that delivers personalised advice based on the user's daily emotional patterns.

---

## 1. Problem Statement

Modern users face several disconnected challenges at the intersection of music and mental wellness:

| Problem | Description |
|--------|-------------|
| **Passive Music Discovery** | Finding music that matches your current mood requires endless manual searching |
| **No Emotional Awareness** | People don't notice recurring emotional patterns until they become serious problems |
| **Disconnected Wellness Tools** | Therapy, journaling, and music exist in separate apps with no shared context |
| **Song Discovery Friction** | Hearing a great song in public with no easy way to identify and save it |
| **Generic Recommendations** | Existing apps recommend music based on history, not how you actually feel right now |

---

## 2. General Objective

Create an integrated mobile platform that provides:
- Automatic mood detection through facial analysis, typing patterns, and sound classification
- Real-time music recommendations matched to the user's detected emotional state
- On-the-spot song recognition from ambient audio
- A personalised AI wellness therapist that analyses the user's daily emotional data
- A mood journal with passive behavioural tracking for long-term self-awareness

---

## 3. Specific Objectives

### For Users (Listener)
- Detect current emotion automatically via front camera
- Receive instant music recommendations matched to that emotion
- Identify any song playing around them in seconds
- Chat with an AI therapist that knows their real emotional history
- Log mood entries enriched by passive typing behaviour analysis
- Track emotional patterns over time through an insights dashboard

### For Users (Wellness)
- Get personalised evening wellness sessions from the AI therapist
- Receive CBT-based coping strategies tied to their specific daily data
- Understand the relationship between their environment, behaviour, and mood
- Build long-term self-awareness through automated emotion tracking

### Platform Goals
- Make mood-matched music discovery effortless and instant
- Bridge the gap between music, emotion, and mental wellness in one app
- Provide AI-powered therapy guidance accessible to anyone, for free
- Run core features entirely on-device to protect user privacy

---

## 4. Project Description

MoodSense AI is a dual-purpose platform combining intelligent music matching with emotional wellness tools. 

### Core Features

#### A. Emotion Detection Engine
- **Facial Analysis:** Real-time emotion detection via front camera using ML Kit and a custom TensorFlow Lite model — detects Happy, Sad, Angry, Fear, Surprise, Disgust, and Neutral
- **Typing Behaviour Analysis:** Tracks words-per-minute, backspace frequency, and pause duration to infer stress and anxiety states passively
- **Sound Classification:** YAMNet-based on-device ambient audio classifier detects speech, music, traffic, and nature sounds

#### B. Mood-Based Music System
- **iTunes Search API Integration:** Serves curated tracks for every emotional state with album art and 30-second previews — completely free, no API key required
- **Emotion-to-Music Mapping:** Each of the 7 detected emotions maps to a specific music search profile (e.g. Angry → intense rock, Fear → calm ambient, Happy → upbeat pop)
- **One-tap Playback:** Opens track previews or full songs in Apple Music / browser directly from the result card

#### C. Song Recognition (Shazam-like)
- **ACRCloud Integration:** Records 8 seconds of ambient audio, sends an audio fingerprint to ACRCloud, and returns the song title, artist, album, genre, release year, and match confidence score
- **Deep Linking:** Identified songs can be opened instantly in YouTube or Spotify with one tap
- **Animated UI:** Pulsing ring animation with 8-second recording progress bar mirrors the Shazam experience

#### D. AI Mood Therapist
- **Google Gemini Integration:** Powered by Gemini 2.0 Flash — free tier, 1,500 requests/day
- **Personalised Context:** Before every session, the therapist reads the user's actual emotion detections, journal entries, and typing stats from the day via the local Room database
- **Evidence-Based Approach:** Applies CBT reframing, mindfulness techniques, and behavioural activation tailored to the user's specific data
- **Live Chat Interface:** Full conversational UI with animated typing indicator, message bubbles, and contextual quick-reply suggestions

#### E. Mood Journal
- **Rich Entry Logging:** Users write notes, select mood tags, assign a mood score (1–10), and attach their current detected emotion
- **Passive Enrichment:** Typing tracker runs in the background during journaling to add behavioural data to each entry
- **History View:** Full scrollable log of past entries with emotion labels and mood scores

#### F. Insights Dashboard
- **Emotion Frequency Charts:** Visualises which emotions dominated across the day, week, or month
- **Pattern Detection:** Identifies recurring mood trends, time-of-day patterns, and behavioural correlations
- **Typing Trend Analysis:** Shows how typing stress patterns evolve over time

---

## 5. Project Justification

### Market Need
- **Mental Health Crisis:** Growing global demand for accessible, affordable wellness tools
- **Music Streaming Saturation:** Users are overwhelmed by choice — contextual, emotion-driven curation is the next frontier
- **On-Device AI Maturity:** TensorFlow Lite now enables real-time ML inference on mid-range Android phones
- **Wellness App Gap:** No existing app combines passive emotion tracking, music matching, song recognition, and AI therapy in one place

### Unique Value Proposition
- First app to connect detected facial emotion directly to a live music recommendation pipeline
- Song recognition and mood-based music exist in the same interface — hear a song, identify it, then get more like it based on how you feel
- AI therapist has genuine context — it reads your actual data, not a generic questionnaire
- All core emotion detection runs on-device — no camera data ever leaves the phone

---

## 6. Project Scope

### Phase 1: Core Platform (MVP)
- User onboarding and permission flow
- Facial emotion detection with TensorFlow Lite
- Mood-based music recommendations via iTunes API
- Basic mood journal with manual entry

### Phase 2: Enhanced Features
- Typing behaviour passive tracker
- Sound classification with YAMNet
- Emotion history database and insights dashboard
- Song recognition with ACRCloud

### Phase 3: AI Integration
- Google Gemini AI Mood Therapist
- Personalised session context from Room database
- CBT-based response system with quick replies
- Evening wellness session flow

### Phase 4: Expansion
- Collaborative mood playlists
- Wearable device integration for heart rate-based mood detection
- Social mood sharing with anonymous community insights
- Spotify/Apple Music OAuth for full playback within the app

---

## 7. Technical Architecture

### Frontend
| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Navigation | Navigation Compose |
| State Management | StateFlow + MVVM |
| Image Loading | Coil |
| Animations | Compose Animation API |

### Backend / Data
| Component | Technology |
|-----------|------------|
| Local Database | Room (SQLite) |
| Dependency Injection | Hilt |
| HTTP Client | OkHttp |
| JSON Parsing | Gson |
| Architecture | MVVM + Repository Pattern |

### AI & ML
| Feature | Technology |
|---------|------------|
| Facial Emotion Detection | TensorFlow Lite + ML Kit |
| Sound Classification | YAMNet (TFLite) |
| AI Therapist | Groq AI Integration |
| Song Recognition | ACRCloud API |

### External APIs
| API | Purpose | Cost |
|-----|---------|------|
| iTunes Search API | Music recommendations | Free — no key needed |
| ACRCloud | Song recognition | Free — 1,000 recognitions/month |
| Groq AI Integration | AI therapist chat | Free — 1,500 requests/day |

### Database Schema Highlights

**Core Models:**
- `EmotionHistoryEntity` — timestamp, emotion label, confidence score
- `MoodJournalEntity` — note, tags, mood score, detected emotion, timestamp
- `TypingStatsEntity` — WPM, backspace rate, pause duration, session length
- `SoundHistoryEntity` — category, intensity, confidence, timestamp

---

## 8. Implementation Methodology

### Phase 1: Analysis & Planning (2 weeks)
- User journey mapping
- Sensor and API feasibility testing
- Database schema design
- UI/UX wireframing in Figma

### Phase 2: Core Development (6 weeks)
- **Weeks 1–2:** Emotion detection pipeline + camera screen + Room database
- **Weeks 3–4:** Music recommendation system + iTunes integration + mood journal
- **Weeks 5–6:** Song recognition + sound classifier + insights dashboard

### Phase 3: AI Integration (2 weeks)
- Gemini API integration and prompt engineering
- Therapist context builder from Room data
- Chat UI with typing animation and suggestions

### Phase 4: Testing & Refinement (2 weeks)
- Unit testing with JUnit
- UI testing with Espresso
- On-device ML performance profiling
- API error handling and offline fallback testing

### Phase 5: Deployment (1 week)
- APK build and signing
- Play Store listing preparation
- Documentation and demo recording

---

## 9. Expected Results

### Quantitative Metrics
| Metric | Target |
|--------|--------|
| Emotion detections per session | 50+ frames analysed |
| Music recommendation accuracy | 85%+ user satisfaction |
| Song recognition match rate | 90%+ for mainstream music |
| AI therapist response time | < 3 seconds |
| Offline capability | 100% of journal and history features |

### Qualitative Outcomes
- Users report greater awareness of their emotional patterns within 2 weeks of use
- Music discovery feels personal and effortless rather than algorithmic
- AI therapist sessions feel contextual and human, not generic
- Students and young professionals adopt it as a daily wellness habit

### Technical Deliverables
- Fully functional Android APK
- On-device TFLite emotion model
- Integrated Room database with full CRUD operations
- RESTful API consumption layer (iTunes, Gemini, ACRCloud)
- Complete MVVM architecture with Hilt dependency injection

---

## 10. Future Evolution

### Short-term (6–12 months)
- Spotify OAuth integration for in-app full playback
- Widget for home screen mood check-in
- Mood-based alarm and notification system
- Export emotion history as PDF wellness report

### Medium-term (1–2 years)
- **Wearables Integration:** Heart rate and sleep data from Fitbit/Galaxy Watch for physiological mood input
- **Collaborative Playlists:** Share mood-matched playlists anonymously with the community
- **Advanced Therapist:** Multi-session memory, goal tracking, and weekly progress summaries

### Long-term (2–3 years)
- **Clinical Integration:** Partner with mental health organisations to offer the app as a supplementary tool
- **AR Mood Visualisation:** Display emotional aura overlays through the camera in real time
- **IoT Integration:** Smart speaker and smart lighting control based on detected mood
- **Research Platform:** Anonymised aggregate mood data for academic mental wellness research

---

## 11. Success Metrics

### User Metrics
- Daily Active Users (DAU)
- Average session duration
- Therapist sessions completed per week
- Songs identified per user per day
- Journal entries per week

### Technical Metrics
| Metric | Target |
|--------|--------|
| Emotion detection latency | < 500ms per frame |
| App cold start time | < 2 seconds |
| API response time | < 3 seconds |
| Offline data availability | 100% |
| Crash rate | < 0.1% |

---

## 12. Risk Management

| Risk | Impact | Mitigation |
|------|--------|------------|
| TFLite model accuracy variance across devices | High | Test on 5+ device types, add confidence threshold fallback |
| Gemini API quota exhaustion | Medium | Cache responses, add offline fallback message |
| ACRCloud monthly limit reached | Low | Show graceful "limit reached" message, suggest upgrade |
| Camera permission denied | High | Fallback to manual mood selection |
| Network unavailability | Medium | Full offline mode for journal and history features |

---

## 13. Team Structure

### Core Team
- **Project Lead / Android Developer:** Architecture, ML integration, AI therapist
- **UI/UX Developer:** Jetpack Compose screens, animations, design system
- **ML Engineer:** TFLite model training, YAMNet integration, on-device inference optimisation

### Advisory Support
- Mental health and wellness consultants
- Music therapy specialists
- Android performance and security reviewers

---

## Conclusion

MoodSense AI addresses a significant gap at the intersection of music technology and mental wellness by making emotional self-awareness effortless and actionable. Rather than asking users to manually log how they feel, the app detects it — through their face, their typing, and their environment — and responds with the right music, the right insights, and the right guidance. With a fully on-device ML pipeline, a free AI therapist powered by Google Gemini, and a Shazam-like song recognition engine, MoodSense AI has the potential to become a daily companion for anyone who believes the music you hear should match the person you are in that moment.
