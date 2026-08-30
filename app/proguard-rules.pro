# Proguard rules for Media3 ExoPlayer and Jetpack Compose
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**
-keepattributes *Annotation*,InnerClasses,Signature
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
