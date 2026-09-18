# 🚀 Panduan Build & Test Aplikasi - Task Management Feature

## ⚠️ Troubleshooting Build Issues

Jika Gradle hang atau build timeout, ikuti langkah berikut:

### 1️⃣ **Stop Gradle Daemon**

```powershell
# Di terminal PowerShell
cd d:\projectt

# Stop semua gradle daemon
.\gradlew.bat --stop

# Atau paksa kill process Java
Get-Process | Where-Object {$_.ProcessName -like "*java*"} | Stop-Process -Force
```

### 2️⃣ **Clean Build**

```powershell
# Clean project
.\gradlew.bat clean

# Sync project (jika pakai Android Studio)
# File → Sync Project with Gradle Files
```

### 3️⃣ **Build APK Debug**

```powershell
# Build debug APK (lebih cepat)
.\gradlew.bat assembleDebug

# Atau build release (lebih lambat, perlu signing)
.\gradlew.bat assembleRelease
```

**Estimasi waktu:** 5-10 menit (tergantung PC)

---

## 📱 Install ke Device/Emulator

### **Opsi A: Menggunakan ADB (Command Line)**

#### 1. **Cek APK sudah ada:**
```powershell
# Cek file APK
Get-ChildItem -Path "app\build\outputs\apk\debug" -Filter "*.apk"
```

**Output yang diharapkan:**
```
app-debug.apk
```

#### 2. **Cek device terhubung:**
```powershell
adb devices
```

**Output yang diharapkan:**
```
List of devices attached
emulator-5554   device
# atau
ABC123XYZ       device  (untuk physical device)
```

Jika tidak ada device:
- **Emulator:** Buka Android Studio → Device Manager → Start emulator
- **Physical Device:** 
  - Enable USB Debugging di Settings → Developer Options
  - Connect USB cable
  - Allow USB debugging di popup

#### 3. **Install APK:**
```powershell
# Install ke device/emulator
adb install -r app\build\outputs\apk\debug\app-debug.apk

# Flag -r = reinstall (replace existing app)
```

**Output sukses:**
```
Performing Streamed Install
Success
```

#### 4. **Launch aplikasi:**
```powershell
# Start aplikasi otomatis
adb shell am start -n com.workoutreminder.app/.MainActivity
```

---

### **Opsi B: Menggunakan Android Studio (Lebih Mudah)**

1. **Buka Android Studio**
2. **Open Project:** `d:\projectt`
3. **Sync Gradle:** File → Sync Project with Gradle Files
4. **Select Device:** Pilih emulator/device di toolbar
5. **Run:** Klik tombol Run (▶️) atau Shift+F10
6. **Wait:** Android Studio akan build & install otomatis

---

### **Opsi C: Manual Install (Drag & Drop)**

1. **Locate APK:**
   ```
   d:\projectt\app\build\outputs\apk\debug\app-debug.apk
   ```

2. **Emulator:**
   - Drag & drop file APK ke window emulator
   - Wait untuk auto-install
   - Open app dari app drawer

3. **Physical Device:**
   - Copy APK ke internal storage
   - Buka file manager di device
   - Tap APK file
   - Klik "Install"
   - Allow "Install from unknown sources" jika diminta

---

## 🧪 Testing Checklist

### **Test 1: Buka Tab Tasks**
- [ ] Buka aplikasi
- [ ] Tap tab "Tasks" di bottom navigation
- [ ] Lihat header "Task Manager"
- [ ] Lihat statistik (0 Aktif, 0 Selesai, 0 Total)
- [ ] Lihat empty state dengan icon 🎯

### **Test 2: Tambah Task Baru**
- [ ] Tap FAB (+) di kanan bawah
- [ ] Dialog "Buat Task Baru" muncul
- [ ] Isi form:
  - Judul: "Beli Protein Powder"
  - Deskripsi: "Stok habis, perlu beli yang baru"
  - Kategori: Tap "Belanja"
  - Prioritas: Tap "Tinggi"
  - Tanggal: Tap 📅, pilih besok
  - Jam: Tap 🕐, pilih 18:00
- [ ] Tap "Simpan"
- [ ] Snackbar muncul: "Task ... berhasil disimpan!"
- [ ] Task muncul di list
- [ ] Statistik update: 1 Aktif, 1 Total

### **Test 3: Expand Card**
- [ ] Tap card yang baru dibuat
- [ ] Card expand dengan animasi smooth
- [ ] Deskripsi muncul
- [ ] Tombol Edit & Delete muncul
- [ ] Tanggal "Dibuat" muncul di kanan bawah
- [ ] Tap lagi untuk collapse

### **Test 4: Mark as Complete**
- [ ] Tap checkbox (⭕) pada task
- [ ] Checkbox berubah jadi ✅ (hijau)
- [ ] Text jadi strikethrough
- [ ] Card background jadi lebih gelap
- [ ] Snackbar: "Task ... selesai! ✅"
- [ ] Statistik update: 0 Aktif, 1 Selesai

### **Test 5: Undo (Mark Incomplete)**
- [ ] Tap checkbox (✅) pada completed task
- [ ] Checkbox kembali jadi ⭕
- [ ] Strikethrough hilang
- [ ] Card background normal
- [ ] Statistik update: 1 Aktif, 0 Selesai

### **Test 6: Filter Tasks**
- [ ] Buat 2-3 task lagi (mix completed & active)
- [ ] Tap chip "Aktif"
- [ ] Hanya task belum selesai yang muncul
- [ ] Tap chip "Selesai"
- [ ] Hanya completed tasks yang muncul
- [ ] Tap chip "Semua"
- [ ] Semua task muncul

### **Test 7: Edit Task**
- [ ] Expand task
- [ ] Tap tombol Edit (✏️)
- [ ] Dialog "Edit Task" muncul
- [ ] Form terisi dengan data task
- [ ] Ubah prioritas dari "Tinggi" ke "Urgent"
- [ ] Tap "Simpan"
- [ ] Badge prioritas update jadi "Mendesak" warna merah gelap

### **Test 8: Delete Task**
- [ ] Expand task
- [ ] Tap tombol Delete (🗑️)
- [ ] Dialog konfirmasi muncul
- [ ] Tap "Hapus"
- [ ] Task hilang dari list
- [ ] Snackbar: "Task dihapus"
- [ ] Statistik update

### **Test 9: Overdue Task**
Untuk test ini, perlu edit tanggal manual di database atau code:

**Cara 1: Edit Database (Advanced)**
```kotlin
// Di AddEditTaskDialog.kt, ubah sementara:
dueDate = LocalDateTime.now().minusDays(2) // 2 hari lalu
```

**Cara 2: Set deadline yang dekat, tunggu**
- Buat task dengan deadline 1 menit ke depan
- Tunggu 1 menit
- Refresh (tutup dan buka app lagi)
- Task seharusnya jadi overdue dengan:
  - [ ] Border merah
  - [ ] Icon ⚠️
  - [ ] Text "TERLAMBAT" warna merah

### **Test 10: Categories**
Buat task untuk setiap kategori:
- [ ] Umum
- [ ] Kerja
- [ ] Pribadi
- [ ] Latihan
- [ ] Belanja
- [ ] Belajar
- [ ] Kesehatan

Verify badge kategori muncul di setiap card.

### **Test 11: Priorities**
Buat 4 task dengan prioritas berbeda:
- [ ] Rendah (hijau)
- [ ] Sedang (orange)
- [ ] Tinggi (merah)
- [ ] Urgent/Mendesak (dark red)

Verify warna badge sesuai dan sorting bekerja.

### **Test 12: No Deadline**
- [ ] Buat task tanpa set deadline
- [ ] Task muncul tanpa icon ⏰ atau ⚠️
- [ ] Hanya kategori yang muncul
- [ ] Task masih bisa dicomplete

### **Test 13: Long Text**
- [ ] Buat task dengan judul sangat panjang (50+ karakter)
- [ ] Verify text truncate dengan "..." saat collapsed
- [ ] Expand → full text muncul
- [ ] Buat task dengan deskripsi panjang (200+ karakter)
- [ ] Verify deskripsi muncul full saat expanded

### **Test 14: Tab Navigation**
- [ ] Switch ke tab "Jadwal" → workout reminders muncul
- [ ] Switch ke tab "Aktivitas" → achievements muncul
- [ ] Switch ke tab "Tasks" → tasks muncul
- [ ] Switch ke tab "Profil" → profile muncul
- [ ] Verify data persist saat switch tab

### **Test 15: App Lifecycle**
- [ ] Buat beberapa tasks
- [ ] Press Home button (minimize app)
- [ ] Open app lagi
- [ ] Verify tasks masih ada (persisted di database)
- [ ] Force close app
- [ ] Open lagi
- [ ] Verify tasks masih ada

---

## 🐛 Common Issues & Fixes

### **Issue 1: Build Error - Unresolved Reference**
```
Error: Unresolved reference: TaskEntity
```

**Fix:**
```powershell
# Sync Gradle
.\gradlew.bat --refresh-dependencies

# Clean & rebuild
.\gradlew.bat clean build
```

### **Issue 2: Database Version Conflict**
```
Error: Migration missing
```

**Fix:**
```kotlin
// Di AppDatabase.kt, pastikan version = 4
@Database(
    entities = [..., TaskEntity::class],
    version = 4,  // ← Harus 4
    exportSchema = false
)

// Atau gunakan fallback:
.fallbackToDestructiveMigration(true) // ← Sudah ada
```

**OR** Uninstall app dulu:
```powershell
adb uninstall com.workoutreminder.app
```
Lalu install ulang.

### **Issue 3: APK Not Found**
```
Error: Can't find app-debug.apk
```

**Fix:**
```powershell
# Cek apakah build sukses
Get-ChildItem -Path "app\build\outputs" -Recurse -Filter "*.apk"

# Jika tidak ada, build ulang:
.\gradlew.bat assembleDebug
```

### **Issue 4: ADB Not Found**
```
Error: adb is not recognized
```

**Fix:**
1. Install Android SDK
2. Add to PATH:
   ```
   C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools
   ```
3. Restart terminal

### **Issue 5: Task Not Showing**
**Kemungkinan penyebab:**
1. Database belum initialize → Restart app
2. ViewModel tidak terinject → Check MainActivity
3. Flow tidak ter-collect → Check TasksScreen

**Debug:**
```kotlin
// Tambahkan log di TaskViewModel
init {
    viewModelScope.launch {
        tasks.collect { taskList ->
            Log.d("TaskViewModel", "Tasks: ${taskList.size}")
        }
    }
}
```

### **Issue 6: Crash on Open Tasks Tab**
**Check Logcat:**
```powershell
adb logcat | Select-String "workoutreminder"
```

**Common causes:**
- Missing import
- Null pointer
- Database not initialized

---

## 📊 Performance Benchmarks

### Expected Performance:
- **App Launch:** < 2 seconds
- **Tab Switch:** < 100ms (instant)
- **Add Task:** < 500ms
- **Complete Task:** < 200ms
- **Filter Switch:** < 100ms (instant)
- **Card Expand:** < 16ms (60fps animation)

### Memory Usage:
- **Idle:** ~50-80 MB
- **With 100 tasks:** ~80-100 MB
- **Heavy usage:** < 150 MB

---

## 🎯 Sample Data for Testing

Anda bisa add tasks ini untuk testing lengkap:

### **Urgent/High Priority:**
```
1. Konsultasi dengan Personal Trainer
   - Kategori: Latihan
   - Prioritas: Urgent
   - Deadline: Besok, 15:00

2. Beli Protein Powder & Suplemen
   - Kategori: Belanja
   - Prioritas: Tinggi
   - Deadline: 3 hari lagi

3. Submit Laporan Fitness Q1
   - Kategori: Kerja
   - Prioritas: Urgent
   - Deadline: Minggu depan
```

### **Medium Priority:**
```
4. Riset Resep Meal Prep
   - Kategori: Kesehatan
   - Prioritas: Sedang
   - Deadline: 5 hari lagi

5. Baca Artikel Nutrisi Makro
   - Kategori: Belajar
   - Prioritas: Sedang
   - Deadline: Tidak ada
```

### **Low Priority:**
```
6. Update Playlist Workout
   - Kategori: Pribadi
   - Prioritas: Rendah
   - Deadline: Tidak ada

7. Research Gym Bag Brands
   - Kategori: Belanja
   - Prioritas: Rendah
   - Deadline: Kapan saja
```

---

## ✅ Success Criteria

Fitur Task Management dianggap **BERHASIL** jika:

✅ Semua 15 test case PASS  
✅ Tidak ada crash  
✅ Performa smooth (60fps)  
✅ Data persist setelah app restart  
✅ UI konsisten dengan tema aplikasi  
✅ All features working as expected  

---

## 📞 Troubleshooting Support

Jika mengalami masalah:

1. **Check Logcat:**
   ```powershell
   adb logcat > log.txt
   # Cari error di log.txt
   ```

2. **Check File Structure:**
   Pastikan semua file ada di lokasi yang benar

3. **Clean & Rebuild:**
   ```powershell
   .\gradlew.bat clean
   .\gradlew.bat assembleDebug
   ```

4. **Fresh Install:**
   ```powershell
   adb uninstall com.workoutreminder.app
   adb install app\build\outputs\apk\debug\app-debug.apk
   ```

---

## 🚀 Ready to Test!

Setelah build sukses dan install, ikuti testing checklist di atas untuk memastikan semua fitur berfungsi dengan baik!

**Good luck! 💪**
