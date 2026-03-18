# FoodBridge - Android Implementation Guide

## 📋 Quick Start Overview

This document provides a complete guide for building FoodBridge as an **Android app only** using:
- **Kotlin** for Android
- **Firebase** for backend (no server needed!)
- **Google Maps** for location services

---

## 🎯 Implementation Strategy

### Development Approach
```
Phase 1: Setup & Firebase Configuration
    ↓
Phase 2: Authentication (Firebase Auth)
    ↓
Phase 3: Core App Structure (Navigation, MVVM)
    ↓
Phase 4: Restaurant Panel (Food Listings)
    ↓
Phase 5: NGO Panel (Browse, Filter, Accept)
    ↓
Phase 6: Real-time Features (FCM Notifications)
    ↓
Phase 7: Maps Integration
    ↓
Phase 8: Polish & Deploy to Play Store
```

---

## 🛠️ What YOU Need To Do Manually (Step-by-Step)

### **STEP 1: Install Required Tools on Your Computer**

#### 1.1 Download & Install Android Studio
```
Website: https://developer.android.com/studio
Version: Latest stable (2023.1+)
Size: ~900 MB

After Installation:
1. Open Android Studio
2. Accept licensing agreements
3. Let it download SDK files (may take 5-10 minutes)
4. Create a test project to verify installation
```

#### 1.2 Create Google Account (if you don't have one)
```
Go to: https://accounts.google.com/signup
You need this for:
- Google Cloud Console (Maps API)
- Google Play Developer account
- Firebase project
```

#### 1.3 Create Firebase Project (FREE)
```
Website: https://console.firebase.google.com/

Steps:
1. Click "Add project"
2. Enter name: "foodbridge"
3. Uncheck "Enable Google Analytics" (for simplicity)
4. Click "Create project"
5. Wait for creation (1-2 minutes)
6. Click "Continue"

After creation:
7. See three options: Android, iOS, Web
8. Click Android icon
9. Register app:
   - Package name: "com.foodbridge.app"
   - App nickname: "FoodBridge"
   - sha1: (skip for now, we'll add later)
10. Download google-services.json file
11. Save this file - you'll use it in Android Studio
```

#### 1.4 Create Google Cloud Project (for Maps API)
```
Website: https://console.cloud.google.com/

Steps:
1. Select the project created automatically by Firebase
2. Go to "APIs & Services" > "Credentials"
3. Create API Key
4. Enable "Maps SDK for Android"
5. Copy your API Key
6. Save it - you'll use it in AndroidManifest.xml
```

#### 1.5 Create Google Play Developer Account (for deployment)
```
Website: https://play.google.com/console

Steps:
1. Click "Use existing Google Account"
2. Accept agreements
3. Pay one-time $25 registration fee
4. Wait 24 hours for account activation
5. Create app entry in Play Store (we'll do this at end)
```

#### 1.6 Generate App Signing Key
```
Don't do this now - we'll generate it in Android Studio later
You'll need this to:
- Sign your APK/AAB file
- Deploy to Play Store
```

---

### **STEP 2: Create Project Folder Structure**

Open PowerShell:

```powershell
# Navigate to your workspace
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

# Create folders
mkdir android
mkdir docs
mkdir .github\workflows  # (optional for CI/CD later)

# Verify
ls
```

---

### **STEP 3: Initialize Git Repository**

```powershell
# Make sure you're in foodlink folder
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

# Initialize git
git init

# Configure your git
git config user.name "Your Full Name"
git config user.email "your.email@gmail.com"

# Verify
git config --list
```

---

### **STEP 4: Create .gitignore File**

Create `.gitignore` in foodlink folder:

```
# Android
.gradle
.idea
build/
gradlew
gradlew.bat
local.properties
*.apk
*.aar

# Generated files
.DS_Store
*.swp
*.swo

# Logs
logs/
*.log

# IDE
.vscode/
*.iml

# Sensitive
google-services.json (added to .gitignore after setup)
local.properties
```

---

### **STEP 5: Create Android Project in Android Studio**

Open Android Studio and create new project:

```
1. Click "New Project"
2. Select "Phone and Tablet" > "Empty Activity"
3. Configure project:
   - Name: FoodBridge
   - Package name: com.foodbridge.app
   - Save location: C:\Users\...\foodlink\android
   - Language: Kotlin
   - Min SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)
4. Click "Finish"
5. Wait for Gradle sync (5-10 minutes first time)
```

---

### **STEP 6: Add Firebase to Android Project**

In Android Studio:

```
1. Top menu: Tools > Firebase
2. Click "Firestore Database" in left panel
3. Click "Get Started" button
4. Click "Connect to Firebase"
5. Select your "foodbridge" project
6. Allow Android Studio to:
   - Download google-services.json
   - Add Firebase dependencies to build.gradle
7. Click "Add Cloud Firestore to your app"
   - This adds Firestore dependency
8. Sync Gradle

Result: google-services.json in app/android folder
```

---

### **STEP 7: Setup Firebase Authentication**

In Firebase Console:

```
1. Go to https://console.firebase.google.com/
2. Select "foodbridge" project
3. Left sidebar: Authentication > Sign-in method
4. Enable:
   - Email/Password (required)
   - Google (optional but recommended)
   - Phone number (optional)
5. Copy your "Web API Key" from Project Settings > General
```

---

### **STEP 8: Setup Firestore Database**

In Firebase Console:

```
1. Left sidebar: Build > Firestore Database
2. Click "Create database"
3. Start in "Production mode"
4. Choose region closest to you
5. Click "Create"
6. We'll create collections from Android code later via Firestore Rules
```

---

### **STEP 9: Setup Cloud Storage**

In Firebase Console:

```
1. Left sidebar: Build > Storage
2. Click "Get Started"
3. Start in "Production mode"
4. Choose same region as Firestore
5. Click "Done"
```

---

### **STEP 10: Add Google Maps API Key**

In Firebase Console:

```
1. Settings > Project Settings
2. Go to "Service Accounts" tab
3. Click "Database Secrets"
4. Copy the secret
5. Or go to Google Cloud Console > APIs & Services > Credentials
6. Create Android API Key:
   - Go to Credentials > Create Credentials > API Key
   - Copy the key
```

In Android Studio:

```
1. Open AndroidManifest.xml
2. Inside <application> tag, add:

<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />

3. Replace YOUR_GOOGLE_MAPS_API_KEY with actual key
```

---

### **STEP 11: Install Required Android Dependencies**

In `android/app/build.gradle`, add in dependencies section:

```gradle
dependencies {
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.1.1')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'

    // Maps
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'

    // Jetpack
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.6.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.6.0'

    // Image loading
    implementation 'io.coil-kt:coil:2.4.0'

    // Room for local cache
    implementation 'androidx.room:room-runtime:2.5.2'
    kapt 'androidx.room:room-compiler:2.5.2'
    implementation 'androidx.room:room-ktx:2.5.2'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1'

    // Retrofit (if needed for custom APIs)
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

Also update `android/build.gradle` (project level):

```gradle
plugins {
    id 'com.android.application' version '8.0.2' apply false
    id 'com.android.library' version '8.0.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
    id 'com.google.gms.google-services' version '4.3.15' apply false
}
```

And in `android/app/build.gradle`, add at the bottom:

```gradle
apply plugin: 'com.google.gms.google-services'
```

---

### **STEP 12: Verify Setup**

In Android Studio:

```
1. Click "Sync Now" if you see prompt
2. Build menu > Rebuild Project
3. Should complete without errors
4. Create Android Virtual Device:
   - Device > Virtual Device Manager
   - Create new with API 28+
5. Run app: Run > Run 'app'
6. Should see default empty activity
```

---

### **STEP 13: Create Firebase Firestore Rules**

In Firebase Console > Firestore > Rules:

Replace existing rules with:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }

    // Food listings - anyone can read, only restaurant can write
    match /listings/{listingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && 
                      request.resource.data.restaurantId == request.auth.uid;
      allow update, delete: if request.auth.uid == resource.data.restaurantId;
    }

    // Notifications
    match /notifications/{notificationId} {
      allow read, write: if request.auth.uid == resource.data.userId;
    }

    // Ratings
    match /ratings/{ratingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
  }
}
```

Click "Publish"

---

### **STEP 14: Setup Cloud Storage Rules**

In Firebase Console > Storage > Rules:

Replace with:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /food-images/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

---

## 📊 Firebase Collections Structure

Create these collections in Firestore (you can do this manually via Firebase Console):

### Collection: `users`
```json
{
  "userId": "auto-generated",
  "name": "Restaurant/NGO Name",
  "email": "user@example.com",
  "role": "restaurant OR ngo OR admin",
  "location": {
    "address": "123 Main St",
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "phone": "+1234567890",
  "profileImage": "storage-url",
  "ratings": 4.5,
  "totalRatings": 12,
  "isVerified": false,
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Collection: `listings`
```json
{
  "listingId": "auto-generated",
  "foodName": "Biryani",
  "foodType": "non-veg",
  "quantity": 50,
  "unit": "kg",
  "servesPeople": 150,
  "restaurantId": "restaurant-user-id",
  "description": "Fresh cooked...",
  "imageUrl": "storage-url",
  "pickupWindow": {
    "startTime": "timestamp",
    "endTime": "timestamp"
  },
  "location": {
    "address": "Restaurant Address",
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "expiryTime": "timestamp",
  "status": "available OR accepted OR collected OR expired",
  "acceptedByNGO": "ngo-user-id OR null",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Collection: `notifications`
```json
{
  "notificationId": "auto-generated",
  "userId": "recipient-id",
  "type": "new-listing OR listing-accepted OR listing-collected",
  "title": "Notification Title",
  "message": "Notification message",
  "listingId": "reference-to-listing",
  "isRead": false,
  "createdAt": "timestamp"
}
```

### Collection: `ratings`
```json
{
  "ratingId": "auto-generated",
  "fromUserId": "rater-id",
  "toUserId": "target-id",
  "rating": 5,
  "review": "Great service!",
  "listingId": "food-listing-id",
  "createdAt": "timestamp"
}
```

---

## 🔒 Security Checklist

- [ ] Enable Email/Password authentication
- [ ] Enable Google Sign-in
- [ ] Set Firestore Rules (security rules added)
- [ ] Set Cloud Storage Rules (security rules added)
- [ ] Get Google Maps API Key with Android key restrictions
- [ ] Create SHA-1 fingerprint for app signing
- [ ] Enable Two-Factor Authentication (2FA) on Google account

---

## 📝 Manual Checklist Before Coding Starts

### Downloaded & Installed
- [ ] Android Studio installed and configured
- [ ] JDK 11+ installed (comes with Android Studio)
- [ ] Git installed and configured
- [ ] Google account created

### Firebase Setup Complete
- [ ] Firebase project created ("foodbridge")
- [ ] Firebase Authentication enabled (Email/Password + Google)
- [ ] Firestore Database created
- [ ] Cloud Storage created
- [ ] Firestore Rules published
- [ ] Storage Rules published
- [ ] google-services.json downloaded

### Android Project Setup Complete
- [ ] Android project created in Android Studio
- [ ] google-services.json placed in app/ folder
- [ ] Firebase dependencies added to build.gradle
- [ ] Firebase connected to Android project
- [ ] Google Maps API key created
- [ ] Google Maps API key added to AndroidManifest.xml
- [ ] Android Virtual Device created and tested

### Verification
- [ ] Android Studio can build project without errors
- [ ] Firebase Connection test passes
- [ ] Firebase Console shows connected device
- [ ] Empty app runs on emulator without crashing
- [ ] Can see Firestore rules in Firebase Console

---

## 🚀 What We'll Build Next

### Phase 2 Deliverables (After Setup)
1. **Firebase Authentication Service**
   - Login screen
   - Register screen
   - Google Sign-in
   - Session management

2. **Data Models (Kotlin Classes)**
   - User model
   - FoodListing model
   - Notification model
   - Rating model

3. **Firebase Repository Pattern**
   - UserRepository (authentication + CRUD)
   - ListingRepository (food listings)
   - NotificationRepository
   - RatingRepository

4. **Navigation Structure**
   - Navigation graph
   - Fragment transitions
   - Bottom navigation for roles

---

## 💡 Key Android Concepts You'll Learn

### MVVM Architecture
- **Model:** Firestore data models
- **View:** Fragments and Activities
- **ViewModel:** Manages data for views

### Repository Pattern
- Abstract layer between UI and Firestore
- Makes testing easier
- Centralizes data logic

### Kotlin Coroutines
- Asynchronous operations
- Firestore requires async calls
- Cleaner than callbacks

### LiveData & StateFlow
- Reactive data binding
- UI updates automatically when data changes
- Lifecycle-aware

### Jetpack Navigation
- Fragment-based navigation
- Type-safe argument passing
- Back stack management

---

## 🎓 Learning Resources

### Official Documentation
- Android Developer Docs: https://developer.android.com/
- Firebase Docs: https://firebase.google.com/docs/android/setup
- Kotlin Language: https://kotlinlang.org/docs/
- Google Maps API: https://developers.google.com/maps/documentation/android-sdk

### Recommended YouTube Channels
- Google Developers (official)
- Philipp Lackner (Kotlin/Android)
- Coding in Flow (Firebase)

---

## 🎯 Success Criteria After Setup

✅ Android Studio opens without errors
✅ Empty project builds successfully
✅ Firebase console shows connected app
✅ Firestore accessible from Android code
✅ Cloud Storage accessible from Android code
✅ Google Maps API key configured
✅ Firestore Rules set correctly
✅ Android Emulator runs smoothly

---

## 📞 Common Issues During Setup

| Issue | Solution |
|-------|----------|
| Gradle sync fails | File > Invalidate Caches > Restart |
| google-services.json not found | Download from Firebase Console > Project Settings |
| Firebase not connecting | Check package name matches Firebase project |
| Google Maps API not working | Verify API key in AndroidManifest.xml |
| Emulator crashes | Reduce RAM allocation in emulator settings |
| Gradle takes too long | Increase Gradle daemon heap size in gradle.properties |

---

## ✅ Once Setup is Complete

1. **Take screenshot of:**
   - Android Studio with empty project running
   - Firebase Console showing foodbridge project
   - Android Emulator showing default app

2. **Message completion status** with screenshots

3. **We'll start:**
   - Phase 2: Authentication System
   - Kotlin data classes and models
   - Firebase integration code

---

## 🎉 Next Phase: Development

Once setup is verified:

1. Create Firebase Authentication UI
2. Build data models
3. Create Repository classes
4. Build Restaurant panel
5. Build NGO panel
6. Add real-time features
7. Maps integration
8. Deploy to Play Store

---

*Last Updated: March 2026*
*Project: FoodBridge Android Edition*
