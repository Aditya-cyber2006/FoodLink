# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom application classes
-keep class com.foodbridge.app.** { *; }

# Keep Retrofit interfaces
-keep interface retrofit2.** { *; }
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.<methods> <methods>;
}

# Keep Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# Keep Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep viewmodels and livedata
-keep class androidx.lifecycle.** { *; }
