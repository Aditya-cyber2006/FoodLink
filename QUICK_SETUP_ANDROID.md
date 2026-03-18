# FoodBridge - Android Quick Setup (TL;DR)

## 🚀 What You Need to Do RIGHT NOW (Android + Firebase Only)

**Duration:** ~2-3 hours total

---

## ✅ STEP-BY-STEP CHECKLIST

### **STEP 1: Download & Install Android Studio (30 mins)**

1. Download: https://developer.android.com/studio
2. Run installer
3. Follow default installation
4. After install, open Android Studio
5. Let it download SDK components (may take 5-10 minutes)

**Verify:**
- Android Studio opens without errors
- SDK Manager shows tools downloaded

---

### **STEP 2: Create Google Account & Firebase Project (20 mins)**

1. Create Google account (if needed): https://accounts.google.com/signup
2. Go to Firebase: https://console.firebase.google.com/
3. Click **"Add project"**
4. Name: `foodbridge`
5. Uncheck "Google Analytics"
6. Click **"Create project"** (wait 1-2 mins)
7. Click **"Continue"**

**Save these for later:**
- Firebase Project ID
- Project URL

---

### **STEP 3: Setup Firebase Services (15 mins)**

#### Register Android App with Firebase

In Firebase Console:

```
1. Click Android icon (< > icon)
2. App name: FoodBridge
3. Package name: com.foodbridge.app
4. Skip SHA-1 for now
5. Click "Register app"
6. Click "Download google-services.json"
7. Save file (don't close page yet)
```

#### Enable Firebase Services

1. **Left sidebar > Build > Authentication**
   - Click "Sign-in method"
   - Enable "Email/Password"
   - Enable "Google" (optional)

2. **Left sidebar > Build > Firestore Database**
   - Click "Create database"
   - Production mode
   - Choose nearest region
   - Click "Create"

3. **Left sidebar > Build > Storage**
   - Click "Get started"
   - Production mode
   - Same region as Firestore
   - Click "Done"

4. **Left sidebar > Build > Messaging**
   - Just note it's available
   - We'll configure later

---

### **STEP 4: Get Google Maps API Key (10 mins)**

1. Go to: https://console.cloud.google.com/
2. Select your "foodbridge" project (dropdown top-left)
3. Search "Maps SDK for Android"
4. Click it and click "Enable"
5. Go to **Credentials** (left sidebar)
6. Click **"Create Credentials"** > **"API Key"**
7. Copy your API Key
8. **Save this - you'll need it soon!**

---

### **STEP 5: Create Android Project (30 mins)**

1. Open Android Studio
2. Click **"New Project"**
3. Select **"Phone & Tablet"** > **"Empty Activity"**
4. Configure:
   - **Name:** FoodBridge
   - **Package name:** com.foodbridge.app
   - **Save location:** C:\Users\...\foodlink\android
   - **Language:** Kotlin
   - **Min SDK:** API 26 (Android 8.0)
5. Click **"Finish"**
6. Wait for Gradle sync (5-10 mins first time)

**After Project Opens:**

1. Paste `google-services.json` from Step 3 into `android/app/` folder
2. In Android Studio, sync project: **File > Sync Now**

---

### **STEP 6: Add Firebase Dependencies (5 mins)**

Open `android/app/build.gradle`:

Find the `plugins` section and add:
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.gms.google-services'  // Add this line
}
```

Find the `dependencies` section and add at the **end** of dependencies block:

```gradle
// Firebase BOM (manages versions)
implementation platform('com.google.firebase:firebase-bom:32.1.1')

// Firebase Services (no version needed when using BOM)
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-firestore-ktx'
implementation 'com.google.firebase:firebase-storage-ktx'
implementation 'com.google.firebase:firebase-messaging-ktx'

// Maps
implementation 'com.google.android.gms:play-services-maps:18.1.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'

// Jetpack (MVVM)
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
```

Click **"Sync Now"** when prompted.

---

### **STEP 7: Add Google Maps API Key (5 mins)**

Open `android/app/src/main/AndroidManifest.xml`:

Inside `<application>` tag, add:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY_HERE" />
```

Replace `YOUR_API_KEY_HERE` with the API key from Step 4.

---

### **STEP 8: Create Android Virtual Device (15 mins)**

1. In Android Studio, click **Device Manager** (right panel)
2. Click **"Create device"**
3. Select **Pixel 4** or **Pixel 5**
4. Click **"Next"**
5. Select **API 28+** (choose any recent one)
6. Click **"Next"** > **"Finish"**
7. Wait for it to download

---

### **STEP 9: Test Project (10 mins)**

1. In Android Studio, click **"Run"** (top toolbar)
2. Select your virtual device
3. Click **"OK"**
4. Wait for app to build and run
5. You should see default empty screen

**If it runs successfully, Setup is COMPLETE!** ✅

---

### **STEP 10: Setup Firestore Rules (5 mins)**

In Firebase Console:

1. Go to **Firestore Database**
2. Click **"Rules"** tab
3. Replace all with:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users - read/write own only
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }
    
    // Listings - anyone read, only creator can edit
    match /listings/{listingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.restaurantId;
    }
    
    // Notifications - only recipient can read
    match /notifications/{docId} {
      allow read: if request.auth.uid == resource.data.userId;
      allow create: if request.auth != null;
    }
    
    // Ratings - anyone can read, only creator can edit
    match /ratings/{docId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
  }
}
```

4. Click **"Publish"**

---

### **STEP 11: Setup Storage Rules (2 mins)**

In Firebase Console:

1. Go to **Cloud Storage**
2. Click **"Rules"** tab
3. Replace all with:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /food-images/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.resource.size < 5242880; // 5MB limit
    }
  }
}
```

4. Click **"Publish"**

---

## ✓ Complete Setup Checklist

- [ ] Android Studio installed
- [ ] Firebase project created
- [ ] Firebase Authentication enabled
- [ ] Firestore Database created
- [ ] Cloud Storage created
- [ ] google-services.json downloaded and placed
- [ ] Google Maps API key created
- [ ] Android project created with Kotlin
- [ ] Firebase dependencies added
- [ ] Google Maps API key added to AndroidManifest.xml
- [ ] Android Virtual Device created
- [ ] App runs on emulator without errors
- [ ] Firestore Rules published
- [ ] Storage Rules published

---

## 🎯 What's Installed/Configured

### In Android Studio
```
✅ Android SDK (API 26-34)
✅ Kotlin compiler
✅ Gradle build system
✅ Emulator
✅ Firebase SDK
✅ Google Maps SDK
✅ Jetpack libraries
✅ Coroutines
```

### In Firebase
```
✅ Authentication (Email/Password + Google)
✅ Firestore Database (NoSQL)
✅ Cloud Storage (images)
✅ Cloud Messaging (notifications)
✅ Analytics (tracking)
```

---

## 🚀 Ready to Code!

Once all boxes are checked, we'll build:

### Week 1: Core Features
- Firebase Auth UI (Login/Register)
- User models (Kotlin data classes)
- Firebase Repository pattern

### Week 2: Restaurant Features
- Add food listing form
- View my listings
- Firestore integration

### Week 3: NGO Features
- Browse listings
- Filter by distance/type
- Accept listings

### Week 4: Polish
- Maps integration
- Push notifications
- Offline support
- Deploy to Play Store

---

## ⏱️ Time Breakdown

| Step | Time |
|------|------|
| Android Studio install | 30 min |
| Firebase setup | 20 min |
| Google Maps API | 10 min |
| Create Android project | 30 min |
| Add Firebase & dependencies | 10 min |
| Google Maps key | 5 min |
| Virtual device | 15 min |
| Test run | 10 min |
| Firestore & Storage rules | 10 min |
| **TOTAL** | **~2.5 hours** |

---

## 📸 Send These Screenshots When Complete

1. Android Studio with project open and "Run" button highlighted
2. Android Emulator showing default FoodBridge app running
3. Firebase Console showing "foodbridge" project
4. Your email address (for reference)

---

## 🆘 Common Issues & Quick Fixes

| Issue | Fix |
|-------|-----|
| Gradle sync fails | File > Invalidate Caches > Restart |
| google-services.json not found | Download from Firebase > Project Settings |
| Build error about Firebase | Run: Build > Rebuild Project |
| Emulator won't start | Check: Device Manager > Create new device |
| Maps API not working | Verify API key in AndroidManifest.xml |
| Firebase auth not connecting | Check package name matches Firebase |

---

## ✅ Once Complete

Message me with:
- "Setup Complete!"
- Screenshots of:
  1. Android app running
  2. Firebase Console
  3. Emulator status

We'll immediately start:
- Building Authentication UI
- Creating data models
- Integrating Firebase Firestore

---

**Estimated Time to Complete Setup: 2.5-3 hours**

**Let's build FoodBridge!** 🚀
