package com.team.moblocation.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.moblocation.core.data.model.Ppl
import com.team.moblocation.core.data.model.Registration
import com.team.moblocation.core.data.repo.PplRepo
import com.team.moblocation.service.AuthService
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService = AuthService.getInstance(),
    private val pplRepo: PplRepo = PplRepo.getInstance()
): ViewModel() {

    fun register(
        registration: Registration,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Create Firebase Auth account
                authService.registerWithEmail(
                    email = registration.email,
                    password = password
                )

                val user = authService.getCurrentUser()
                    ?: throw IllegalStateException("Authentication failed")

                val ppl = Ppl(
                    uid = user.id,
                    fullName = registration.name,
                    email = registration.email,
                )

                pplRepo.createPpl(ppl)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Registration failed")
            }
        }
    }
}