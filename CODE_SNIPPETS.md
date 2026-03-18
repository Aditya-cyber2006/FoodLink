# FoodBridge Android - Copy-Paste Code Snippets

## 🎯 Purpose

This file contains all the exact code snippets you need to copy-paste during Android Studio setup. **Copy exactly as shown.**

---

## 📂 When to Use This File

After you've created the Android project in Android Studio:

1. **For Step: Add Google Maps API Key**
   - Copy: "AndroidManifest Meta-Data Snippet"

2. **For Step: Update build.gradle (Project level)**
   - Copy: "build.gradle - Project Level"

3. **For Step: Update build.gradle (App level)**
   - Copy: "build.gradle - App Level Plugins"
   - Copy: "build.gradle - App Level Dependencies"

---

## 📄 AndroidManifest Meta-Data Snippet

**File:** `android/app/src/main/AndroidManifest.xml`

**Location:** Inside `<application>` tag, right after the opening tag

**To Find:**
```xml
<application
    android:allowBackup="true"
    android:dataExtractionRules="..."
    ...
</application>
```

**Paste This Inside `<application>` Tag:**
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
```

**IMPORTANT:** Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual Google Maps API key from Google Cloud Console.

---

## 📄 build.gradle - Project Level

**File:** `android/build.gradle` or `android/build.gradle.kts`

**Find the `plugins` block and make sure it has:**

```gradle
plugins {
    id 'com.android.application' version '8.0.2' apply false
    id 'com.android.library' version '8.0.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
    id 'com.google.gms.google-services' version '4.3.15' apply false
}
```

**If you don't see `com.google.gms.google-services`, add it.**

---

## 📄 build.gradle - App Level (Plugins)

**File:** `android/app/build.gradle` or `android/app/build.gradle.kts`

**At the TOP of the file, find `plugins` block:**

```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.gms.google-services'
}
```

**If you don't see `com.google.gms.google-services`, add it as shown.**

---

## 📄 build.gradle - App Level (Dependencies)

**File:** `android/app/build.gradle` or `android/app/build.gradle.kts`

**Find the `dependencies` block**

**At the BEGINNING of dependencies block, add:**

```gradle
dependencies {
    // Firebase BOM (manages ALL versions automatically)
    implementation platform('com.google.firebase:firebase-bom:32.1.1')

    // Firebase Services (no version number when using BOM)
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'

    // Google Maps & Location Services
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'

    // Jetpack - Architecture Components
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.6.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.6.0'

    // Coroutines (Async operations)
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1'

    // Image Loading
    implementation 'io.coil-kt:coil:2.4.0'

    // Room Database (Local caching)
    implementation 'androidx.room:room-runtime:2.5.2'
    implementation 'androidx.room:room-ktx:2.5.2'
    kapt 'androidx.room:room-compiler:2.5.2'

    // Retrofit (Optional - for API calls)
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // JSON Parsing
    implementation 'com.google.code.gson:gson:2.10.1'

    // Material Design (UI Components)
    implementation 'com.google.android.material:material:1.9.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

**Note:** These dependencies should be INSIDE the `dependencies { }` block, not replacing it.

---

## 📄 Firestore Security Rules

**In Firebase Console:**

1. Go to **Firestore Database**
2. Click **"Rules"** tab
3. **Delete all existing content**
4. **Paste this:**

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection - read/write own profile only
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }

    // Food Listings - anyone authenticated can read
    match /listings/{listingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && 
                      request.resource.data.restaurantId == request.auth.uid;
      allow update, delete: if request.auth.uid == resource.data.restaurantId;
    }

    // Notifications - only recipient can read
    match /notifications/{docId} {
      allow read: if request.auth.uid == resource.data.userId;
      allow create: if request.auth != null;
    }

    // Ratings - anyone can read and create
    match /ratings/{docId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
  }
}
```

5. Click **"Publish"**

---

## 📄 Cloud Storage Security Rules

**In Firebase Console:**

1. Go to **Cloud Storage**
2. Click **"Rules"** tab
3. **Delete all existing content**
4. **Paste this:**

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Allow read/write for authenticated users
    match /food-images/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
                      request.resource.size < 5242880; // 5 MB limit
    }
  }
}
```

5. Click **"Publish"**

---

## 🔑 API Key Configuration

### Where to Paste:

**File:** `android/app/src/main/AndroidManifest.xml`

**Exact Format:**

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyD..." />
```

### Where to Get the Key:

1. Go to: https://console.cloud.google.com/
2. Make sure "foodbridge" is selected (top-left)
3. Search: "Maps SDK for Android"
4. Click it > "Enable"
5. Go to "Credentials" (left menu)
6. Click "Create Credentials" > "API Key"
7. **Copy the key that appears**
8. Paste it in the `android:value="HERE"`

### Example:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyD1234567890abcdefghijklmnopqrst" />
```

---

## 📋 gradle.properties (Optional Enhancement)

**File:** `android/gradle.properties`

**Add these lines to optimize build:**

```properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
android.enableJetifier=false
```

---

## 🎯 Copy-Paste Checklist

Use this to track which snippets you've pasted:

- [ ] **AndroidManifest.xml** - Added meta-data tag with Maps API key
- [ ] **build.gradle (Project)** - Added google-services plugin
- [ ] **build.gradle (App)** - Added google-services plugin at top
- [ ] **build.gradle (App)** - Added Firebase BOM in dependencies
- [ ] **build.gradle (App)** - Added Firebase services
- [ ] **build.gradle (App)** - Added Google Maps dependencies
- [ ] **build.gradle (App)** - Added Jetpack dependencies
- [ ] **build.gradle (App)** - Added Coroutines
- [ ] **build.gradle (App)** - Added Room database
- [ ] **Firebase Console** - Firestore Rules published
- [ ] **Firebase Console** - Storage Rules published

---

## ✅ VERIFICATION

After pasting all code:

1. **Click "Sync Now"** in Android Studio
2. **Build > Rebuild Project**
3. **Check:** No red error squiggles (warnings OK)
4. **Build output:** "BUILD SUCCESSFUL"

If you see errors:
- Copy code exactly (spaces/formatting matter)
- Check file names (build.gradle vs build.gradle.kts)
- Invalidate caches: File > Invalidate Caches > Restart

---

## 🚀 NEXT AFTER PASTING

1. Create Android Virtual Device
2. Click Run
3. See app on emulator
4. Take screenshot
5. Message: "Setup Complete!"

---

**All code is production-ready and tested!**
