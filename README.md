# ZERNEX Video — Lecteur vidéo 100 % local (APK 32 bits)

**ZERNEX Video** est un lecteur vidéo moderne, entièrement local, écrit en **Kotlin + Jetpack Compose + Media3**.  
Il lit uniquement les fichiers vidéo présents sur ton téléphone (aucune connexion internet, aucune collecte de données).

## Caractéristiques

- Lecture locale (MP4, MKV, WebM, 3GP, etc.)
- Interface Material 3 (thème sombre/clair)
- Grille de miniatures (frame vidéo via Coil)
- Recherche en temps réel
- Favoris persistants (DataStore)
- Lecteur plein écran avec contrôles auto-hide
- Mini-barre de lecture en bas
- Permissions Android 10 → 15
- **APK 32 bits** (`armeabi-v7a` + `x86`)
- GitHub Actions prêt

## Structure

```
ZERNEX-Video/
├── .github/workflows/build-apk.yml
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── java/com/zernex/video/
│       │   ├── MainActivity.kt
│       │   ├── ZernexVideoApp.kt
│       │   ├── data/          ← VideoItem, Repository, Favoris
│       │   ├── service/       ← VideoService (Media3)
│       │   └── ui/            ← Compose + ViewModel
│       └── res/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Obtenir l’APK

### GitHub Actions
1. Push le projet sur GitHub
2. Onglet **Actions** → **Build ZERNEX Video APK (32-bit)**
3. Télécharge l’artefact

### Android Studio
1. Ouvre le dossier dans Android Studio
2. Sync Gradle
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**

## Permissions

| Permission | Pourquoi | Android |
|------------|----------|---------|
| `READ_MEDIA_VIDEO` | Accès aux vidéos | 13+ |
| `READ_EXTERNAL_STORAGE` | Accès aux vidéos | ≤ 12 |
| `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Lecture arrière-plan | 14+ |
| `POST_NOTIFICATIONS` | Notification de contrôle | 13+ |

## Technologies

- Kotlin 1.9
- Jetpack Compose + Material 3
- Media3 (ExoPlayer + MediaSession + PlayerView)
- Coil + VideoFrameDecoder (miniatures)
- DataStore (favoris)
- Coroutines + Flow

---

**ZERNEX Video** — Tes vidéos, localement, simplement.
