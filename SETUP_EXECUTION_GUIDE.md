# FoodBridge Android - Setup Execution Guide

## ✅ CURRENT STATUS

**Already Installed:**
- ✅ Node.js: v22.20.0
- ✅ npm: 10.9.3
- ✅ Git: 2.50.1.windows.1

**Need to Install:**
- ❌ Android Studio
- ❌ Android SDK
- ❌ Java Development Kit (JDK) - comes with Android Studio

**TODO:**
- [ ] Step 1: Download & Install Android Studio (MANUAL)
- [ ] Step 2: Initialize Git in foodlink folder (TERMINAL)
- [ ] Step 3: Create project structure (TERMINAL)
- [ ] Step 4: Create Android project (MANUAL - Android Studio)
- [ ] Step 5: Setup Firebase (MANUAL - Browser + Android Studio)
- [ ] Step 6: Add dependencies (MANUAL - Android Studio)
- [ ] Step 7: Test on emulator (MANUAL - Android Studio)

---

## 🎯 PHASE 1: DOWNLOAD & INSTALL ANDROID STUDIO (MANUAL)

### What to Do RIGHT NOW:

1. **Open Browser**
   - Go to: https://developer.android.com/studio

2. **Click "Download Android Studio"**
   - File size: ~900 MB
   - Choose your OS: Windows (should auto-select)

3. **Run the installer**
   - Accept default installation path
   - Let it install (may take 10-15 minutes)

4. **After installation:**
   - Android Studio will prompt to download SDK
   - Click "Next" and let it download components
   - This could take 10-20 minutes (depends on internet)

5. **When Android Studio opens:**
   - Accept all license agreements
   - Note: You'll need Android Studio for next steps
   - Keep it open - we'll use it next

### ⏱️ Expected Time: 30-40 minutes

---

## 🎯 PHASE IT: PROJECT SETUP (TERMINAL)

Once Android Studio is downloading/installing, use this terminal to setup Git and project structure.

### Step 1: Initialize Git Repository

```powershell
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

git init
git config user.name "Your Name"
git config user.email "your.email@gmail.com"
git config --list
```

### Step 2: Create Project Folders

```powershell
# Make sure you're in foodlink folder
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

# Create main folders
mkdir android
mkdir docs
mkdir firebase

# Verify
ls
# Should see: android, docs, firebase, and any markdown files
```

### Step 3: Create .gitignore

Create file: `C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\.gitignore`

```
# Android
.gradle/
.idea/
build/
*.apk
*.aar
gradle/
gradlew
gradlew.bat
local.properties
*.iml

# Generated
.DS_Store
*.log
*.swp
*.swo
Thumbs.db

# IDE
.vscode/
*.jks
*.keystore

# Sensitive
google-services.json
```

---

## 🎯 PHASE III: FIREBASE SETUP (BROWSER + CONSOLE)

### Step 1: Create Firebase Project

1. **Go to:** https://console.firebase.google.com/
2. **Click: "Add Project"**
3. **Name:** foodbridge
4. **Uncheck:** "Enable Google Analytics"
5. **Click:** "Create Project" (wait 1-2 min)
6. **Click:** "Continue"

### Step 2: Register Android App in Firebase

In Firebase Console:

```
1. Click "Android" icon (< > symbol)
2. App Name: "FoodBridge"
3. Package name: "com.foodbridge.app"
4. (Skip SHA-1 for now, we'll add it later)
5. Click "Register app"
6. Click "Download google-services.json"
   → SAVE THIS FILE (don't close the page)
```

### Step 3: Enable Firebase Services

**Authentication:**
- Left menu: "Build" > "Authentication"
- Click "Sign-in method"
- Enable: "Email/Password"
- Enable: "Google" (optional)

**Firestore Database:**
- Left menu: "Build" > "Firestore Database"
- Click "Create database"
- Start in "Production mode"
- Choose region closest to you
- Click "Create"

**Cloud Storage:**
- Left menu: "Build" > "Storage"
- Click "Get Started"
- Select "Production mode"
- Same region as Firestore
- Click "Done"

### Step 4: Get Google Maps API Key

1. Go to: https://console.cloud.google.com/
2. Select "foodbridge" project (top-left dropdown)
3. Search for: "Maps SDK for Android"
4. Click it > Click "Enable"
5. Go to "Credentials" (left sidebar)
6. Click "Create Credentials" > "API Key"
7. **COPY THIS KEY** (you'll need it soon)

---

## 🎯 PHASE IV: CREATE ANDROID PROJECT IN Android Studio (MANUAL)

### In Android Studio:

1. **Click: "New Project"**
2. **Select: "Phone and Tablet" > "Empty Activity"**
3. **Configure Project:**
   ```
   Name: FoodBridge
   Package name: com.foodbridge.app
   Save location: C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\android
   Language: Kotlin
   Min SDK: API 26 (Android 8.0)
   Target SDK: API 34+ (Android 14+)
   ```
4. **Click: "Finish"**
5. **Wait for Gradle sync** (5-10 minutes first time)

### Add google-services.json:

1. You downloaded this from Firebase (Step 2 above)
2. Place it in: `android/app/google-services.json`
3. Android Studio should ask to sync
4. Click "Sync Now"

---

## 🎯 PHASE V: ADD FIREBASE & DEPENDENCIES (Android Studio)

### Step 1: Update Project-Level build.gradle

Open: `android/build.gradle.kts` (or `.gradle` - depends on version)

Find the `plugins` section and ensure it has:

```gradle
plugins {
    id 'com.android.application' version '8.0.2' apply false
    id 'com.android.library' version '8.0.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
    id 'com.google.gms.google-services' version '4.3.15' apply false
}
```

### Step 2: Update App-Level build.gradle

Open: `android/app/build.gradle.kts` (or `.gradle`)

At the **TOP**, ensure plugins section includes:
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.gms.google-services'  // ADD THIS LINE
}
```

In the **bottom** of dependencies block, add:

```gradle
// Firebase BOM (manages all versions)
implementation platform('com.google.firebase:firebase-bom:32.1.1')

// Firebase Services
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-firestore-ktx'
implementation 'com.google.firebase:firebase-storage-ktx'
implementation 'com.google.firebase:firebase-messaging-ktx'
implementation 'com.google.firebase:firebase-analytics-ktx'

// Google Maps & Location
implementation 'com.google.android.gms:play-services-maps:18.1.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'

// Jetpack Architecture
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
implementation 'androidx.navigation:navigation-fragment-ktx:2.6.0'
implementation 'androidx.navigation:navigation-ui-ktx:2.6.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1'

// Image Loading
implementation 'io.coil-kt:coil:2.4.0'

// Room (Local Database)
implementation 'androidx.room:room-runtime:2.5.2'
implementation 'androidx.room:room-ktx:2.5.2'
kapt 'androidx.room:room-compiler:2.5.2'

// Retrofit (Optional)
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// JSON
implementation 'com.google.code.gson:gson:2.10.1'

// Testing
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
```

### Step 3: Sync Gradle

When prompted: Click "Sync Now"

Wait for sync to complete. Check Build > Rebuild Project (should have no errors).

---

## 🎯 PHASE VI: ADD GOOGLE MAPS API KEY

Open: `android/app/src/main/AndroidManifest.xml`

Inside the `<application>` tag (after opening tag), add:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
```

Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with the key you got from Step 4 in Phase III.

---

## 🎯 PHASE VII: SETUP FIRESTORE RULES

In **Firebase Console**:

1. Go to **Firestore Database**
2. Click **"Rules"** tab
3. Replace with:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }

    // Food Listings
    match /listings/{listingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && 
                      request.resource.data.restaurantId == request.auth.uid;
      allow update, delete: if request.auth.uid == resource.data.restaurantId;
    }

    // Notifications
    match /notifications/{docId} {
      allow read: if request.auth.uid == resource.data.userId;
      allow create: if request.auth != null;
    }

    // Ratings
    match /ratings/{docId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
  }
}
```

4. Click **"Publish"**

---

## 🎯 PHASE VIII: SETUP CLOUD STORAGE RULES

In **Firebase Console**:

1. Go to **Cloud Storage**
2. Click **"Rules"** tab
3. Replace with:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /food-images/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
                      request.resource.size < 5242880;
    }
  }
}
```

4. Click **"Publish"**

---

## 🎯 PHASE IX: CREATE ANDROID VIRTUAL DEVICE

In **Android Studio**:

1. Right panel: Click **"Device Manager"**
2. Click **"Create device"**
3. Select **"Pixel 4"** or **"Pixel 5"**
4. Click **"Next"**
5. Select **Android API 30+** (or any recent one)
6. Click **"Next"** > **"Finish"**
7. Wait for download to complete

---

## 🎯 PHASE X: TEST RUN

In **Android Studio**:

1. Click **"Run"** button (green play icon)
2. Select your virtual device
3. Click **"OK"**
4. Wait for build and launch (~30 seconds)

### Expected Result:
You should see a default Android screen with "Hello World!" or similar empty activity.

If this appears: ✅ **SETUP SUCCESSFUL!**

---

## ✅ FINAL SETUP VERIFICATION CHECKLIST

- [ ] Android Studio installed
- [ ] Project created (com.foodbridge.app)
- [ ] google-services.json in app/ folder
- [ ] Firebase dependencies added to build.gradle
- [ ] Google Maps API key added to AndroidManifest.xml
- [ ] Firebase Authentication enabled
- [ ] Firestore Database created
- [ ] Cloud Storage created
- [ ] Firestore Rules published
- [ ] Storage Rules published
- [ ] Android Virtual Device created
- [ ] App runs on emulator without errors
- [ ] No build errors or warnings

---

## 📋 TOTAL SETUP ESTIMATES

| Phase | Time | Notes |
|-------|------|-------|
| **I** | 30-40 min | Download + Install Android Studio |
| **II** | 5 min | Terminal: Git & folders |
| **III** | 20 min | Firebase Console setup |
| **IV** | 15 min | Android Studio: Create project |
| **V** | 10 min | Add dependencies |
| **VI** | 2 min | Add Maps API key |
| **VII** | 5 min | Firestore Rules |
| **VIII** | 5 min | Storage Rules |
| **IX** | 15 min | Virtual Device creation |
| **X** | 10 min | Verify & test |
| **TOTAL** | ~2.5 hours | Complete setup |

---

## 🚨 COMMON ISSUES & SOLUTIONS

| Issue | Solution |
|-------|----------|
| Gradle sync fails | File > Invalidate Caches > Restart |
| google-services.json not found | Download from Firebase again, place in android/app/ |
| Build error: Firebase not found | Sync Gradle: Build > Rebuild Project |
| Emulator won't start | Make sure device is created, allocate more RAM |
| Maps API not showing | Verify API key in AndroidManifest.xml |
| Firebase auth not connecting | Check package name matches Firebase project |

---

## 🎯 NEXT PHASE (After Setup Complete)

Once all steps done and app runs on emulator:

1. **Take screenshots:**
   - Android Studio with app running
   - Emulator showing app
   - Firebase Console showing "foodbridge" project

2. **Message completion** with screenshots

3. **We start Phase 1 Development:**
   - Authentication system (Kotlin code)
   - User models (data classes)
   - Firebase integration
   - Navigation structure

---

## 📍 WHERE YOU ARE

```
┌─ Setup Phase (You are here)
│  ├─ Install Android Studio ← Do this now
│  ├─ Setup Firebase ← Do this now
│  ├─ Create Android project ← Do this now
│  ├─ Add dependencies ← Do this now
│  └─ Test app ← Do this now
│
├─ Phase 1: Core Features (Starts next week)
│  ├─ Authentication
│  ├─ User models
│  └─ Firebase integration
│
├─ Phase 2: Restaurant Panel (Week 2)
├─ Phase 3: NGO Panel (Week 3)
├─ Phase 4: Maps & Real-time (Week 4)
├─ Phase 5: Polish & Testing (Week 5)
└─ Phase 6: Deploy to Play Store (Week 6)
```

---

## 🚀 READY?

1. **Download Android Studio NOW:** https://developer.android.com/studio
2. **Follow this guide step by step**
3. **Message when stuck** (screenshot + description)
4. **Message when complete** with proof screenshots

---

*Last Updated: March 2026*
*FoodBridge Android Setup Guide*
