# FoodBridge - Android Project Summary

## 🎯 Project Vision (Android Only)

**FoodBridge** is a native Android application connecting restaurants with NGOs to reduce food waste and help feed people in need. Built with **Kotlin** and **Firebase** for maximum simplicity and scalability.

---

## 📊 Project Scope

### Scale
- **Users:** 1,000-100,000 restaurants and NGOs
- **Daily Listings:** 100-10,000 food items
- **Geographic Scope:** City/Region based
- **Timeline:** 8 weeks from setup to Play Store

### Problems Solved
1. **Food Waste:** Restaurants throw away excess food
2. **Hunger:** NGOs struggle to find food sources
3. **Inefficiency:** Manual coordination is slow
4. **Accountability:** No tracking of distributions

---

## 👥 System Users

### 1. **Restaurants** 🍽️
**Capabilities:**
- Register with business details
- Post food listings (name, quantity, pickup time)
- View pickup confirmations
- Rate NGOs
- Track food distributed

**Benefits:**
- Reduce waste (environmental impact)
- Tax deductions (potentially)
- Community recognition
- Compliance with FnBWaste rules

### 2. **NGOs** 🤝
**Capabilities:**
- Browse nearby available food listings
- Filter by food type, quantity, distance
- Accept a listing to pick up
- Navigate to restaurant location
- Confirm pickup completion
- Rate restaurants
- Track food collected

**Benefits:**
- Find food sources easily
- Reduce sourcing costs
- Scale distribution efforts
- Impact tracking (for donors/reports)

### 3. **Admin** (Optional Web Dashboard)
**Capabilities:**
- Monitor system health
- View all users and listings
- Remove fraudulent listings
- View analytics
- Manage reports

---

## 🛠️ TECH STACK (FINAL)

### Mobile Application (Native Android)
```
Language: Kotlin (Modern, Type-safe, Official)
Min API: 26 (Android 8.0)
Target API: 34 (Android 14)
Architecture: MVVM (Model-View-ViewModel)
UI Pattern: Fragments + Activities + Navigation Component
```

### Backend (Firebase - No Server Needed!) 🔥
```
Authentication: Firebase Auth (Email + Google Sign-in)
Database: Firestore (Real-time, NoSQL, Offline-capable)
Storage: Cloud Storage (Food images, max 5MB per image)
Notifications: Firebase Cloud Messaging (FCM)
Analytics: Firebase Analytics (automatic)
Functions: Cloud Functions (optional for complex logic)
```

### Location Services
```
Maps: Google Maps SDK for Android
Geolocation: Google Play Services Location
Geocoding: Places API
```

### Development Tools
```
IDE: Android Studio (official)
Version Control: Git + GitHub
Build System: Gradle
Language Server: Kotlin Compiler
Testing: JUnit + Espresso
Package Manager: Maven (via Gradle)
```

### Libraries & Frameworks
```
Core:
- androidx.appcompat:appcompat
- androidx.constraintlayout:constraintlayout
- com.google.android.material:material

Firebase:
- firebase-auth-ktx
- firebase-firestore-ktx
- firebase-storage-ktx
- firebase-messaging-ktx
- firebase-analytics-ktx

Architecture:
- lifecycle-viewmodel-ktx
- lifecycle-livedata-ktx
- navigation-fragment-ktx
- navigation-ui-ktx

Async:
- kotlinx-coroutines-android
- kotlinx-coroutines-core

Maps & Location:
- play-services-maps
- play-services-location

Image Loading:
- coil (Kotlin image loader)

Local Database:
- androidx.room:room-runtime
- androidx.room.room-ktx

JSON:
- com.google.code.gson:gson

Testing:
- junit:junit
- androidx.test.espresso:espresso-core
```

---

## 🏗️ ARCHITECTURE OVERVIEW

```
┌─────────────────────────────────────┐
│     Android User Interface          │
│  (Fragments, Activities, Screens)   │
└──────────────┬──────────────────────┘
               │
               │ User Actions
               ▼
┌─────────────────────────────────────┐
│      ViewModel (MVVM)               │
│  (Business Logic, State Management) │
└──────────────┬──────────────────────┘
               │
               │ Data Requests
               ▼
┌─────────────────────────────────────┐
│ Repository Pattern (Abstraction)    │
│  UserRepository, ListingRepository  │
│  NotificationRepository, etc        │
└──────────────┬──────────────────────┘
               │
   ┌───────────┴─────────────┐
   │                         │
   ▼                         ▼
Local Cache            Firebase Backend
(Room DB)           (Realtime Sync)
   │                         │
   │      ┌──────────────────┘
   │      │
   └──────┴────────────────────────┐
          │                        │
          ▼                        ▼
Offline Mode           Cloud Services
(Local Data)    (Auth, Storage, Messaging)
```

---

## 📋 DATABASE SCHEMA (Firestore Collections)

### Collection: `users`
```kotlin
data class User(
    val uid: String,                    // Firebase Auth UID
    val name: String,
    val email: String,
    val phone: String,
    val role: String,                   // "restaurant" | "ngo" | "admin"
    val location: LocationData,         // {address, latitude, longitude}
    val profileImage: String,           // Cloud Storage URL
    val ratings: Double,                // Average rating (0-5)
    val totalRatings: Int,
    val isVerified: Boolean,
    val createdAt: Long,               // Timestamp
    val updatedAt: Long
)
```

### Collection: `listings`
```kotlin
data class FoodListing(
    val listingId: String,
    val foodName: String,              // "Biryani", "Bread", etc
    val foodType: String,              // "veg" | "non-veg"
    val quantity: Double,              // 50 (in kg/pieces)
    val unit: String,                  // "kg" | "pieces" | "people"
    val servesPeople: Int?,            // 150 people
    val restaurantId: String,          // Reference to User
    val description: String,
    val imageUrl: String,              // Cloud Storage URL
    val pickupWindow: PickupWindow,   // {startTime, endTime}
    val location: LocationData,        // {address, lat, lng}
    val expiryTime: Long,
    val status: String,                // "available" | "accepted" | "collected" | "expired"
    val acceptedByNGO: String?,        // NGO user ID or null
    val createdAt: Long,
    val updatedAt: Long
)
```

### Collection: `notifications`
```kotlin
data class Notification(
    val notificationId: String,
    val userId: String,                // Recipient ID
    val type: String,                  // "new-listing" | "listing-accepted" | etc
    val title: String,
    val message: String,
    val listingId: String?,            // Related listing (optional)
    val isRead: Boolean,
    val createdAt: Long
)
```

### Collection: `ratings`
```kotlin
data class Rating(
    val ratingId: String,
    val fromUserId: String,            // Who rated
    val toUserId: String,              // Who was rated
    val rating: Int,                   // 1-5 stars
    val review: String,
    val listingId: String,             // Related listing
    val createdAt: Long
)
```

---

## 🔐 SECURITY MODEL

### Authentication
- ✅ Firebase Email/Password auth
- ✅ Google Sign-in
- ✅ Automatic token refresh
- ✅ Session management

### Authorization (Firestore Rules)
```
✅ Users can only read/write their own profiles
✅ Anyone (authenticated) can read listings
✅ Only creator can edit/delete their listings
✅ Only recipient can read notifications
✅ Ratings enforced at app level
```

### Data Protection
- ✅ All data encrypted in transit (HTTPS)
- ✅ All data encrypted at rest (Firebase default)
- ✅ Images stored in Cloud Storage (private by default)
- ✅ No sensitive data in logs
- ✅ SHA-1 key fingerprint for app signing

---

## 🎨 UI/UX DESIGN

### Color Scheme
```
Primary: #10B981    (Emerald - Environmental impact)
Secondary: #F59E0B  (Amber - Food/Warmth)
Accent: #EF4444    (Red - Urgent/Action)
Success: #10B981   (Green - Confirmed)
Background: #F3F4F6 (Light gray)
Text Dark: #1F2937 (Dark gray)
```

### Navigation Structure
```
┌─ Auth Flow
│  ├─ Login Screen
│  ├─ Register Screen
│  └─ Role Selection
│
├─ Restaurant Tab
│  ├─ Dashboard
│  ├─ Add Listing
│  ├─ My Listings
│  └─ Profile
│
├─ NGO Tab
│  ├─ Browse Listings
│  ├─ Map View
│  ├─ My Accepted
│  └─ Profile
│
├─ Notifications
│  └─ Notifications List
│
└─ Profile
   └─ User Settings
```

---

## 📱 SCREEN LIST

### Authentication
1. **Splash/Welcome** - App intro
2. **Login** - Email + Google
3. **Register** - New account
4. **Role Selection** - Restaurant/NGO/Admin
5. **Email Verification** - Confirm email

### Restaurant Features
6. **Restaurant Dashboard** - Stats, quick actions
7. **Add Listing** - Form to add food
8. **My Listings** - View posted items
9. **Listing Details** - Edit/delete options
10. **Pickup Confirmations** - NGO pickups

### NGO Features
11. **NGO Dashboard** - Stats, nearby listings
12. **Browse Listings** - List/grid view
13. **Map View** - Restaurants on map
14. **Listing Details** - Full info + Accept
15. **Accepted Listings** - Pending pickups
16. **Navigation Screen** - Route to restaurant

### Common
17. **Notifications** - Real-time alerts
18. **Ratings Screen** - Rate after pickup
19. **User Profile** - Edit info
20. **Settings** - App preferences

---

## 🔄 KEY USER FLOWS

### Restaurant Posting Food
```
1. Restaurant logs in
2. Clicks "Add New Listing"
3. Fills form:
   - Food name, type (veg/non-veg)
   - Quantity + unit (kg/pieces)
   - Pickup time window
   - Manual location + optional image
4. Submits listing
5. Listing saved to Firestore
6. FCM notification sent to nearby NGOs
7. List updated real-time for NGOs
```

### NGO Finding & Accepting Food
```
1. NGO logs in
2. Sees "5 listings within 3km"
3. Opens map > sees restaurant markers
4. Clicks listing "50kg Biryani"
5. Sees: Photo, time, distance, restaurant info
6. Clicks "Accept"
7. Confirms acceptance
8. Gets restaurant contact + navigation route
9. Notification sent to restaurant
10. Goes to restaurant, collects food
11. Marks "Pickup Completed"
12. Can rate restaurant
```

### Rating & Impact Tracking
```
1. After pickup completion
2. Both can rate each other (1-5 stars)
3. Admin dashboard shows:
   - Total kg saved
   - Total people served
   - Active partnerships
   - Monthly activity chart
```

---

## ✨ CORE FEATURES (MVP)

### Login & Registration
- ✅ Email/Password signup
- ✅ Google Sign-in
- ✅ Role selection (Restaurant/NGO)
- ✅ Phone number verification (optional)

### Restaurant Panel
- ✅ Add food listing with details
- ✅ Upload food image
- ✅ Set pickup time window
- ✅ View active listings
- ✅ Mark as collected
- ✅ See pickup history

### NGO Panel
- ✅ View nearby listings (real-time)
- ✅ Filter by food type & quantity
- ✅ See distance from location
- ✅ Accept a listing
- ✅ Get restaurant contact details
- ✅ Mark pickup completed
- ✅ View pickup history

### Real-time Features
- ✅ Push notifications on new listings
- ✅ Live listing updates
- ✅ Acceptance notifications
- ✅ Pickup completion notifications

### Maps Integration
- ✅ Show restaurant location
- ✅ Show nearby listings on map
- ✅ Navigation integration
- ✅ Distance calculation

### Ratings & Reviews
- ✅ Rate after completing pickup
- ✅ Leave review text (optional)
- ✅ View user ratings
- ✅ Block low-rated users (future)

### Admin Dashboard (Optional Web)
- ✅ Total food saved (kg)
- ✅ Active listings count
- ✅ User statistics
- ✅ Daily/weekly activity chart
- ✅ Remove spam listings

---

## 🚀 DEVELOPMENT PHASES

| Phase | Duration | Focus |
|-------|----------|-------|
| **1** | Week 1 | Setup, Firebase config, Project structure |
| **2** | Week 2 | Auth system, User models, Data classes |
| **3** | Week 3 | Restaurant panel, Add listing form |
| **4** | Week 3-4 | NGO panel, Browse, Filter, Accept |
| **5** | Week 4 | Maps integration, Location services |
| **6** | Week 5 | Real-time notifications (FCM) |
| **7** | Week 5-6 | Rating system, User profiles |
| **8** | Week 6-7 | Testing, Bug fixes, Performance |
| **9** | Week 7-8 | Polish, Dark mode, Offline support |
| **10** | Week 8 | Deploy to Play Store |

---

## 💾 DATA FLOW EXAMPLE

### When Restaurant Adds Listing:

```
User Input (Form)
    ↓
ViewModel validates input
    ↓
Repository.addListing(data)
    ↓
Firestore.collection("listings").add(data)
    ↓
Firestore triggers FCM notification
    ↓
All nearby NGOs receive notification
    ↓
Real-time listener notifies NGO apps
    ↓
NGO sees new listing appear in feed
```

---

## ⚡ Performance Targets

| Metric | Target |
|--------|--------|
| Login time | < 3 seconds |
| Load listings | < 2 seconds |
| Accept listing | < 1 second |
| Map load | < 3 seconds |
| Push notification delivery | < 10 seconds |
| Offline mode switching | < 500ms |

---

## 📊 COST BREAKDOWN

### Firebase Free Tier (Sufficient for MVP)
```
Authentication:    Free (unlimited)
Firestore:         50K reads/month free
Cloud Storage:     5GB free
Cloud Messaging:   Free (unlimited)
Hosting:          12.5GB bandwidth/month free
Total Monthly Cost: $0
```

### As You Scale
```
Firestore overage: $0.06 per 100K read/write
Cloud Storage:     $0.18 per GB
Bandwidth:         $0.12 per GB (after free tier)
Estimated at 10K users: $10-50/month
```

---

## ✅ Success Criteria

### MVP Launch
- ✅ Users can register and login
- ✅ Restaurants can post food listings
- ✅ NGOs can browse and accept listings
- ✅ Real-time notifications work
- ✅ Maps show nearby listings
- ✅ App works offline (local cache)
- ✅ Ratings system functional
- ✅ Available on Play Store

### Quality
- ✅ Minimum 4.0 star rating on Play Store
- ✅ Zero crashes in first week
- ✅ Response times < 2 seconds
- ✅ 95%+ user retention after 1 week
- ✅ 99%+ uptime

---

## 🎓 What You'll Learn

### Android Development
- MVVM architecture pattern
- Kotlin best practices
- Fragment lifecycle management
- Navigation component
- Coroutines & async programming
- LiveData & StateFlow (reactive)
- Room database (local caching)

### Firebase
- Firestore real-time database
- Firebase Authentication
- Cloud Storage image handling
- Cloud Messaging (push notifications)
- Security rules & authorization

### UI/UX Development
- Material Design 3
- Responsive layouts
- Constraint layouts
- RecyclerView & adapters
- Custom components

### DevOps
- App signing certificates
- Play Store console
- Release management
- Crash analytics (Firebase)
- User analytics

---

## 📞 Support & Community

### Official Resources
- Android Docs: https://developer.android.com/
- Firebase Docs: https://firebase.google.com/docs
- Kotlin Docs: https://kotlinlang.org/docs
- Google Maps: https://developers.google.com/maps

### Community
- Stack Overflow #android #kotlin #firebase
- Reddit: r/androiddev
- GitHub: Android examples
- Medium: Kotlin tutorials

---

## 🎉 Next Steps

Once setup is complete:

1. **Phase 1:** Initialize Android project with Firebase
2. **Phase 2:** Build authentication system
3. **Phase 3:** Create data models and repositories
4. **Phase 4:** Build restaurant panel UI
5. **Phase 5:** Build NGO panel UI
6. **Phase 6:** Add real-time features
7. **Phase 7:** Maps and location features
8. **Phase 8:** Deploy to Play Store

---

## 📝 Project Files

```
FoodBridge/
├── ROADMAP.md                    ← Overall timeline
├── QUICK_SETUP_ANDROID.md        ← Android setup checklist
├── IMPLEMENTATION_ANDROID.md     ← Detailed setup guide
├── PROJECT_SUMMARY.md            ← This file
├── DATABASE_SCHEMA.md            ← Firebase collection details
└── android/                      ← Android Studio project
```

---

## 🎯 Vision Statement

> "FoodBridge is the fastest, easiest way for restaurants to donate surplus food and NGOs to find it. We're building a technology that turns food waste into community care."

---

**Built with Kotlin + Firebase for Android**
*Simplicity. Speed. Social Impact.*

*Last Updated: March 2026*
