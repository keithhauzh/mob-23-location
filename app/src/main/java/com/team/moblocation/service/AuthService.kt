package com.team.moblocation.service

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.team.moblocation.core.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class AuthService private constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        firebaseAuth.currentUser?.let { updateUser(it) }
    }

    private fun updateUser(firebaseUser: FirebaseUser) {
        _user.update {
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "Unknown",
                email = firebaseUser.email ?: "",
                photoURL = firebaseUser.photoUrl?.toString() ?: ""
            )
        }
    }

    // Google Login
    suspend fun signInWithGoogle(context: Context) {
        try {
            val token = getGoogleCredentialToken(context) ?: return
            val credential = GoogleAuthProvider.getCredential(token, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { updateUser(it) }

        } catch (e: GetCredentialCancellationException) {
            Log.d("AuthService", "Sign in cancelled", e)
        } catch (e: GetCredentialException) {
            Log.d("AuthService", "Google sign in failed", e)
        } catch (e: Exception) {
            Log.d("AuthService", "Sign in failed", e)
        }
    }

    // Email Authentication
    suspend fun registerWithEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser?.let { updateUser(it) }
    }

    suspend fun loginWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser?.let { updateUser(it) }
    }

    // Sign out
    fun signOut() {
        firebaseAuth.signOut()
        _user.value = null
    }

    fun getCurrentUser(): User? = _user.value

    // Token
    private suspend fun getGoogleCredentialToken(context: Context): String? {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(GOOGLE_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            result.credential.data.getString(
                "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
            )
        } catch (e: Exception) {
            Log.d("AuthService", "Token failed", e)
            null
        }
    }

    companion object {
        private const val GOOGLE_CLIENT_ID =
            "324605846990-tltpqd9d6eksf4br3464o7jc17863vhp.apps.googleusercontent.com"

        private var instance: AuthService? = null

        fun getInstance(): AuthService {
            if (instance == null) {
                instance = AuthService()
            }
            return instance!!
        }
    }
}