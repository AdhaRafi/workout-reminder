# 🔧 Quick Fix - Error Installation

## ❌ Error yang Anda alami:

```
adb install app\build\outputs\apk\debug\app-debug.apk
```

Error message:
```
adb: The term 'adb' is not recognized as the name of a cmdlet, function, script file, or operable program. Check the spelling of the name, or if a path was included, verify that the path is correct and try again.
```

---

## ✅ Solusi CEPAT:

### **Opsi 1: Pakai Gradle Run (TERMUDAH)**

Tidak perlu ADB sama sekali!

```powershell
# Di terminal PowerShell (d:\projectt)

# Stop gradle daemon jika hang
.\gradlew.bat --stop

# Build dan install langsung ke device/emulator
.\gradlew.bat installDebug

# Atau run langsung (build + install + launch)
.\gradlew.bat run
```

**Tunggu sampai selesai**, lalu aplikasi otomatis terinstall dan buka!

---

### **Opsi 2: Pakai Android Studio (PALING MUDAH)**

1. **Buka Android Studio**
2. **Open Project:** Pilih folder `d:\projectt`
3. **Wait for sync** (Gradle sync otomatis)
4. **Klik tombol Run ▶️** di toolbar (atau Shift+F10)
5. **Pilih device/emulator** di popup
6. **Done!** App auto-build & install

---

### **Opsi 3: Setup ADB (Jika mau pakai command line)**

#### A. Install Platform Tools:

**Download:**
1. Buka: https://developer.android.com/tools/releases/platform-tools
2. Download "SDK Platform-Tools for Windows"
3. Extract ke: `C:\platform-tools`

**Add to PATH:**
```powershell
# Run as Administrator
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\platform-tools", "Machine")
```

**Restart Terminal**, lalu test:
```powershell
adb version
```

#### B. Connect Device:

**Emulator:**
```powershell
# Start emulator dari Android Studio
# Device Manager → Start

# Verify
adb devices
```

**Physical Device:**
1. Enable USB Debugging (Settings → Developer Options)
2. Connect USB
3. Allow USB debugging di popup
4. Verify:
   ```powershell
   adb devices
   ```

#### C. Install APK:
```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

---

## 🎯 Rekomendasi Saya:

### **Gunakan Opsi 1 (Gradle) - PALING CEPAT:**

```powershell
# Di terminal Anda (PowerShell)
cd d:\projectt

# Stop gradle yang hang
.\gradlew.bat --stop

# Build + Install + Run
.\gradlew.bat installDebug
```

**Atau jika ingin langsung run:**
```powershell
.\gradlew.bat run
```

**Syarat:**
- Device/emulator sudah running
- USB debugging enabled (untuk physical device)

---

## 📱 Cara Start Emulator (Jika belum ada)

### **Di Android Studio:**
1. Toolbar → Device Manager icon (📱)
2. Klik "Create Device" jika belum ada
3. Pilih device (contoh: Pixel 6)
4. Pilih system image (contoh: API 33/Android 13)
5. Klik "Finish"
6. Klik "Play ▶️" untuk start emulator

### **Via Command Line:**
```powershell
# List emulators
emulator -list-avds

# Start specific emulator
emulator -avd <nama_emulator>
```

---

## ✅ Verification

Setelah install sukses, Anda akan lihat:

```
BUILD SUCCESSFUL in 2m 30s
45 actionable tasks: 45 executed
```

Atau di Android Studio:
```
App installed successfully
```

---

## 🐛 Jika Masih Error

### **Error: No devices found**
```powershell
# Cek device
adb devices

# Jika kosong, start emulator atau connect device
```

### **Error: INSTALL_FAILED_UPDATE_INCOMPATIBLE**
```powershell
# Uninstall dulu
adb uninstall com.workoutreminder.app

# Install ulang
.\gradlew.bat installDebug
```

### **Error: Gradle build failed**
```powershell
# Clean
.\gradlew.bat clean

# Build ulang
.\gradlew.bat assembleDebug
```

---

## 🚀 Langkah Singkat (Copy-Paste Ready)

```powershell
# 1. Stop gradle
cd d:\projectt
.\gradlew.bat --stop

# 2. Install ke device
.\gradlew.bat installDebug

# 3. Launch manual
# Buka app "Workout Reminder" di device/emulator
```

**ATAU pakai Android Studio Run button (▶️) - PALING MUDAH!**

---

Pilih opsi yang paling nyaman untuk Anda! 😊
