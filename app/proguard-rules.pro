# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep JAudioTagger classes
-keep class org.jaudiotagger.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class com.google.gson.** { *; }
