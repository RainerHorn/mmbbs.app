# Fachliche Anforderungen

## Ziel

Das LehrerDashboard bündelt die für einen Schultag relevanten Informationen in einer chronologisch sortierten Android-Ansicht. Die App bleibt bei fehlender Verbindung mit zuletzt synchronisierten Daten und lokalen Memos nutzbar.

## Datenquellen und Umfang

| Quelle | Inhalte | Zugriff | Offline-Verhalten |
| --- | --- | --- | --- |
| WebUntis | Unterricht, Räume, Vertretungen und Änderungen | Nur lesend | Letzten erfolgreichen Tagesstand anzeigen |
| Microsoft Graph | Kalendertermine, relevante Nachrichten, Aufgaben und Teams-Inhalte | Nur lesend, Anmeldung per MSAL | Zuletzt synchronisierte Einträge anzeigen |
| Lokale Memos | Titel, Notiz, optionaler Zeitpunkt und Status | Lesen und Schreiben | Vollständig offline verfügbar |

## Gemeinsames Dashboard-Modell

Jeder Eintrag besitzt eine stabile ID, Quelle, Kategorie, Status, Titel, Beschreibung und optional einen Zeitpunkt. Einträge mit Zeitpunkt werden aufsteigend sortiert; Einträge ohne Zeitpunkt folgen in einer stabilen fachlichen Reihenfolge. Bei gleichen Werten entscheidet die stabile ID.

## Synchronisation und Fehler

- Quellen werden unabhängig voneinander synchronisiert, damit ein Teilausfall nicht das gesamte Dashboard blockiert.
- Der letzte erfolgreiche Synchronisationszeitpunkt wird sichtbar angezeigt.
- Beim ersten Start ohne Daten erscheint ein leerer Zustand; bei einem Ladefehler ohne Cache ein Fehlerzustand.
- Zugangsdaten und Tokens gehören in sicheren Android-Speicher und niemals ins Repository.

## Nicht Bestandteil der ersten Ausbaustufe

- Bearbeitung externer Kalender-, Aufgaben- oder Stundenplandaten
- Versand oder Beantwortung von Nachrichten
- Mehrbenutzer- oder Administrationsfunktionen
