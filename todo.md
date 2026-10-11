# TODO – LehrerDashboard

Stand: 11. Oktober 2026

## P0 – Projekt zuverlässig ausführbar machen

- [x] Gradle Wrapper vervollständigen (`gradlew`, `gradlew.bat` und `gradle/wrapper/gradle-wrapper.jar`) und ins Repository aufnehmen.
- [x] Einen sauberen Debug-Build mit JDK 17 und Android SDK 35 ausführen und dokumentieren.
- [x] Mindestens einen CI-Workflow für Build, Unit-Tests und Lint einrichten.
- [x] Repository-Basis prüfen: vorhandene Dateien initial committen und sicherstellen, dass lokale IDE-/Build-Artefakte ignoriert bleiben.

## P1 – Fachliche Grundlage schaffen

- [ ] Anforderungen und Datenumfang für WebUntis, Microsoft Graph, lokale Memos und Offline-Betrieb konkretisieren.
- [ ] UI, Domänenlogik und Datenzugriff in klar getrennte Pakete bzw. Schichten aufteilen.
- [ ] Dashboard-Modelle aus `DashboardApp.kt` herauslösen; stabile IDs, Zeitstempel, Status und Quelltyp ergänzen.
- [ ] `DashboardViewModel` mit einem expliziten UI-Zustand einführen (Laden, Inhalt, leerer Zustand, Fehler, letzter Sync).
- [ ] Statische `sampleItems` durch ein Repository ersetzen; Beispieldaten nur noch als Fake für Preview und Tests verwenden.
- [ ] Ein einheitliches Sortier- und Zusammenführungsverhalten für Unterricht, Termine, Aufgaben, Nachrichten und Memos definieren.

## P1 – Erste nutzbare Funktionen

- [ ] Memo-Erstellung über den Floating Action Button implementieren, einschließlich Bearbeiten und Löschen.
- [ ] Room-Datenbank für Memos und zwischengespeicherte Dashboard-Einträge integrieren.
- [ ] Manuelle Aktualisierung als echte Aktion mit Ladeanzeige, Fehlerbehandlung und Zeitstempel umsetzen.
- [ ] Detailansicht bzw. passende Aktion beim Antippen eines Dashboard-Eintrags ergänzen.
- [ ] Leere Zustände, Offline-Hinweis und verständliche Fehlermeldungen gestalten.

## P2 – Externe Datenquellen anbinden

- [ ] WebUntis-Anbindung für Stundenplan, Vertretungen und Änderungen implementieren.
- [ ] Microsoft-Graph-Anmeldung über MSAL einrichten; benötigte Berechtigungen möglichst klein halten.
- [ ] Outlook-Kalender und relevante Nachrichten über Microsoft Graph integrieren.
- [ ] Aufgaben und Teams-Inhalte anbinden und in das gemeinsame Dashboard-Modell übersetzen.
- [ ] Zugangsdaten und Tokens sicher speichern; keine Secrets in Quellcode oder Build-Dateien ablegen.
- [ ] Token-Erneuerung, Abmeldung, API-Limits und partielle Synchronisationsfehler behandeln.

## P2 – Synchronisation und Offline-Betrieb

- [ ] WorkManager für periodische Hintergrundaktualisierungen konfigurieren.
- [ ] Cache-Strategie und Konfliktregeln für lokale und externe Daten festlegen.
- [ ] Netzwerkstatus berücksichtigen und den letzten erfolgreichen Sync transparent anzeigen.
- [ ] Verhalten unter Android-Energiesparmodi sowie nach einem Geräteneustart testen.

## P2 – Qualität und Barrierefreiheit

- [ ] Unit-Tests für Mapping, Sortierung, Zusammenführung und ViewModel-Zustände schreiben.
- [ ] Compose-UI-Tests für Dashboard, Aktualisierung, Fehlerzustände und Memo-Ablauf ergänzen.
- [ ] Datumslogik injizierbar machen (`Clock` statt direktem `LocalDate.now()`), damit sie testbar bleibt.
- [ ] Sichtbare Texte und Content Descriptions vollständig in `strings.xml` auslagern.
- [ ] TalkBack, große Schrift, Kontrast, Touch-Ziele sowie Hell-/Dunkelmodus prüfen.
- [ ] Unterschiedliche Bildschirmgrößen, Rotation und Prozesswiederherstellung testen.

## P3 – Veröffentlichung vorbereiten

- [ ] Datenschutzkonzept und Datenschutzerklärung für Schul-, Microsoft- und WebUntis-Daten erstellen.
- [ ] Backup-Regeln für sensible bzw. angemeldete Daten explizit konfigurieren; `allowBackup` überprüfen.
- [ ] Release-Minifizierung und Ressourcen-Shrinking aktivieren und passende ProGuard/R8-Regeln ergänzen.
- [ ] App-Icon, Versionsstrategie, Signierung und Release-Checkliste finalisieren.
- [ ] README um Architektur, Einrichtung externer Dienste, Build-/Testbefehle und bekannten Funktionsumfang erweitern.

## Aktueller Stand

- Vorhanden: Android-App mit Kotlin, Jetpack Compose, Material 3, Tagesübersicht und Hell-/Dunkel-Theme.
- Noch simuliert: Aktualisieren-Schaltfläche, Memo-Schaltfläche und sämtliche Dashboard-Daten.
- Noch nicht vorhanden: Navigation, Persistenz, Netzwerkzugriff, Authentifizierung, Hintergrundarbeit und Tests.
