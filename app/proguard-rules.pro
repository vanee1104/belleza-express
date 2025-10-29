# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ====================================================
# CONFIGURACIONES GENERALES
# ====================================================

# Preservar información de debugging para stack traces
-keepattributes SourceFile,LineNumberTable

# Si preservas la información de líneas, oculta el nombre original del archivo
-renamesourcefileattribute SourceFile

# Preservar anotaciones importantes
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# ====================================================
# GLIDE - IMAGE LOADING LIBRARY
# ====================================================

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Preservar clases de Glide
-keep class com.bumptech.glide.** { *; }
-keep enum com.bumptech.glide.** { *; }

# Métodos nativos de Glide
-keep class com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher {
    <init>(...);
}

# ====================================================
# OKHTTP & RETROFIT - NETWORKING
# ====================================================

# OkHttp
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keepclasseswithmembers class * {
    @com.squareup.okhttp3.* <methods>;
}

-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ====================================================
# KOTLIN SPECIFIC RULES
# ====================================================

# Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }

# Kotlin reflection
-keep class kotlin.reflect.** { *; }

# Kotlin metadata
-keepattributes RuntimeVisibleAnnotations

# ====================================================
# ANDROIDX & MATERIAL DESIGN
# ====================================================

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Material Components
-keep class com.google.android.material.** { *; }

# ====================================================
# JSON & SERIALIZATION
# ====================================================

# GSON
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Model classes used in JSON serialization
-keepclassmembers class com.example.myapplicationarturocashfaster.** {
    public <methods>;
    public <fields>;
}

# ====================================================
# SUPABASE MANAGER & CUSTOM CLASSES
# ====================================================

# Preservar nuestras clases principales
-keep class com.example.myapplicationarturocashfaster.SupabaseManager { *; }
-keep class com.example.myapplicationarturocashfaster.Service { *; }
-keep class com.example.myapplicationarturocashfaster.**Activity { *; }
-keep class com.example.myapplicationarturocashfaster.**Fragment { *; }

# Preservar métodos nativos
-keepclasseswithmembernames class * {
    native <methods>;
}

# ====================================================
# PARCELABLE & SERIALIZABLE
# ====================================================

# Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Serializable classes
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ====================================================
# SUPRESIÓN DE WARNINGS
# ====================================================

# OkHttp warnings
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Kotlin warnings
-dontwarn org.jetbrains.annotations.**

# AndroidX warnings
-dontwarn androidx.**

# Material Design warnings
-dontwarn com.google.android.material.**

# ====================================================
# WEBVIEW & JAVASCRIPT (OPCIONAL)
# ====================================================

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ====================================================
# LOTTIE ANIMATIONS (SI USAS LA DEPENDENCIA)
# ====================================================

-keep class com.airbnb.lottie.** { *; }

# ====================================================
# REFLECTION & RUNTIME
# ====================================================

# Para clases que usan reflexión
-keepclassmembers class ** {
    @org.jetbrains.annotations.NotNull *;
    @org.jetbrains.annotations.Nullable *;
}

# ====================================================
# CUSTOM RULES FOR YOUR APP
# ====================================================

# Agrega aquí reglas específicas para tu aplicación
# -keep class com.tupaqueda.company.** { *; }

# Preservar recursos de strings (opcional)
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}