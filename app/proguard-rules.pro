# ========================
# 🛠️ Hilt / Dagger
# ========================
-keep class dagger.hilt.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# ========================
# 🌐 Retrofit / OkHttp
# ========================
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# ========================
# 🧩 Kotlin Serialization
# ========================
-keep class kotlinx.serialization.** { *; }
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class **$$serializer { *; }
-dontwarn kotlinx.serialization.**

# ========================
# 📱 Jetpack Compose
# ========================
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.compose.**
-dontwarn androidx.lifecycle.**

# ========================
# 💾 DataStore
# ========================
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# ========================
# 📅 Java Time / ThreeTenABP
# ========================
-dontwarn org.threeten.bp.**
-keep class org.threeten.bp.** { *; }

# ========================
# ✨ Debugging (opcjonalne)
# ========================
-keepattributes SourceFile,LineNumberTable