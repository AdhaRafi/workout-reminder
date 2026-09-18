# 🔧 Fix Compilation Errors

## ✅ Sudah Diperbaiki:

### 1. build.gradle.kts - compileSdk syntax error
**Error:**
```kotlin
compileSdk {
    version = release(36) {
        minorApiLevel = 1
    }
}
```

**Fixed:**
```kotlin
compileSdk = 36
```

---

## 🔍 Potential Issues Lainnya:

### Issue 1: Desugaring untuk LocalDateTime (API 26+)

Jika Anda ingin support Android < 26, tambahkan desugaring:

**Di build.gradle.kts:**
```kotlin
android {
    ...
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Add this:
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    ...
    // Add this:
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}
```

**Tapi TIDAK PERLU jika minSdk = 26 (sudah support LocalDateTime).**

---

### Issue 2: Cek Desugaring Setting

Buka `app/build.gradle.kts`, cari bagian `compileOptions`:

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

Pastikan tidak ada `isCoreLibraryDesugaringEnabled` jika minSdk = 26.

---

## 🚀 Langkah Build Ulang:

```powershell
# 1. Clean project
.\gradlew.bat clean

# 2. Stop daemon
.\gradlew.bat --stop

# 3. Build debug
.\gradlew.bat assembleDebug

# Atau langsung install jika device ready
.\gradlew.bat installDebug
```

---

## 🐛 Troubleshooting Build Errors:

### Jika error "Unresolved reference: TaskEntity"

**Solusi:**
```powershell
# Sync dependencies
.\gradlew.bat --refresh-dependencies

# Clean & rebuild
.\gradlew.bat clean build
```

### Jika error "Migration missing" saat run

**Solusi 1: Uninstall app dulu**
```powershell
adb uninstall com.workoutreminder.app
```

**Solusi 2: Sudah ada di AppDatabase.kt:**
```kotlin
.fallbackToDestructiveMigration(true)  // ← Sudah ada
```

Ini akan auto-delete & recreate database jika version berubah.

### Jika error di Converters

Cek `Converters.kt` ada import:
```kotlin
import com.workoutreminder.app.data.entity.TaskPriority
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
```

### Jika error "Cannot find symbol: TaskViewModel"

Rebuild project:
```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

---

## ✅ Verification Checklist:

Setelah build sukses, Anda akan lihat:

```
BUILD SUCCESSFUL in Xm Ys
Z actionable tasks: Z executed
```

Dan file APK di:
```
app\build\outputs\apk\debug\app-debug.apk
```

Check size:
```powershell
Get-Item app\build\outputs\apk\debug\app-debug.apk | Select-Object Name, Length
```

Expected size: ~5-10 MB

---

## 🎯 Next Steps After Build Success:

1. **Install:**
   ```powershell
   .\gradlew.bat installDebug
   ```

2. **Launch app manually** di device/emulator

3. **Test features** sesuai checklist di `BUILD_AND_TEST_GUIDE.md`

---

## 📞 Jika Masih Error:

**Di terminal, run:**
```powershell
.\gradlew.bat assembleDebug --info > build_log.txt
```

Lalu buka `build_log.txt` dan cari baris yang ada:
- `error:`
- `FAILED`
- `Exception`

Share error tersebut untuk fix lebih lanjut.

---

**Status: compileSdk sudah diperbaiki, siap build ulang!** ✅
