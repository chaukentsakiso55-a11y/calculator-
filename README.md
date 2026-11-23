# CALCULATOR

An Android calculator that looks serious and behaves like it has had enough.

## Features

- Native Android app written in Kotlin
- Calculator-style keypad and expression display
- Normal, Savage, Unhinged and Veterinarian personality modes
- Random sarcastic responses
- Detects long expressions and division-by-zero attempts
- Never displays real calculation answers
- No accounts, analytics, ads or payment processing

The R2,000 payment lines are jokes. The app never collects money or payment information.

## Build

Requires JDK 17, Android SDK 35 and Gradle 8.9.

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

GitHub Actions builds a debug APK on pushes to `main` and uploads it as an artifact.

## Package

`com.cyberpulse.calculator`
