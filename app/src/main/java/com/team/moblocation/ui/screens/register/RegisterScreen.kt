package com.team.moblocation.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.moblocation.core.data.model.Registration
import com.team.moblocation.ui.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = viewModel()
    val context = LocalContext.current

    var registration by remember { mutableStateOf(Registration()) }
    var password by remember { mutableStateOf("") }

    fun submitRegistration() {
        viewModel.register(
            registration = registration,
            password = password,
            onSuccess = {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login) { inclusive = true }
                }
            },
            onError = {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Registration",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            BasicInfoPage(
                registration = registration,
                password = password,
                onUpdate = { registration = it },
                onSubmit = { submitRegistration() },
                onPasswordChange = { password = it },
                navController
           )
        }
    }
}

@Composable
fun BasicInfoPage(
    registration: Registration,
    password: String,
    onUpdate: (Registration) -> Unit,
    onSubmit: () -> Unit,
    onPasswordChange: (String) -> Unit,
    navController: NavController
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = registration.name,
            onValueChange = { onUpdate(registration.copy(name = it)) },
            label = { Text("Full Name") }
        )

        OutlinedTextField(
            value = registration.email,
            onValueChange = { onUpdate(registration.copy(email = it)) },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )


        Spacer(Modifier.height(16.dp))
        Button(onClick = onSubmit) { Text("Register") }
    }

    Spacer(Modifier.height(12.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Already have an account?")
        TextButton(
            onClick = { navController.navigate(Screen.Login) }
        ) {
            Text("Login")
        }
    }
}
