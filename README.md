# Ki Clash — Android 2D Fighting Game

A small, original anime-inspired 2D fighting game prototype for Android. It features four selectable fighters (Goku, Vegeta, Broly and Frieza), story mode, tournament mode, health/Ki bars, punch, Ki blast, guard and power-up controls.

## Build the APK

1. Install **Android Studio** (current stable) and the Android SDK Platform 35.
2. Open this folder in Android Studio.
3. Allow Gradle sync to finish.
4. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Find the APK at `app/build/outputs/apk/debug/app-debug.apk`.

Alternatively, from a terminal with Gradle installed:
```bash
gradle assembleDebug
```

Requirements: JDK 17+, Android Gradle Plugin 8.7.3 (Android Studio/Gradle downloads it), Android SDK 35.

## Controls

- Menu: tap a fighter, then tap the left half of the lower screen for Story or right half for Tournament.
- ◀ / ▶: move
- PUNCH: basic attack and build Ki
- KI BLAST: spend Ki for a stronger attack
- GUARD: recover a little health
- POWER: charge up

## Notes

This is a playable starter project using simple Canvas-drawn placeholder art. It does not include official Dragon Ball artwork, music, voices, or other licensed assets. Replace/add assets only when you have rights to use them. The included characters and names are fan-game references; this is not an official Dragon Ball product.


## Build without installing Android Studio (GitHub Actions)

This project includes `.github/workflows/build-apk.yml` for cloud builds.

1. Create a GitHub account if you do not already have one.
2. Create a new **public or private repository** on GitHub.
3. Upload the contents of this ZIP (including the hidden `.github` folder) to the repository.
4. Open the repository's **Actions** tab and run **Build Android APK** (or wait for the first push build).
5. Open the completed workflow run, scroll to **Artifacts**, and download `Ki-Clash-debug-apk`.
6. Extract the artifact ZIP; it contains `app-debug.apk`. Transfer it to your Android phone and install it. You may need to allow installation from that source.

The APK produced this way is a debug build for testing, not a Play Store release.
