package com.team.moblocation.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.moblocation.core.data.repo.IUserRepo
import com.team.moblocation.service.AuthService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
	private val repo: IUserRepo,
	private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {

	private val _greetings = MutableStateFlow("")
	val greetings: StateFlow<String> = _greetings.asStateFlow()

	fun greet(name: String) {
		viewModelScope.launch {
			delay(1000)
			_greetings.value = "Hello $name"
		}
	}

	fun greetings(): String {
		return "Hello ${fetchUser()}"
	}

	fun fetchUser(): String {
		val user = repo.getUser()
		_greetings.value = "Hello"
		return user
	}

	fun loginWithEmail(
		email: String,
		password: String,
		onError: (String) -> Unit
	) {
		val vRes = validate(email, password)
		if (vRes == null) {
			viewModelScope.launch {
				try {
					authService.loginWithEmail(email, password)
				} catch (e: Exception) {
					onError(e.message ?: "Login failed")
				}
			}
		}
	}

	fun loginWithGoogle(
		context: Context,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				authService.signInWithGoogle(context)
			} catch (e: Exception) {
				onError(e.message ?: "Google sign-in failed")
			}
		}
	}

	fun validate(email: String, password: String): String? {
		return try {
			require(email.isNotBlank() && email == "email@a.com") { "Invalid email" }
			require(password.isNotBlank() && password == "password") { "Invalid password" }
			null
		} catch (e: Exception) {
			e.message.toString()
		}
	}
}
