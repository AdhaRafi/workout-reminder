package com.workoutreminder.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.workoutreminder.app.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    companion object {
        private const val PREF_NAME = "workout_user_profile"
        private const val KEY_NAME = "key_name"
        private const val KEY_AVATAR = "key_avatar"
        private const val KEY_PHOTO_URI = "key_photo_uri"
        private const val KEY_BIO = "key_bio"
        private const val KEY_FITNESS_LEVEL = "key_fitness_level"
        private const val KEY_FAVORITE_CATEGORY = "key_favorite_category"
        private const val KEY_GENDER = "key_gender"
        private const val KEY_AGE = "key_age"
        private const val KEY_HEIGHT = "key_height"
        private const val KEY_WEIGHT = "key_weight"
        private const val KEY_TARGET_WORKOUTS = "key_target_workouts"
        private const val KEY_FITNESS_GOAL = "key_fitness_goal"
    }

    private fun loadProfile(): UserProfile {
        return UserProfile(
            name = prefs.getString(KEY_NAME, "Sobat Bugar") ?: "Sobat Bugar",
            avatar = prefs.getString(KEY_AVATAR, "🦁") ?: "🦁",
            photoUri = prefs.getString(KEY_PHOTO_URI, null),
            bio = prefs.getString(KEY_BIO, "Konsisten latihan untuk tubuh sehat & bugar! 💪")
                ?: "Konsisten latihan untuk tubuh sehat & bugar! 💪",
            fitnessLevel = prefs.getString(KEY_FITNESS_LEVEL, "Pemula") ?: "Pemula",
            favoriteCategory = prefs.getString(KEY_FAVORITE_CATEGORY, "Punggung (Back)")
                ?: "Punggung (Back)",
            gender = prefs.getString(KEY_GENDER, "Pria") ?: "Pria",
            age = prefs.getInt(KEY_AGE, 24),
            heightCm = prefs.getFloat(KEY_HEIGHT, 172.0f),
            weightKg = prefs.getFloat(KEY_WEIGHT, 68.0f),
            targetWorkoutsPerWeek = prefs.getInt(KEY_TARGET_WORKOUTS, 4),
            fitnessGoal = prefs.getString(KEY_FITNESS_GOAL, "Bulking / Tambah Otot")
                ?: "Bulking / Tambah Otot"
        )
    }

    fun saveProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString(KEY_NAME, profile.name)
            putString(KEY_AVATAR, profile.avatar)
            putString(KEY_PHOTO_URI, profile.photoUri)
            putString(KEY_BIO, profile.bio)
            putString(KEY_FITNESS_LEVEL, profile.fitnessLevel)
            putString(KEY_FAVORITE_CATEGORY, profile.favoriteCategory)
            putString(KEY_GENDER, profile.gender)
            putInt(KEY_AGE, profile.age)
            putFloat(KEY_HEIGHT, profile.heightCm)
            putFloat(KEY_WEIGHT, profile.weightKg)
            putInt(KEY_TARGET_WORKOUTS, profile.targetWorkoutsPerWeek)
            putString(KEY_FITNESS_GOAL, profile.fitnessGoal)
            apply()
        }
        _userProfile.value = profile
    }
}
