# Firebase Setup (FoodBridge)

This project is already wired for Firebase Auth + Firestore in code.

## 1) Create Firebase Project

1. Open Firebase Console.
2. Create a new project (or use existing).
3. Add Android app with package name: `com.foodbridge.app`.

## 2) Add Config File

1. Download `google-services.json` from Firebase Console.
2. Place it here:

`android/app/google-services.json`

Without this file, Firebase cannot initialize.

## 3) Enable Authentication

1. Firebase Console -> Authentication -> Sign-in method.
2. Enable `Email/Password`.
3. Create test users:
   - `restaurant@demo.com` / `demo123`
   - `ngo@demo.com` / `demo123`
   - `admin@demo.com` / `demo123`

## 4) Create Firestore Database

1. Firebase Console -> Firestore Database -> Create database.
2. Start in test mode for development.
3. Region: choose nearest to users.

## 5) Add User Profiles (Optional but Recommended)

Create collection: `users`

For each auth user, create a document with uid as document id and fields:

- `email` (string)
- `name` (string)
- `userType` (string: `restaurant` / `ngo` / `admin`)
- `createdAt` (number timestamp)

If a profile doc is missing, app auto-creates one on first login with inferred role.

## 6) Firestore Security Rules (Starter)

Use for development only; tighten before production.

```txt
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null;
    }

    match /listings/{listingId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## 7) Verify In App

1. Launch app.
2. Login with one of the test accounts.
3. Restaurant posts listing -> listing appears in NGO/Admin screens.

## Notes

- App currently uses Firebase first, then falls back to local dummy data if Firebase is unavailable.
- This allows continued development even before full Firebase setup is complete.