package com.workoutreminder.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.workoutreminder.app.data.repository.UserProfileManager
import com.workoutreminder.app.data.repository.WorkoutRepository

class WorkoutViewModelFactory(
    private val application: Application,
    private val repository: WorkoutRepository,
    private val userProfileManager: UserProfileManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(application, repository, userProfileManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
