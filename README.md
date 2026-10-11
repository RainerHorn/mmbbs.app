# LehrerDashboard

Eine persönliche Android-App, die Unterricht, Termine, Aufgaben, Nachrichten und Memos in einer Tagesansicht bündelt.

## Stand

Das Projekt enthält ein erstes Jetpack-Compose-Dashboard mit Beispieldaten. Die externen Datenquellen werden in den nächsten Schritten ergänzt:

- WebUntis für Stundenplan und Änderungen
- Microsoft Graph für Outlook, Kalender, Aufgaben und Teams
- lokale Memos und Offline-Daten mit Room
- Hintergrundaktualisierung mit WorkManager

## Entwicklung

- Android Studio mit JDK 17
- Android SDK 35
- Kotlin und Jetpack Compose
- Mindestversion: Android 8.0 (API 26)

Nach dem Öffnen in Android Studio die Gradle-Synchronisierung ausführen und anschließend die `app`-Konfiguration starten.

Alternativ lässt sich das Projekt mit dem Gradle Wrapper prüfen:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug testDebugUnitTest lintDebug
```

Das Debug-APK wird unter `app/build/outputs/apk/debug/app-debug.apk` erzeugt. GitHub Actions führt denselben Build sowie Unit-Tests und Lint bei Pushes und Pull Requests auf `main` aus.
