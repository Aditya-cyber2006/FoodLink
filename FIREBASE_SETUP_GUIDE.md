# FoodBridge - Firebase Console Setup Guide

## 🔥 FIREBASE SETUP (Browser-Based)

This guide walks you through setting up Firebase for FoodBridge. No coding required - just clicking in the Firebase Console!

---

## ✅ STEP 1: Create Firebase Project

### What to Do:

1. **Open Browser** → Go to: https://console.firebase.google.com/

2. **Click "Add Project"**
   - Project name: `foodbridge`
   - Uncheck "Enable Google Analytics" (optional, skip for simplicity)
   - Click "Create Project"
   - Wait 1-2 minutes for creation

3. **Click "Continue"** when done

### You Should See:
- Firebase Console with "foodbridge" project
- Three icons: Android, iOS, Web
- Empty dashboard

---

## ✅ STEP 2: Register Android App

### In Firebase Console:

1. Click **Android icon** (looks like <> symbol)

2. App registration form appears:
   ```
   Package name: com.foodbridge.app
   App nickname: FoodBridge
   SHA-1: (skip for now - we'll add later)
   ```

3. Click **"Register app"**

4. **IMPORTANT:** Click **"Download google-services.json"**
   - Save this file
   - You'll move it to `android/app/google-services.json` later
   - Location: Usually Downloads folder

5. Click **"Next"** (through other options) until done

### Key Point:
**KEEP THIS FILE SAFE** - You need it for the Android project!

---

## ✅ STEP 3: Enable Authentication

### In Firebase Console Left Sidebar:

1. Go to: **Build** > **Authentication**

2. Click **"Sign-in method"** tab

3. **Enable Email/Password:**
   - Click email/password provider
   - Toggle "Enable" ON
   - Click "Save"

4. **Enable Google Sign-in (Optional but Recommended):**
   - Click Google provider
   - Toggle "Enable" ON
   - For support email, select your email
   - Click "Save"

### Result:
You should see both enabled in the sign-in methods list

---

## ✅ STEP 4: Create Firestore Database

### In Firebase Console Left Sidebar:

1. Go to: **Build** > **Firestore Database**

2. Click **"Create Database"** button

3. Database Settings:
   ```
   Start in: Production mode
   Location: Select closest to you (Asia-South1 or Asia-East1)
   ```

4. Click **"Enable"** or **"Create"**

5. Wait 1-2 minutes for database to initialize

### You Should See:
- Empty Firestore database
- Collections: (empty)
- Rules tab

---

## ✅ STEP 5: Setup Cloud Storage

### In Firebase Console Left Sidebar:

1. Go to: **Build** > **Storage**

2. Click **"Get Started"** button

3. Storage Settings:
   ```
   Start in: Production mode
   Location: Same region as Firestore (from Step 4)
   ```

4. Click **"Done"** or **"Enable"**

5. Wait for initialization

### You Should See:
- Empty storage bucket
- Rules tab

---

## ✅ STEP 6: Get Google Maps API Key

### Go to: https://console.cloud.google.com/

1. **Top-left dropdown:** Select "foodbridge" project
   - It should be there (created automatically by Firebase)

2. **Search bar:** Type "Maps SDK for Android"

3. Click **"Maps SDK for Android"** from results

4. Click **"Enable"** button

5. Go to **"Credentials"** (left sidebar)

6. Click **"Create Credentials"** > **"API Key"**
   - Google Cloud creates an API Key
   - Copy this key

7. **SAVE THIS KEY** temporarily
   - You'll add it to AndroidManifest.xml later

---

## ✅ STEP 7: Publish Firestore Rules

### Back in Firebase Console:

1. Go to: **Firestore Database**

2. Click **"Rules"** tab

3. Replace all existing rules with:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection - read/write own only
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }

    // Food Listings - anyone can read, restaurants can write
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

    // Ratings - anyone can read/create
    match /ratings/{docId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
  }
}
```

4. Click **"Publish"** button

### What This Does:
- Users can only access their own profile
- Anyone can read food listings
- Only the restaurant can edit their listings
- NGOs can only see their notifications
- Everyone can rate

---

## ✅ STEP 8: Publish Cloud Storage Rules

### In Firebase Console:

1. Go to: **Cloud Storage**

2. Click **"Rules"** tab

3. Replace all existing rules with:

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

4. Click **"Publish"** button

### What This Does:
- Only authenticated users can read/write images
- Max file size: 5 MB
- All images stored under /food-images/ folder

---

## ✅ STEP 9: Enable Firebase Cloud Messaging (FCM)

### In Firebase Console:

1. Go to: **Build** > **Messaging**

2. You should see an overview page

3. No setup needed - it's automatically enabled
   - We'll configure it in Android code later

---

## ✅ VERIFICATION CHECKLIST

- [ ] Firebase project "foodbridge" created
- [ ] Android app registered (com.foodbridge.app)
- [ ] google-services.json downloaded
- [ ] Email/Password authentication enabled
- [ ] Google Sign-in enabled (optional)
- [ ] Firestore Database created
- [ ] Cloud Storage bucket created
- [ ] Firestore Rules published
- [ ] Storage Rules published
- [ ] Google Maps API key created
- [ ] Cloud Messaging visible

---

## 📋 What You Have Now

### Files to Collect:
1. **google-services.json** (from Firebase download)
   - Save to: `android/app/google-services.json`

2. **Google Maps API Key** (from Google Cloud)
   - You'll add to: `AndroidManifest.xml`

### Firebase Services Enabled:
- Authentication (Email + Google Sign-in)
- Firestore Database
- Cloud Storage
- Cloud Messaging

### Security Rules Published:
- Firestore Rules ✅
- Cloud Storage Rules ✅

---

## 🎯 Next Steps

Once Firebase setup is complete:

1. **Place `google-services.json`** in `android/app/` folder
2. **Add Google Maps API key** to `AndroidManifest.xml`
3. Continue with Android Studio setup (see SETUP_EXECUTION_GUIDE.md)

---

## 🆘 COMMON ISSUES

| Issue | Solution |
|-------|----------|
| Can't find Maps SDK | Make sure project is selected (top-left dropdown) |
| Rules won't publish | Check syntax - copy-paste exactly |
| Storage Rules error | Ensure bucket exists (Cloud Storage step) |
| API key not showing | Refresh page, then check Credentials |

---

## ⏱️ TIME ESTIMATE

| Step | Time |
|------|------|
| Create Firebase project | 3 min |
| Register Android app | 5 min |
| Setup Authentication | 3 min |
| Create Firestore | 2 min |
| Create Cloud Storage | 2 min |
| Get Maps API key | 5 min |
| Publish Firestore Rules | 2 min |
| Publish Storage Rules | 2 min |
| Setup Cloud Messaging | 1 min |
| **TOTAL** | **25 mins** |

---

**Once complete with Firebase setup, continue with SETUP_EXECUTION_GUIDE.md for Android Studio steps.**
