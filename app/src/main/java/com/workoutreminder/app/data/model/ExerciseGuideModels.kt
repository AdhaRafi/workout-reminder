package com.workoutreminder.app.data.model

enum class MuscleCategory(
    val displayName: String,
    val subtitle: String,
    val emoji: String,
    val colorHex: Long
) {
    ALL("Semua", "Seluruh Kelompok Otot", "🔥", 0xFFFF5A3C),
    BACK("Punggung", "Lats, Rhomboids & Traps", "🔙", 0xFF38BDF8),
    CHEST("Dada", "Upper & Lower Pectoralis", "🛡️", 0xFFFF7E67),
    LEGS("Kaki", "Quads, Hamstrings & Glutes", "🦵", 0xFF3DDC84),
    SHOULDERS("Bahu", "Front, Side & Rear Deltoids", "🥋", 0xFFA855F7),
    ARMS("Lengan", "Biceps & Triceps", "💪", 0xFFF59E0B),
    CORE("Perut", "Abs & Stabilitas Inti", "⚡", 0xFFEC4899)
}

data class ExerciseItem(
    val id: String,
    val name: String,
    val category: MuscleCategory,
    val difficulty: String = "Pemula",
    val equipment: String = "Dumbbell",
    val primaryMuscles: String,
    val secondaryMuscles: String,
    val shortDescription: String,
    val steps: List<String>,
    val formTips: List<String>,
    val commonMistakes: List<String>,
    val beginnerRecommendation: String,
    val illustrationType: String
)

data class BeginnerLesson(
    val id: String,
    val number: Int,
    val title: String,
    val tag: String,
    val icon: String,
    val summary: String,
    val keyPoints: List<String>,
    val goldenRule: String
)

object ExerciseGuideRepository {

    val beginnerLessons: List<BeginnerLesson> = listOf(
        BeginnerLesson(
            id = "lesson_1",
            number = 1,
            title = "Mindset & Cara Memulai Latihan dari Nol",
            tag = "Pondasi Utama",
            icon = "🧠",
            summary = "Kunci transformasi fisik bukan mengangkat beban terberat di hari pertama, melainkan konsistensi hadir dan membangun kebiasaan tanpa cedera.",
            keyPoints = listOf(
                "Mulai dengan 3 sesi seminggu (durasi 40-50 menit per sesi).",
                "Fokus pelajari postur dan teknik (Form) sebelum mulai menambah beban berat.",
                "Ego lifting (memaksa beban yang terlalu berat) adalah penyebab nomor 1 cedera pada pemula.",
                "Tubuh akan terasa pegal (DOMS) di 1-2 minggu awal, ini normal dan akan mereda seiring adaptasi."
            ),
            goldenRule = "Konsistensi mengalahkan intensitas yang hanya sesekali."
        ),
        BeginnerLesson(
            id = "lesson_2",
            number = 2,
            title = "Pemanasan Dinamis (Warm-Up) Wajib 5-10 Menit",
            tag = "Cegah Cedera",
            icon = "🔥",
            summary = "Jangan pernah langsung mengangkat beban dengan otot yang dingin dan kaku. Pemanasan menaikkan suhu inti tubuh dan melumasi persendian.",
            keyPoints = listOf(
                "Lakukan peregangan dinamis: Arm circles (putaran lengan), Torso twist (putar pinggang), Leg swings (ayunan kaki), dan Jumping jacks.",
                "Lakukan 1-2 set pemanasan (Warm-up sets) dengan beban sangat ringan (hanya 30-40% dari beban kerja biasa) sebelum masuk set utama.",
                "Hindari peregangan statis (menahan posisi diam terlalu lama) SEBELUM latihan karena dapat menurunkan kekuatan ledak otot."
            ),
            goldenRule = "5 menit pemanasan menghindarkanmu dari berbulan-bulan cedera sendi."
        ),
        BeginnerLesson(
            id = "lesson_3",
            number = 3,
            title = "Pahami Repetisi (Reps), Set & Progressive Overload",
            tag = "Konsep Latihan",
            icon = "📊",
            summary = "Repetisi adalah berapa kali gerakan diulang. Set adalah kumpulan dari repetisi tersebut yang diselingi jeda istirahat.",
            keyPoints = listOf(
                "Berapa Repetisi? Untuk pemula yang ingin membentuk otot (Hipertrofi): 8 – 12 repetisi per set adalah rentang ideal.",
                "Berapa Set? Lakukan 3 – 4 set untuk setiap jenis gerakan latihan.",
                "Waktu Istirahat (Rest): Istirahatlah 60 – 90 detik antar set agar otot sempat memulihkan energi ATP.",
                "Progressive Overload: Setelah kamu bisa melakukan 12 repetisi dengan mudah dan form sempurna, barulah naikkan beban sedikit (misal tambah 1-2 kg)."
            ),
            goldenRule = "Targetkan 3 set x 8-12 repetisi dengan beban di mana repetisi ke-10 mulai terasa menantang."
        ),
        BeginnerLesson(
            id = "lesson_4",
            number = 4,
            title = "Teknik Pernapasan yang Benar",
            tag = "Efisiensi Tenaga",
            icon = "🫁",
            summary = "Menahan napas saat angkat beban berbahaya karena bisa menaikkan tekanan darah secara drastis dan membuat pusing berkunang-kunang.",
            keyPoints = listOf(
                "Hembuskan Napas (Exhale / Buang Napas) saat fase paling berat (kontraksi konsentrik), seperti saat mendorong ke atas atau menarik beban mendekat.",
                "Tarik Napas (Inhale) saat fase menurunkan beban secara terkontrol (fase eksentrik).",
                "Kencangkan otot perut (Bracing core) seperti hendak ditinju untuk mengunci tulang belakang agar stabil."
            ),
            goldenRule = "Buang napas saat mendorong/menarik beban, tarik napas saat menurunkan beban."
        ),
        BeginnerLesson(
            id = "lesson_5",
            number = 5,
            title = "Jadwal Latihan 3 Hari Pemula (Full Body Split)",
            tag = "Jadwal Efektif",
            icon = "📅",
            summary = "Pola 3 hari seminggu adalah yang paling terbukti berhasil untuk pemula karena memberi waktu pemulihan 48 jam bagi serat otot untuk tumbuh.",
            keyPoints = listOf(
                "Senin (Sesi A): Dada (Push-up/Bench Press) + Punggung (Lat Pulldown) + Kaki (Squat) + Bahu (Shoulder Press).",
                "Rabu (Sesi B): Punggung (Bent-Over Row) + Dada (Incline Press) + Kaki (Romanian Deadlift/Lunges) + Lengan (Curls).",
                "Jumat (Sesi C): Kaki (Squat) + Bahu (Lateral Raises) + Trisep (Pushdown) + Perut (Plank).",
                "Selasa, Kamis, Sabtu, Minggu: Hari istirahat (Rest day / jalan kaki santai 30 menit)."
            ),
            goldenRule = "Otot tidak tumbuh saat kamu latihan, otot tumbuh saat kamu beristirahat di hari libur."
        ),
        BeginnerLesson(
            id = "lesson_6",
            number = 6,
            title = "Nutrisi, Hidrasi & Waktu Tidur",
            tag = "Pertumbuhan Otot",
            icon = "🥗",
            summary = "Latihan hanyalah pemicu sinyal, makanan bergizi dan tidur adalah bahan baku yang membangun otot dan membakar lemak.",
            keyPoints = listOf(
                "Asupan Protein: Konsumsi protein berkualitas (dada ayam, telur, tempe, tahu, ikan, susu) sekitar 1.4 - 1.8 gram per kg berat badan.",
                "Minum Air Cukup: Minum 2.5 - 3 liter air per hari. Dehidrasi 2% saja sudah menurunkan performa angkat beban hingga 15%.",
                "Tidur Berkualitas: Tidur 7 – 8 jam setiap malam. Hormon pertumbuhan (HGH) dilepaskan maksimal saat fase deep sleep."
            ),
            goldenRule = "Hasil latihan = 50% Latihan yang Benar + 30% Makanan Bergizi + 20% Tidur Cukup."
        )
    )

    val exercises: List<ExerciseItem> = listOf(
        // ==================== PUNGGUNG (BACK) ====================
        ExerciseItem(
            id = "back_1",
            name = "Lat Pulldown",
            category = MuscleCategory.BACK,
            difficulty = "Pemula",
            equipment = "Mesin Kabel",
            primaryMuscles = "Latissimus Dorsi (Sayap)",
            secondaryMuscles = "Biceps, Rhomboids, Rear Delts",
            shortDescription = "Latihan terbaik pemula untuk melebarkan otot punggung dan membentuk siluet V-Taper yang gagah.",
            steps = listOf(
                "Duduk di mesin lat pulldown, atur bantalan paha hingga mengunci kaki dengan kokoh.",
                "Pegang bar dengan pegangan lebih lebar dari bahu, telapak menghadap ke depan.",
                "Tegakkan badan, busungkan dada sedikit, lalu tarik bar ke bawah hingga menyentuh dada bagian atas.",
                "Fokus menarik dengan menggerakkan siku ke bawah dan belakang, rasakan sayap punggung meremas.",
                "Kembalikan bar ke atas secara perlahan dan terkontrol selama 2-3 detik hingga otot lats terentang."
            ),
            formTips = listOf(
                "Jangan mengayunkan badan terlalu jauh ke belakang (maksimal condong 15 derajat).",
                "Kunci bahu ke bawah (depress scapula) sebelum mulai menarik.",
                "Gunakan 'Hook Grip' atau bayangkan tanganmu hanyalah kait, tarik menggunakan siku."
            ),
            commonMistakes = listOf(
                "Menarik bar ke belakang leher (sangat berbahaya bagi sendi bahu).",
                "Menggunakan momentum tubuh untuk mengayun beban berat.",
                "Melepas beban ke atas terlalu cepat tanpa kendali."
            ),
            beginnerRecommendation = "3 Set × 10 - 12 Repetisi • Beban Ringan-Sedang • Istirahat 60-90 detik",
            illustrationType = "lat_pulldown"
        ),
        ExerciseItem(
            id = "back_2",
            name = "Dumbbell Bent-Over Row",
            category = MuscleCategory.BACK,
            difficulty = "Pemula",
            equipment = "Dumbbell & Bangku",
            primaryMuscles = "Rhomboids & Mid-Back (Punggung Tengah)",
            secondaryMuscles = "Latissimus, Biceps, Core",
            shortDescription = "Latihan tebal punggung dengan satu tangan di bangku untuk menopang pinggang agar aman dari cedera.",
            steps = listOf(
                "Letakkan lutut dan tangan kiri di bangku datar sebagai tumpuan yang kokoh.",
                "Pegang dumbbell di tangan kanan, biarkan lengan menjuntai lurus dengan telapak menghadap tubuh.",
                "Jaga tulang punggung tetap lurus datar dan kunci otot perut.",
                "Tarik dumbbell ke atas ke arah pinggul (bukan ke dada) dengan mengarahkan siku ke langit-langit.",
                "Tahan kontraksi di puncak selama 1 detik, lalu turunkan perlahan ke posisi semula."
            ),
            formTips = listOf(
                "Jaga punggung tetap lurus sejajar lantai, jangan biarkan bahu turun miring.",
                "Tarik beban menuju saku celana belakang untuk aktivasi lats maksimal."
            ),
            commonMistakes = listOf(
                "Memutar tubuh bagian atas untuk mengangkat beban berat.",
                "Punggung membungkuk membulat yang membebani tulang pinggang."
            ),
            beginnerRecommendation = "3 Set × 10 Repetisi per sisi • Istirahat 60 detik",
            illustrationType = "dumbbell_row"
        ),
        ExerciseItem(
            id = "back_3",
            name = "Seated Cable Row",
            category = MuscleCategory.BACK,
            difficulty = "Pemula",
            equipment = "Mesin Kabel",
            primaryMuscles = "Rhomboids & Trapezius Tengah",
            secondaryMuscles = "Latissimus Dorsi, Biceps, Forearms",
            shortDescription = "Membangun ketebalan punggung dan memperbaiki postur tubuh agar tegak dan tidak bungkuk.",
            steps = listOf(
                "Duduk di mesin cable row, letakkan kaki pada pijakan dengan lutut sedikit ditekuk.",
                "Raih pegangan V-handle, dorong badan ke belakang hingga punggung tegak 90 derajat.",
                "Busungkan dada, tarik pegangan ke arah pusar sambil merapatkan kedua tulang belikat.",
                "Tahan 1 detik saat pegangan mendekati perut.",
                "Lepas kembali lengan ke depan secara perlahan tanpa membiarkan punggung membungkuk."
            ),
            formTips = listOf(
                "Jangan biarkan lutut terkunci lurus kaku.",
                "Fokus rapatkan kedua belikat di belakang seperti sedang menjepit pensil di antara tulang punggung."
            ),
            commonMistakes = listOf(
                "Mengayunkan pinggang maju mundur terlalu ekstrem.",
                "Mengangkat bahu ke arah telinga (shrugging) saat menarik."
            ),
            beginnerRecommendation = "3 Set × 12 Repetisi • Fokus kontraksi lambat",
            illustrationType = "cable_row"
        ),
        ExerciseItem(
            id = "back_4",
            name = "Back Hyperextension",
            category = MuscleCategory.BACK,
            difficulty = "Pemula",
            equipment = "Bangku Roman / Hyperextension",
            primaryMuscles = "Erector Spinae (Punggung Bawah)",
            secondaryMuscles = "Glutes, Hamstrings",
            shortDescription = "Memperkuat otot punggung bawah sebagai perisai pelindung tulang belakang dan anti sakit pinggang.",
            steps = listOf(
                "Posisikan tubuh tengkurap pada bangku hyperextension dengan bantalan berada tepat di bawah pinggul.",
                "Kaitkan pergelangan kaki di bawah bantalan penahan.",
                "Silangkan kedua tangan di depan dada atau letakkan di pelipis.",
                "Turunkan badan ke bawah dengan menekuk pinggul hingga sudut sekitar 60-70 derajat.",
                "Angkat tubuh kembali ke atas menggunakan otot punggung bawah dan pantat hingga lurus sejajar kaki."
            ),
            formTips = listOf(
                "Gerakan harus mengalir halus dan terkontrol, jangan disentakkan.",
                "Cukup angkat sampai tubuh membentuk garis lurus, jangan melengkung ke belakang berlebihan (hyperextend)."
            ),
            commonMistakes = listOf(
                "Melentingkan badan terlalu tinggi ke belakang yang menjepit bantalan tulang sendi.",
                "Menggunakan beban tambahan saat teknik dasar belum dikuasai."
            ),
            beginnerRecommendation = "3 Set × 10 - 15 Repetisi • Gunakan berat tubuh sendiri",
            illustrationType = "hyperextension"
        ),

        // ==================== DADA (CHEST) ====================
        ExerciseItem(
            id = "chest_1",
            name = "Push-Up Standar",
            category = MuscleCategory.CHEST,
            difficulty = "Pemula",
            equipment = "Bodyweight (Tanpa Alat)",
            primaryMuscles = "Pectoralis Major (Dada)",
            secondaryMuscles = "Triceps, Bahu Depan, Core",
            shortDescription = "Raja gerakan pembentuk otot dada tanpa perlu ke gym. Melatih dorongan dan stabilitas seluruh tubuh.",
            steps = listOf(
                "Ambil posisi plank dengan tangan diletakkan sedikit lebih lebar dari bahu.",
                "Kencangkan perut dan pantat sehingga tubuh membentuk garis lurus dari kepala hingga tumit.",
                "Turunkan badan secara perlahan dengan menekuk siku hingga dada hampir menyentuh lantai (1-2 cm).",
                "Jaga sudut siku sekitar 45 derajat dari badan (berbentuk seperti mata panah, bukan huruf T).",
                "Dorong lantai kuat-kuat hingga lengan kembali lurus ke posisi awal."
            ),
            formTips = listOf(
                "Jika belum kuat push-up di lantai, lakukan Incline Push-Up (tangan bertumpu di bangku/meja) atau Knee Push-Up (bertumpu pada lutut).",
                "Jaga leher tetap netral menatap lantai 30 cm di depan tangan."
            ),
            commonMistakes = listOf(
                "Pinggul melorot ke bawah atau pantat terangkat terlalu tinggi.",
                "Siku melebar 90 derajat keluar yang dapat menekan sendi bahu."
            ),
            beginnerRecommendation = "3 Set × 8 - 12 Repetisi (atau variasi knee push-up)",
            illustrationType = "pushup"
        ),
        ExerciseItem(
            id = "chest_2",
            name = "Dumbbell Flat Bench Press",
            category = MuscleCategory.CHEST,
            difficulty = "Pemula",
            equipment = "Dumbbell & Bangku Datar",
            primaryMuscles = "Pectoralis Major (Dada Tengah)",
            secondaryMuscles = "Triceps, Bahu Depan (Anterior Deltoid)",
            shortDescription = "Latihan kompon utama untuk membangun ketebalan dan massa otot dada dengan rentang gerak bebas.",
            steps = listOf(
                "Berbaring di bangku datar dengan kedua kaki menapak kuat di lantai.",
                "Pegang dumbbell di samping dada dengan telapak tangan menghadap ke depan dan siku membentuk sudut 75 derajat.",
                "Kunci belikat bahu ke belakang dan busungkan dada ke atas.",
                "Dorong dumbbell ke atas lurus di atas dada tengah hingga lengan hampir lurus (jangan benturkan kedua dumbbell).",
                "Turunkan kembali secara perlahan dalam 2-3 detik hingga merasakan peregangan nyaman pada dada."
            ),
            formTips = listOf(
                "Selalu jaga kedua telapak kaki menapak kokoh di lantai untuk menjaga stabilitas.",
                "Pertahankan sedikit lengkungan alami pada punggung bawah (bukan rata mendatar kaku)."
            ),
            commonMistakes = listOf(
                "Mengunci siku secara kasar (lock-out) di puncak dorongan.",
                "Menurunkan beban terlalu cepat tanpa menahannya."
            ),
            beginnerRecommendation = "3 Set × 8 - 10 Repetisi • Beban yang bisa dikontrol penuh",
            illustrationType = "bench_press"
        ),
        ExerciseItem(
            id = "chest_3",
            name = "Incline Dumbbell Press",
            category = MuscleCategory.CHEST,
            difficulty = "Pemula",
            equipment = "Dumbbell & Bangku Miring",
            primaryMuscles = "Upper Chest (Dada Bagian Atas)",
            secondaryMuscles = "Bahu Depan, Triceps",
            shortDescription = "Membentuk dada bagian atas agar penuh dan menyatu rapi dengan tulang selangka (collarbone).",
            steps = listOf(
                "Atur kemiringan bangku pada sudut 30 hingga 45 derajat (jangan terlalu tegak agar tidak menjadi latihan bahu).",
                "Duduk dan posisikan dumbbell di paha, lalu dorong naik ke posisi awal di samping dada atas.",
                "Dorong beban ke atas secara simultan hingga lengan lurus di atas dada atas.",
                "Tahan 1 detik di atas, lalu turunkan terkontrol sampai sejajar dada atas."
            ),
            formTips = listOf(
                "Sudut bangku 30 derajat adalah titik manis (sweet spot) aktivasi dada atas maksimal.",
                "Pikirkan untuk merapatkan siku bagian dalam saat mendorong ke atas."
            ),
            commonMistakes = listOf(
                "Kemiringan bangku terlalu tinggi (di atas 60 derajat) sehingga bahu mendominasi gerakan.",
                "Mengangkat pantat lepas dari dudukan bangku."
            ),
            beginnerRecommendation = "3 Set × 10 Repetisi • Istirahat 90 detik",
            illustrationType = "incline_press"
        ),

        // ==================== KAKI (LEGS) ====================
        ExerciseItem(
            id = "leg_1",
            name = "Goblet Squat",
            category = MuscleCategory.LEGS,
            difficulty = "Pemula",
            equipment = "Dumbbell / Kettlebell",
            primaryMuscles = "Quadriceps (Paha Depan) & Glutes (Pantat)",
            secondaryMuscles = "Hamstrings, Calves, Core",
            shortDescription = "Variasi squat terbaik untuk pemula karena beban di depan dada otomatis menjaga postur punggung tetap tegak.",
            steps = listOf(
                "Berdiri tegak dengan kaki dibuka sedikit lebih lebar dari bahu, ujung jari kaki menyerong keluar 15-30 derajat.",
                "Pegang satu dumbbell secara vertikal di depan dada dengan kedua tangan merangkul ujung dumbbell.",
                "Tarik napas, dorong pinggul ke belakang lalu tekuk lutut seolah hendak duduk di kursi rendah.",
                "Turun hingga paha minimal sejajar dengan lantai sambil menjaga dada tetap tegak terbuka.",
                "Dorong lantai melalui tumit dan telapak kaki tengah untuk kembali berdiri tegak ke posisi awal."
            ),
            formTips = listOf(
                "Pastikan lutut bergerak searah dengan jari kaki saat turun (jangan biarkan lutut terlipat ke dalam).",
                "Jaga beban dumbbell tetap menempel dekat di depan dada."
            ),
            commonMistakes = listOf(
                "Tumit terangkat dari lantai saat berjongkok.",
                "Punggung membungkuk ke depan karena dada tidak dibusungkan."
            ),
            beginnerRecommendation = "3 Set × 10 - 12 Repetisi • Fokus pada kedalaman squat yang nyaman",
            illustrationType = "squat"
        ),
        ExerciseItem(
            id = "leg_2",
            name = "Romanian Deadlift (RDL)",
            category = MuscleCategory.LEGS,
            difficulty = "Menengah",
            equipment = "Dumbbell / Barbell",
            primaryMuscles = "Hamstrings (Paha Belakang) & Glutes",
            secondaryMuscles = "Lower Back, Forearms, Core",
            shortDescription = "Latihan kunci pembentuk paha belakang dan postur atletis dengan gerakan engsel pinggul (Hip Hinge).",
            steps = listOf(
                "Berdiri tegak selebar pinggul dengan memegang sepasang dumbbell di depan paha.",
                "Tekuk lutut sedikit (soft knee) dan kunci sudut lutut ini sepanjang gerakan.",
                "Dorong pinggul ke belakang sejauh mungkin sambil menurunkan dumbbell meluncur dekat di depan tulang kering.",
                "Berhenti saat merasakan peregangan kuat pada paha belakang (biasanya tepat di bawah lutut).",
                "Dorong pinggul kembali ke depan dan kencangkan otot pantat (glutes) untuk berdiri tegak."
            ),
            formTips = listOf(
                "Ini adalah gerakan mendorong pinggul ke belakang (Hip Hinge), bukan gerakan jongkok (Squat).",
                "Jaga dumbbell selalu menempel atau sangat dekat dengan paha dan tulang kering."
            ),
            commonMistakes = listOf(
                "Membungkukkan punggung untuk meraih lantai lebih rendah.",
                "Menekuk lutut berlebihan hingga menjadi squat."
            ),
            beginnerRecommendation = "3 Set × 8 - 10 Repetisi • Gunakan beban ringan untuk melatih engsel pinggul",
            illustrationType = "rdl"
        ),
        ExerciseItem(
            id = "leg_3",
            name = "Walking Lunges",
            category = MuscleCategory.LEGS,
            difficulty = "Pemula",
            equipment = "Bodyweight / Dumbbell",
            primaryMuscles = "Quadriceps & Glutes",
            secondaryMuscles = "Hamstrings, Calves, Keseimbangan",
            shortDescription = "Melatih kekuatan satu kaki secara mandiri, memperbaiki ketidakseimbangan otot paha kiri dan kanan.",
            steps = listOf(
                "Berdiri tegak dengan tangan di pinggang (atau memegang dumbbell ringan di samping).",
                "Langkahkan kaki kanan jauh ke depan dan turunkan pinggul.",
                "Lutut depan ditekuk membentuk sudut 90 derajat, lutut belakang mendekati lantai (tanpa membentur).",
                "Dorong dengan kaki depan untuk melangkah maju dan ulangi dengan kaki kiri.",
                "Lanjutkan bergantian secara stabil dan terkendali."
            ),
            formTips = listOf(
                "Jaga tubuh bagian atas tetap tegak, jangan condong ke depan berlebihan.",
                "Pastikan langkah cukup panjang agar lutut depan tidak melewati jauh dari jari kaki."
            ),
            commonMistakes = listOf(
                "Kehilangan keseimbangan karena melangkah dalam satu garis lurus seperti tali akrobat (buka selebar pinggul).",
                "Membenturkan lutut belakang ke lantai dengan keras."
            ),
            beginnerRecommendation = "3 Set × 10 Langkah per kaki • Istirahat 60 detik",
            illustrationType = "lunges"
        ),

        // ==================== BAHU (SHOULDERS) ====================
        ExerciseItem(
            id = "shoulder_1",
            name = "Dumbbell Overhead Shoulder Press",
            category = MuscleCategory.SHOULDERS,
            difficulty = "Pemula",
            equipment = "Dumbbell & Bangku Tegak",
            primaryMuscles = "Anterior & Lateral Deltoid (Bahu Depan & Samping)",
            secondaryMuscles = "Triceps, Upper Chest, Traps",
            shortDescription = "Latihan paling fundamental untuk membangun bahu yang lebar, tebal, dan kekuatan dorong vertikal.",
            steps = listOf(
                "Duduk di bangku dengan sandaran tegak 90 derajat, telapak kaki menapak lantai.",
                "Pegang dumbbell setinggi telinga dengan siku berada sedikit di depan bidang tubuh (sekitar 60 derajat).",
                "Dorong dumbbell lurus ke atas hingga lengan hampir lurus di atas kepala.",
                "Tahan sejenak di atas tanpa membenturkan kedua dumbbell.",
                "Turunkan perlahan selama 2 detik kembali ke level telinga/bahu."
            ),
            formTips = listOf(
                "Jangan biarkan siku melebar lurus 180 derajat ke samping; arahkan sedikit ke depan (scapular plane).",
                "Jaga punggung bawah tetap menempel pada sandaran bangku, jangan melengkungkan pinggang."
            ),
            commonMistakes = listOf(
                "Mendorong beban terlalu ke depan bukan lurus vertikal di atas kepala.",
                "Menggunakan dorongan kaki saat melakukan posisi duduk."
            ),
            beginnerRecommendation = "3 Set × 8 - 10 Repetisi • Istirahat 90 detik",
            illustrationType = "shoulder_press"
        ),
        ExerciseItem(
            id = "shoulder_2",
            name = "Dumbbell Lateral Raises",
            category = MuscleCategory.SHOULDERS,
            difficulty = "Pemula",
            equipment = "Dumbbell Ringan",
            primaryMuscles = "Lateral Deltoid (Bahu Samping)",
            secondaryMuscles = "Trapezius Atas",
            shortDescription = "Kunci utama untuk membuat bahu terlihat lebar dan menciptakan ilusi pinggang yang tampak lebih ramping.",
            steps = listOf(
                "Berdiri tegak dengan kaki selebar pinggul, pegang dumbbell ringan di samping paha.",
                "Tekuk siku sedikit (sekitar 10-15 derajat) dan condongkan badan ke depan sangat sedikit.",
                "Angkat kedua lengan ke samping hingga setinggi bahu dengan memimpin menggunakan siku.",
                "Bayangkan seperti sedang menuang teko air di puncak gerakan (jari kelingking sedikit lebih tinggi dari jempol).",
                "Turunkan perlahan secara terkontrol melawan gravitasi."
            ),
            formTips = listOf(
                "Gunakan beban yang SANGAT RINGAN (misal 2-4 kg untuk pemula). Beban terlalu berat akan dipikul oleh otot leher/traps.",
                "Jangan mengayunkan pinggang untuk mengangkat beban."
            ),
            commonMistakes = listOf(
                "Mengangkat beban melebihi ketinggian bahu yang dapat menyebabkan jepitan sendi (impingement).",
                "Mengayunkan tubuh dari depan ke belakang."
            ),
            beginnerRecommendation = "3 Set × 12 - 15 Repetisi • Fokus pada sensasi panas (burn) di bahu samping",
            illustrationType = "lateral_raise"
        ),
        ExerciseItem(
            id = "shoulder_3",
            name = "Face Pulls",
            category = MuscleCategory.SHOULDERS,
            difficulty = "Pemula",
            equipment = "Mesin Kabel & Rope (Tali)",
            primaryMuscles = "Rear Deltoids (Bahu Belakang) & Rotator Cuff",
            secondaryMuscles = "Rhomboids, Trapezius Tengah",
            shortDescription = "Obat penawar bahu bungkuk akibat sering melihat HP/laptop, serta pelindung utama kesehatan sendi bahu.",
            steps = listOf(
                "Pasang tali (rope) pada mesin kabel setinggi mata atau dahi.",
                "Pegang kedua ujung tali dengan jempol menghadap ke arah tubuhmu.",
                "Mundurlah 1-2 langkah hingga kabel terasa tegang.",
                "Tarik tali langsung ke arah hidung atau dahi sambil membuka kedua siku tinggi ke samping dan belakang.",
                "Di akhir gerakan, putar tangan keluar sehingga posisi tangan berada di samping telinga.",
                "Remas otot bahu belakang selama 1 detik, lalu kembalikan perlahan."
            ),
            formTips = listOf(
                "Jaga siku selalu berada lebih tinggi dari pergelangan tangan sepanjang tarikan.",
                "Kencangkan perut agar badan tidak condong tertarik oleh kabel."
            ),
            commonMistakes = listOf(
                "Menarik tali ke arah leher bawah bukan ke arah dahi.",
                "Menggunakan beban terlalu berat sehingga badan terayun-ayun."
            ),
            beginnerRecommendation = "3 Set × 12 - 15 Repetisi • Sangat baik dilakukan di akhir sesi latihan",
            illustrationType = "face_pull"
        ),

        // ==================== LENGAN (ARMS) ====================
        ExerciseItem(
            id = "arm_1",
            name = "Dumbbell Bicep Curl",
            category = MuscleCategory.ARMS,
            difficulty = "Pemula",
            equipment = "Dumbbell",
            primaryMuscles = "Biceps Brachii (Kepala Panjang & Pendek)",
            secondaryMuscles = "Brachialis, Forearms",
            shortDescription = "Gerakan klasik terpopuler untuk membentuk puncak otot bisep lengan yang padat dan kekar.",
            steps = listOf(
                "Berdiri tegak dengan sepasang dumbbell di samping tubuh, telapak menghadap ke paha.",
                "Kunci siku di samping pinggang (jangan biarkan siku maju mundur).",
                "Angkat dumbbell ke atas sambil memutar pergelangan tangan (supinasi) sehingga telapak menghadap dada di atas.",
                "Remas bisep sekuat tenaga di puncak gerakan selama 1 detik.",
                "Turunkan perlahan selama 2-3 detik ke posisi awal."
            ),
            formTips = listOf(
                "Bayangkan siku tertancap di pinggang; hanya lengan bawah yang boleh bergerak.",
                "Jangan mengayunkan punggung bawah untuk membantu mengangkat beban."
            ),
            commonMistakes = listOf(
                "Memajukan siku ke depan di puncak gerakan yang menghilangkan beban dari bisep.",
                "Menjatuhkan beban ke bawah terlalu cepat tanpa ditahan."
            ),
            beginnerRecommendation = "3 Set × 10 - 12 Repetisi • Istirahat 60 detik",
            illustrationType = "bicep_curl"
        ),
        ExerciseItem(
            id = "arm_2",
            name = "Dumbbell Hammer Curl",
            category = MuscleCategory.ARMS,
            difficulty = "Pemula",
            equipment = "Dumbbell",
            primaryMuscles = "Brachialis & Brachioradialis",
            secondaryMuscles = "Biceps Brachii, Forearm Grip",
            shortDescription = "Menebalkan ukuran lengan dari samping dan memperkuat cengkeraman tangan (grip strength).",
            steps = listOf(
                "Pegang dumbbell dengan posisi telapak tangan saling berhadapan (seperti memegang palu).",
                "Kunci siku di samping pinggang dan tegakkan badan.",
                "Angkat dumbbell lurus ke atas ke arah bahu dengan mempertahankan posisi telapak tetap netral.",
                "Tahan kontraksi di atas selama 1 detik, lalu turunkan perlahan ke samping paha."
            ),
            formTips = listOf(
                "Dapat dilakukan dengan kedua tangan bersamaan atau bergantian kiri dan kanan.",
                "Kencangkan genggaman tangan pada dumbbell untuk aktivasi lengan bawah."
            ),
            commonMistakes = listOf(
                "Mengayunkan dumbbell menggunakan momentum pinggul.",
                "Pergelangan tangan menekuk lunglai saat mengangkat."
            ),
            beginnerRecommendation = "3 Set × 10 - 12 Repetisi • Beban sedikit lebih berat dari bicep curl",
            illustrationType = "hammer_curl"
        ),
        ExerciseItem(
            id = "arm_3",
            name = "Tricep Rope Pushdown",
            category = MuscleCategory.ARMS,
            difficulty = "Pemula",
            equipment = "Mesin Kabel & Rope",
            primaryMuscles = "Triceps Brachii (Kepala Luar & Tengah)",
            secondaryMuscles = "Forearms, Core",
            shortDescription = "Membentuk bagian belakang lengan (trisep mengambil 60% porsi ukuran lengan atasmu!).",
            steps = listOf(
                "Pasang attachment tali pada katrol kabel posisi paling atas.",
                "Pegang kedua ujung tali, condongkan badan sedikit ke depan dan kunci siku di samping rusuk dada.",
                "Dorong tali ke bawah dengan meluruskan siku sepenuhnya.",
                "Di ujung bawah gerakan, pisahkan kedua ujung tali ke samping luar untuk kontraksi trisep maksimal.",
                "Kembalikan tali ke atas secara perlahan hingga membentuk sudut 90 derajat pada siku."
            ),
            formTips = listOf(
                "Hanya lengan bawah yang bergerak naik turun, siku tetap diam terkunci di samping tubuh.",
                "Kunci trisep sejenak saat tangan berada di posisi paling bawah."
            ),
            commonMistakes = listOf(
                "Siku membuka melebar ke luar atau bergerak maju mundur.",
                "Menggunakan berat badan untuk menekan beban ke bawah."
            ),
            beginnerRecommendation = "3 Set × 12 Repetisi • Istirahat 60 detik",
            illustrationType = "tricep_pushdown"
        ),

        // ==================== PERUT & CORE ====================
        ExerciseItem(
            id = "core_1",
            name = "Plank Standar",
            category = MuscleCategory.CORE,
            difficulty = "Pemula",
            equipment = "Bodyweight / Matras",
            primaryMuscles = "Transverse Abdominis (Otot Inti Dalam)",
            secondaryMuscles = "Rectus Abdominis, Glutes, Bahu",
            shortDescription = "Latihan fondasi terbaik untuk membangun korset otot inti yang kokoh dan melindungi tulang belakang.",
            steps = listOf(
                "Tengkurap di lantai, bertumpu pada kedua lengan bawah (forearms) dan jari-jari kaki.",
                "Posisikan siku tepat berada di bawah bahu secara vertikal.",
                "Angkat tubuh hingga membentuk satu garis lurus sempurna dari kepala, punggung, hingga tumit.",
                "Kencangkan otot perut sekuat tenaga seperti hendak menahan benturan di perut.",
                "Kencangkan juga otot pantat (glutes) dan paha depan agar tubuh tidak melorot.",
                "Bernapaslah secara teratur dan tahan posisi selama durasi target."
            ),
            formTips = listOf(
                "Jangan menahan napas; bernapaslah pendek namun teratur.",
                "Bayangkan kamu sedang menarik pusar ke arah tulang belakang."
            ),
            commonMistakes = listOf(
                "Pinggul melorot ke bawah yang menyebabkan nyeri pada punggung bawah.",
                "Pantat terangkat terlalu tinggi membentuk segitiga.",
                "Menundukkan kepala ke bawah menatap kaki (jaga pandangan netral ke lantai di antara tangan)."
            ),
            beginnerRecommendation = "3 Set × Tahan 30 - 45 Detik • Istirahat 45 detik antar set",
            illustrationType = "plank"
        ),
        ExerciseItem(
            id = "core_2",
            name = "Bicycle Crunches",
            category = MuscleCategory.CORE,
            difficulty = "Pemula",
            equipment = "Bodyweight / Matras",
            primaryMuscles = "Rectus Abdominis & Obliques (Perut Samping)",
            secondaryMuscles = "Hip Flexors",
            shortDescription = "Dinobatkan sebagai salah satu latihan perut paling efektif oleh riset ilmiah untuk membakar lemak samping.",
            steps = listOf(
                "Berbaring terlentang di matras dengan tangan diletakkan di samping kepala (jangan menarik leher).",
                "Angkat kedua kaki dari lantai dengan lutut ditekuk membentuk sudut 90 derajat.",
                "Angkat kepala dan tulang belikat sedikit dari lantai.",
                "Putar tubuh bagian atas ke kanan sambil membawa siku kiri mendekati lutut kanan yang ditekuk, sementara kaki kiri diluruskan ke depan.",
                "Lakukan sebaliknya secara bergantian dengan ritme seperti mengayuh sepeda secara terkontrol."
            ),
            formTips = listOf(
                "Fokus pada memutar bahu, bukan hanya menggerakkan siku.",
                "Lakukan dengan tempo lambat dan terkendali, bukan cepat terburu-buru."
            ),
            commonMistakes = listOf(
                "Menarik kepala dan leher dengan tangan (dapat mencederai otot leher).",
                "Mengayuh terlalu cepat tanpa merasakan kontraksi perut."
            ),
            beginnerRecommendation = "3 Set × 15 - 20 Kayuhan total • Istirahat 45 detik",
            illustrationType = "bicycle_crunch"
        ),
        ExerciseItem(
            id = "core_3",
            name = "Lying Leg Raises",
            category = MuscleCategory.CORE,
            difficulty = "Pemula",
            equipment = "Bodyweight / Matras",
            primaryMuscles = "Lower Abs (Perut Bagian Bawah)",
            secondaryMuscles = "Hip Flexors, Core",
            shortDescription = "Menargetkan bagian perut bawah yang biasanya paling sulit dilatih dan sering menjadi tempat timbunan lemak.",
            steps = listOf(
                "Berbaring terlentang dengan kedua tangan diletakkan di samping pinggul atau di bawah pantat untuk menopang pinggang.",
                "Rapatkan kedua kaki dan luruskan ke depan.",
                "Angkat kedua kaki ke atas secara bersamaan hingga membentuk sudut 90 derajat dengan lantai.",
                "Tahan 1 detik di atas sambil mengencangkan perut bawah.",
                "Turunkan kedua kaki secara perlahan hingga hampir menyentuh lantai (sekitar 5 cm di atas lantai) tanpa membiarkan tumit menyentuh matras."
            ),
            formTips = listOf(
                "Pastikan punggung bawah selalu menempel rata pada matras sepanjang gerakan.",
                "Jika terasa terlalu berat atau punggung bawah melengkung, tekuk lutut sedikit (bent-knee raises)."
            ),
            commonMistakes = listOf(
                "Punggung bawah terangkat melengkung dari lantai saat kaki turun.",
                "Mengayunkan kaki menggunakan momentum pinggul."
            ),
            beginnerRecommendation = "3 Set × 10 - 12 Repetisi • Gerakan lambat saat menurunkan kaki",
            illustrationType = "leg_raises"
        )
    )

    fun getExercisesByCategory(category: MuscleCategory): List<ExerciseItem> {
        return if (category == MuscleCategory.ALL) {
            exercises
        } else {
            exercises.filter { it.category == category }
        }
    }
}
