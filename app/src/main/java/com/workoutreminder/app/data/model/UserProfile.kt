package com.workoutreminder.app.data.model

import java.util.Locale
import kotlin.math.roundToInt

data class UserProfile(
    val name: String = "Sobat Bugar",
    val avatar: String = "🦁",
    val photoUri: String? = null,
    val bio: String = "Konsisten latihan untuk tubuh sehat & bugar! 💪",
    val fitnessLevel: String = "Pemula",
    val favoriteCategory: String = "Punggung (Back)",
    val gender: String = "Pria",
    val age: Int = 24,
    val heightCm: Float = 172.0f,
    val weightKg: Float = 68.0f,
    val targetWorkoutsPerWeek: Int = 4,
    val fitnessGoal: String = "Bulking / Tambah Otot"
) {
    val bmi: Float
        get() {
            val heightM = heightCm / 100f
            if (heightM <= 0f) return 0f
            val score = weightKg / (heightM * heightM)
            return (score * 10).roundToInt() / 10f
        }

    val bmiCategory: String
        get() = when {
            bmi < 18.5f -> "Kurus (Underweight)"
            bmi in 18.5f..24.9f -> "Ideal (Normal)"
            bmi in 25.0f..29.9f -> "Kelebihan Berat"
            else -> "Obesitas"
        }

    val bmiCategoryColor: Long
        get() = when {
            bmi < 18.5f -> 0xFF38BDF8 // Sky Blue
            bmi in 18.5f..24.9f -> 0xFF2ECC71 // Active Green
            bmi in 25.0f..29.9f -> 0xFFFF9800 // Warm Orange
            else -> 0xFFEF4444 // Error Red
        }

    val idealWeightMin: Float
        get() {
            val h = heightCm / 100f
            return ((18.5f * h * h) * 10).roundToInt() / 10f
        }

    val idealWeightMax: Float
        get() {
            val h = heightCm / 100f
            return ((24.9f * h * h) * 10).roundToInt() / 10f
        }

    val dailyWaterMl: Int
        get() = (weightKg * 35).roundToInt()

    val dailyWaterLiters: String
        get() = String.format(Locale.US, "%.1f", dailyWaterMl / 1000f)

    val estimatedBmrCalories: Int
        get() {
            val bmr = if (gender == "Wanita") {
                447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * age)
            } else {
                88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * age)
            }
            return bmr.roundToInt()
        }
}
