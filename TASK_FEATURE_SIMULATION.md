# 📱 Simulasi Fitur Task Management - Workout Reminder App

## 🎯 Overview
Fitur Task Management telah ditambahkan sebagai tab ke-3 di bottom navigation, antara "Aktivitas" dan "Profil".

---

## 🚀 Skenario Penggunaan

### **Skenario 1: Membuka Tab Tasks Pertama Kali**

**Aksi:** User tap tab "Tasks" di bottom navigation

**Yang Terlihat:**
```
┌─────────────────────────────────────────┐
│  🔶 Task Manager                        │
│     Kelola Tugas & Aktivitas            │
├─────────────────────────────────────────┤
│                                         │
│  ┌────────────────────────────────┐    │
│  │  📊 Statistik Task             │    │
│  │                                │    │
│  │     7        1        8        │    │
│  │   Aktif   Selesai  Total       │    │
│  └────────────────────────────────┘    │
│                                         │
│  🔽 Filter:                             │
│  [Semua (8)] [Aktif (7)] [Selesai (1)] │
│                                         │
│  📋 DAFTAR TASKS:                       │
│  ┌────────────────────────────────┐    │
│  │ ⭕ Konsultasi Personal Trainer │🔴│ │
│  │    📁 Latihan                  │    │
│  │    ⏰ 16 Sep 2026, 15:00      │    │
│  │    (3 jam lagi)                │    │
│  └────────────────────────────────┘    │
│                                         │
│  ┌────────────────────────────────┐    │
│  │ ⭕ Beli Protein Powder...      │🔴│ │
│  │    📁 Belanja                  │    │
│  │    ⚠️ 14 Sep 2026 • TERLAMBAT │    │
│  └────────────────────────────────┘    │
│                                         │
│  ┌────────────────────────────────┐    │
│  │ ⭕ Selesaikan Laporan Q1...    │🔴│ │
│  │    📁 Kerja                    │    │
│  │    ⏰ 21 Sep 2026, 23:59      │    │
│  └────────────────────────────────┘    │
│                                         │
│  ┌────────────────────────────────┐    │
│  │ ⭕ Coba Leg Day Routine Baru   │🔴│ │
│  │    📁 Latihan                  │    │
│  │    ⏰ 19 Sep 2026, 23:59      │    │
│  └────────────────────────────────┘    │
│                                         │
│              [+] FAB                    │
└─────────────────────────────────────────┘
```

**Keterangan Warna:**
- 🔴 Tinggi/Urgent (Merah)
- 🟠 Sedang (Orange)
- 🟢 Rendah (Hijau)
- ⚠️ Overdue (Merah + warning icon)

---

### **Skenario 2: Tap Card untuk Expand Detail**

**Aksi:** User tap pada card "Beli Protein Powder"

**Yang Terlihat:**
```
┌────────────────────────────────────────┐
│ ⭕ Beli Protein Powder dan Suplemen 🔴 │
│    📁 Belanja                          │
│    ⚠️ 14 Sep 2026, 23:59 • TERLAMBAT  │
│                                        │
│    📝 Deskripsi:                       │
│    Stok protein powder habis, perlu    │
│    beli yang baru di toko suplemen     │
│                                        │
│    [✏️] [🗑️]         Dibuat: 11 Sep   │
└────────────────────────────────────────┘
```

**Interaksi:**
- Tap lagi untuk collapse
- Tombol ✏️ untuk edit
- Tombol 🗑️ untuk delete
- Tap checkbox ⭕ untuk mark as done

---

### **Skenario 3: Menambah Task Baru**

**Aksi:** User tap tombol FAB (+) di kanan bawah

**Yang Terlihat - Dialog Muncul:**
```
┌──────────────────────────────────────┐
│  Buat Task Baru              ✖️      │
├──────────────────────────────────────┤
│                                      │
│  Judul Task                          │
│  ┌────────────────────────────────┐  │
│  │ [                            ] │  │
│  └────────────────────────────────┘  │
│                                      │
│  Deskripsi (Opsional)                │
│  ┌────────────────────────────────┐  │
│  │                                │  │
│  │                                │  │
│  │                                │  │
│  └────────────────────────────────┘  │
│                                      │
│  Kategori:                           │
│  [Umum] [Kerja] [Pribadi] [Latihan] │
│  [Belanja] [Belajar] [Kesehatan]    │
│                                      │
│  Prioritas:                          │
│  [Rendah] [Sedang] [Tinggi] [!]     │
│                                      │
│  Tenggat Waktu (Opsional):           │
│  [📅 Pilih Tanggal] [🕐 Pilih Jam]  │
│                                      │
│  [Batal]              [Simpan]       │
└──────────────────────────────────────┘
```

**User mengisi:**
1. Judul: "Beli Resistance Band"
2. Deskripsi: "Perlu beli resistance band set untuk home workout"
3. Kategori: tap "Belanja"
4. Prioritas: tap "Sedang"
5. Tanggal: tap "📅" → pilih 20 Sep 2026
6. Jam: tap "🕐" → pilih 18:00
7. Tap "Simpan"

**Hasil:** 
- Dialog tutup
- Snackbar muncul: "Task 'Beli Resistance Band' berhasil disimpan!"
- Task baru muncul di list

---

### **Skenario 4: Mark Task as Done**

**Aksi:** User tap checkbox pada task "Beli Resistance Band"

**Animasi:**
1. Checkbox berubah dari ⭕ ke ✅ (hijau)
2. Text berubah warna jadi abu-abu
3. Text ter-strike-through ~~Beli Resistance Band~~
4. Card background jadi lebih gelap
5. Snackbar: "Task 'Beli Resistance Band' selesai! ✅"

**Yang Terlihat:**
```
┌────────────────────────────────────┐
│ ✅ ~~Beli Resistance Band~~    🟠 │
│    📁 Belanja                      │
└────────────────────────────────────┘
```

**Statistik Update:**
- Aktif: 7 → 6
- Selesai: 1 → 2
- Total: 8

---

### **Skenario 5: Filter Tasks**

**Aksi:** User tap chip "Selesai (2)"

**Yang Terlihat:**
```
┌─────────────────────────────────────────┐
│  Filter Active:                         │
│  [Semua (8)] [Aktif (6)] [✓ Selesai]   │
│                                         │
│  📋 COMPLETED TASKS:                    │
│  ┌────────────────────────────────┐    │
│  │ ✅ ~~Beli Resistance Band~~   │🟠│  │
│  │    📁 Belanja                  │    │
│  └────────────────────────────────┘    │
│                                         │
│  ┌────────────────────────────────┐    │
│  │ ✅ ~~Beli Matras Yoga Baru~~  │🟠│  │
│  │    📁 Belanja                  │    │
│  └────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

**Kembali ke "Aktif":**
- Hanya tampil 6 task yang belum selesai
- Sorted by: priority → due date

---

### **Skenario 6: Edit Task**

**Aksi:** User expand task → tap ✏️ Edit

**Yang Terlihat:**
```
┌──────────────────────────────────────┐
│  Edit Task                   ✖️      │
├──────────────────────────────────────┤
│  Judul Task                          │
│  ┌────────────────────────────────┐  │
│  │ Beli Protein Powder...         │  │
│  └────────────────────────────────┘  │
│                                      │
│  Deskripsi:                          │
│  ┌────────────────────────────────┐  │
│  │ Stok protein powder habis...   │  │
│  └────────────────────────────────┘  │
│                                      │
│  Kategori: [✓ Belanja]              │
│  Prioritas: [✓ Tinggi]              │
│                                      │
│  Tenggat: [📅 14 Sep] [🕐 23:59]    │
│  [Hapus Tenggat]                     │
│                                      │
│  [Batal]              [Simpan]       │
└──────────────────────────────────────┘
```

**User bisa:**
- Ubah prioritas dari "Tinggi" ke "Urgent"
- Ubah deadline ke besok
- Update deskripsi
- Tap "Simpan"

---

### **Skenario 7: Delete Task**

**Aksi:** User expand task → tap 🗑️ Delete

**Dialog Konfirmasi:**
```
┌──────────────────────────────────────┐
│  ❌ Hapus Task?                      │
├──────────────────────────────────────┤
│  Apakah Anda yakin ingin menghapus   │
│  task 'Beli Protein Powder dan       │
│  Suplemen'?                          │
│                                      │
│         [Batal]     [Hapus]          │
└──────────────────────────────────────┘
```

**Hasil jika tap "Hapus":**
- Task hilang dari list dengan animasi
- Snackbar: "Task dihapus"
- Statistik update

---

### **Skenario 8: Empty State - Tidak Ada Task**

**Kondisi:** User baru pertama kali atau sudah hapus semua task

**Yang Terlihat:**
```
┌─────────────────────────────────────────┐
│  📊 Statistik                           │
│     0        0        0                 │
│   Aktif   Selesai  Total                │
├─────────────────────────────────────────┤
│                                         │
│            🎯                           │
│       Belum Ada Task                    │
│                                         │
│  Mulai tambahkan task pertama Anda      │
│  untuk mengelola aktivitas dengan       │
│  lebih baik!                            │
│                                         │
│        [+ Buat Task]                    │
│                                         │
└─────────────────────────────────────────┘
```

---

### **Skenario 9: Empty State - Semua Task Selesai**

**Kondisi:** User filter "Aktif" tapi semua task sudah done

**Yang Terlihat:**
```
┌─────────────────────────────────────────┐
│  Filter: [✓ Aktif]                      │
│                                         │
│            ✅                           │
│     Tidak Ada Task Aktif                │
│                                         │
│  Semua task Anda sudah selesai.         │
│  Mantap! 🎉                             │
│                                         │
└─────────────────────────────────────────┘
```

---

## 🎨 Color Legend & Visual Indicators

### Priority Colors:
- **🔴 Urgent/Tinggi** - Red (#F44336, #D32F2F)
- **🟠 Sedang** - Orange (#FF9800)
- **🟢 Rendah** - Green (#4CAF50)

### Status Indicators:
- **⭕ Unchecked** - Gray circle
- **✅ Checked** - Green circle with checkmark
- **⚠️ Overdue** - Red warning triangle
- **⏰ Due Soon** - Clock icon

### Card States:
- **Normal** - Dark card background
- **Completed** - Darker background + strikethrough text
- **Overdue** - Red border (2dp)
- **Expanded** - Show full description + actions

---

## 📊 Real-World Use Cases

### Use Case 1: Gym Enthusiast
```
✅ Beli Protein Powder        [Belanja, Tinggi]
⭕ Meal Prep Minggu Ini       [Kesehatan, Sedang] 
⭕ Book Personal Trainer      [Latihan, Urgent]
⭕ Research Creatine Brands   [Belajar, Rendah]
```

### Use Case 2: Busy Professional
```
⭕ Submit Q1 Report           [Kerja, Tinggi]
⭕ Evening Workout            [Latihan, Sedang]
⭕ Buy Gym Clothes            [Belanja, Rendah]
✅ Morning Cardio             [Latihan, Sedang]
```

### Use Case 3: Fitness Journey Tracker
```
⭕ Take Progress Photos       [Pribadi, Sedang]
⭕ Update Weight Log          [Kesehatan, Rendah]
⭕ Plan Next Week Workouts    [Latihan, Tinggi]
⭕ Read Nutrition Article     [Belajar, Sedang]
```

---

## 🎯 Key Features Demonstrated

✅ **Visual Priority System** - Color-coded badges  
✅ **Smart Sorting** - Active first, by priority, by date  
✅ **Overdue Detection** - Auto-highlight late tasks  
✅ **Category System** - 7 preset categories  
✅ **Expandable Cards** - Tap to see details  
✅ **Quick Actions** - Check, Edit, Delete  
✅ **Filter System** - All, Active, Completed  
✅ **Statistics** - Real-time task counts  
✅ **Empty States** - Helpful UI when no tasks  
✅ **Date & Time Picker** - Full deadline support  
✅ **Snackbar Feedback** - Confirm every action  
✅ **Dark Theme** - Consistent with app theme  
✅ **Indonesian UI** - All text in Bahasa  

---

## 🚀 Navigation Flow

```
Main App
├── Tab 1: Jadwal (Workout Reminders)
├── Tab 2: Aktivitas (Achievements & Streak)
├── Tab 3: Tasks ⭐ (NEW!)
│   ├── View All Tasks
│   ├── Filter Tasks
│   ├── Add New Task
│   ├── Edit Task
│   ├── Delete Task
│   └── Mark Complete/Incomplete
└── Tab 4: Profil (User Profile & Body Metrics)
```

---

## 🎬 Animasi & Interaksi

1. **Card Expand/Collapse** - Smooth fade + expand animation
2. **Task Complete** - Checkbox animation, text fade, strikethrough
3. **Filter Switch** - Instant list update
4. **Add Task** - Dialog slide up from bottom
5. **Delete Task** - Card fade out
6. **Snackbar** - Slide up from bottom, auto-dismiss

---

## 💡 Tips Penggunaan

1. **Prioritas Warna:**
   - Merah = Harus segera dikerjakan
   - Orange = Penting tapi tidak urgent
   - Hijau = Bisa dikerjakan kapan saja

2. **Kategori:**
   - Gunakan "Latihan" untuk workout-related tasks
   - "Kesehatan" untuk nutrition/body tracking
   - "Belanja" untuk gym equipment/suplemen

3. **Deadline:**
   - Set deadline untuk task yang time-sensitive
   - Skip deadline untuk task yang flexible

4. **Filter:**
   - "Aktif" untuk fokus ke task yang belum selesai
   - "Selesai" untuk review achievement
   - "Semua" untuk overview lengkap

---

## 🔥 Integration dengan Fitur Existing

Task Management terintegrasi dengan:
- ✅ **Dark Theme** - Gunakan color palette yang sama
- ✅ **Database** - Disimpan di Room Database lokal
- ✅ **Navigation** - Bottom nav bar 4 tabs
- ✅ **Typography** - Material Typography konsisten
- ✅ **Spacing** - Padding dan margin seragam

**Tidak mengganggu:**
- ❌ Workout reminders tetap berfungsi normal
- ❌ Alarm system tidak terpengaruh
- ❌ Profile dan activity screen tetap sama

---

## 📱 Cara Testing Manual

1. Build & install aplikasi
2. Buka tab "Tasks"
3. Tap FAB (+) untuk buat task pertama
4. Isi form, tap Simpan
5. Tap card untuk expand detail
6. Tap checkbox untuk mark done
7. Coba semua filter (All, Active, Completed)
8. Edit dan delete task
9. Coba buat task dengan berbagai prioritas dan kategori
10. Test overdue dengan set deadline ke tanggal lalu (manual edit di code)

---

**Status:** ✅ Siap digunakan! Build dan install ke device/emulator untuk melihat fitur lengkap bekerja.
