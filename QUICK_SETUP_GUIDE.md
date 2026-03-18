# FoodBridge - Quick Setup Guide

## 🚀 TL;DR - What You Need to Do RIGHT NOW

This is a quick checklist to get everything ready. For details, see IMPLEMENTATION.md.

---

## ✅ STEP-BY-STEP MANUAL SETUP (Do This First!)

### **STEP 1: Install Required Software on Your Computer**

Run these installations:

#### 1.1 Download & Install Node.js
```
Website: https://nodejs.org/
Version: LTS (Long-term Support)
After install, verify:
  Open PowerShell
  Type: node --version
  Expected: v18.0.0 or higher
```

#### 1.2 Download & Install Git
```
Website: https://git-scm.com/download/win
After install, verify:
  Open PowerShell
  Type: git --version
  Expected: git version 2.x.x
```

#### 1.3 Download & Install VS Code (Recommended Editor)
```
Website: https://code.visualstudio.com/
Then install these extensions:
  - ES7+ React/Redux/React-Native snippets
  - Prettier - Code formatter
  - ESLint
  - Thunder Client (API testing)
```

#### 1.4 Create MongoDB Atlas Account (FREE)
```
Go to: https://www.mongodb.com/cloud/atlas

Steps:
1. Click "Sign up with Email"
2. Fill email, password, accept terms
3. Click "Create Account"
4. Verify email
5. Create organization (name: "FoodBridge")
6. Create project (name: "foodlink-dev")
7. Build a cluster:
   - Click "Build a Database"
   - Choose "Shared" (FREE)
   - Select region closest to you
   - Click "Create"
8. Wait for cluster to deploy (2-3 minutes)
9. Click "Connect"
10. Choose "Drivers"
11. Copy connection string (looks like: mongodb+srv://...)
12. Save this string somewhere safe - you'll use it later!
13. IMPORTANT: Replace <password> with actual password
```

#### 1.5 Install Cloudinary (FREE - for image upload)
```
Go to: https://cloudinary.com
Steps:
1. Create account
2. No credit card needed (Free tier)
3. Copy API Key from dashboard
4. Save this for later
```

---

### **STEP 2: Create Project Folder Structure**

Open PowerShell and run:

```powershell
# Navigate to your folder
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

# Create folders
mkdir backend
mkdir frontend
mkdir frontend\web
mkdir frontend\mobile
mkdir docs

# Verify (should see all folders)
ls
```

---

### **STEP 3: Initialize Git Repository**

```powershell
# Make sure you're in foodlink folder
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink"

# Initialize git
git init

# Configure your git (use your real name and email)
git config user.name "Your Full Name"
git config user.email "your.email@gmail.com"

# Verify
git config --list
```

---

### **STEP 4: Create .gitignore File**

In VS Code:
1. Open the foodlink folder
2. Create new file: `.gitignore`
3. Paste this content:

```
# Dependencies
node_modules/
*/node_modules

# Environment variables
.env
.env.local
.env.*.local

# Build files
dist/
build/

# IDE
.vscode/
.idea/
*.swp

# OS
.DS_Store
Thumbs.db

# Logs
logs/
*.log
npm-debug.log*

# MongoDB
mongodb-*.log
```

4. Save the file

---

### **STEP 5: Setup Backend (Node + Express + MongoDB)**

```powershell
# Navigate to backend
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\backend"

# Initialize npm
npm init -y

# Install dependencies (copy-paste entire command)
npm install express mongoose jsonwebtoken bcryptjs cors socket.io multer joi dotenv axios

# Install development dependencies
npm install --save-dev nodemon

# Verify installation (should see many files in node_modules)
ls
```

---

### **STEP 6: Create Backend .env File**

Inside `backend/` folder, create `.env` file:

```
PORT=5000
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/foodbridge?retryWrites=true&w=majority
JWT_SECRET=your_super_secret_key_change_this_to_random_string_12345
NODE_ENV=development
FRONTEND_URL=http://localhost:3000
```

**IMPORTANT:** 
- Replace the MONGODB_URI with your actual MongoDB Atlas connection string
- Replace `<password>` in the string with your actual MongoDB password
- Change `JWT_SECRET` to something random

---

### **STEP 7: Update Backend package.json**

Open `backend/package.json` and update the scripts section:

```json
{
  "name": "foodbridge-backend",
  "version": "1.0.0",
  "description": "FoodBridge API",
  "main": "src/server.js",
  "scripts": {
    "start": "node src/server.js",
    "dev": "nodemon src/server.js",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "type": "module",
  "keywords": [],
  "author": "",
  "license": "ISC",
  "dependencies": {
    "axios": "^1.4.0",
    "bcryptjs": "^2.4.3",
    "cors": "^2.8.5",
    "dotenv": "^16.0.3",
    "express": "^4.18.2",
    "joi": "^17.9.1",
    "jsonwebtoken": "^9.0.0",
    "mongoose": "^7.0.0",
    "multer": "^1.4.5",
    "socket.io": "^4.6.1"
  },
  "devDependencies": {
    "nodemon": "^2.0.20"
  }
}
```

---

### **STEP 8: Setup Frontend (React + Tailwind)**

```powershell
# Navigate to frontend/web
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\frontend\web"

# Create React app with Vite (MUCH faster than Create React App)
npm create vite@latest . -- --template react

# When prompted, select "react"

# Install dependencies
npm install

# Install additional packages
npm install react-router-dom zustand axios socket.io-client react-hook-form zod

# Install Tailwind
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p

# Verify
ls
```

---

### **STEP 9: Setup Tailwind CSS**

Edit `frontend/web/tailwind.config.js`:

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#10B981',
        secondary: '#F59E0B',
        accent: '#EF4444',
      }
    },
  },
  plugins: [],
}
```

Edit `frontend/web/src/index.css` (replace all content):

```css
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans',
    'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
```

---

### **STEP 10: Create Frontend .env File**

Create `frontend/web/.env`:

```
VITE_API_URL=http://localhost:5000/api
VITE_APP_NAME=FoodBridge
```

---

### **STEP 11: Create MongoDB Database & Collection**

In MongoDB Atlas:

```
1. Go to Collections
2. Create Database:
   - Database: foodbridge
   - Collection: users
3. Then we'll create other collections via code
```

---

### **STEP 12: Verify Everything Works**

Test Backend:
```powershell
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\backend"
npm run dev

# Should see:
# Server running on port 5000
# MongoDB connected
```

Test Frontend (in a NEW PowerShell window):
```powershell
cd "C:\Users\ADITYA MUNGASE\OneDrive\Desktop\foodlink\frontend\web"
npm run dev

# Should see:
# VITE v4.x.x ready in xxx ms
# Local: http://localhost:5000
```

---

## 📋 Complete Checklist

### Prerequisites
- [ ] Node.js installed (v18+)
- [ ] Git installed and configured
- [ ] VS Code installed with extensions
- [ ] MongoDB Atlas account created
- [ ] MongoDB connection string copied
- [ ] Cloudinary account created (optional but recommended)

### Project Setup
- [ ] Project folder structure created
- [ ] Git repository initialized
- [ ] .gitignore file created
- [ ] Backend npm initialized
- [ ] Backend .env created with MongoDB URI
- [ ] Backend dependencies installed
- [ ] Backend package.json scripts updated
- [ ] Frontend npm initialized with Vite
- [ ] Frontend dependencies installed
- [ ] Tailwind CSS installed and configured
- [ ] Frontend .env created
- [ ] MongoDB database and users collection created

### Verification
- [ ] `node --version` returns v18+
- [ ] `npm --version` works
- [ ] `git --version` works
- [ ] MongoDB connection string works
- [ ] Backend starts: `npm run dev` (shows "Server running")
- [ ] Frontend starts: `npm run dev` (shows local URL)
- [ ] Can access http://localhost:5173 (React welcome page)

---

## 🎯 Once Setup is Complete

1. Take a screenshot of:
   - Backend running (`npm run dev`)
   - Frontend running (http://localhost:5173)
   - MongoDB Atlas showing connected

2. Message me with "Setup Complete!" and the screenshots

3. We'll begin Phase 1: Backend API Development

---

## 🆘 Common Issues & Fixes

### Issue: MongoDB connection fails
```
Solution:
1. Check .env has correct connection string
2. Check <password> is replaced with actual password
3. Check IP whitelist in MongoDB Atlas
4. Go to Security > Network Access
5. Click "Add IP Address"
6. Add 0.0.0.0/0 (allow all - development only)
```

### Issue: npm command not found
```
Solution:
1. Restart PowerShell
2. Reinstall Node.js
3. Close and reopen terminal
```

### Issue: Port 5000 already in use
```
Solution:
1. Find what's using port 5000
2. Change PORT in .env to 5001
3. Restart backend
```

### Issue: npm packages won't install
```
Solution:
1. Delete node_modules folder
2. Delete package-lock.json
3. Run npm install again
```

### Issue: Vite not starting
```
Solution:
1. Make sure you're in frontend/web folder
2. Delete node_modules
3. npm install
4. npm run dev
```

---

## 📞 Next Steps After Setup

Once everything is running:

1. We'll build the **Backend API** first
2. Then **Frontend Web App**
3. Then **Mobile App**
4. Then **Advanced Features**
5. Finally **Deployment**

Each phase will be clearly documented with code examples.

---

## 📝 Time Estimate

- Prerequisites installation: 30 minutes
- Project setup: 30 minutes
- Verification: 15 minutes

**Total: ~1.5 hours**

---

**You're ready to build FoodBridge!** 🚀

Let me know when setup is complete.
