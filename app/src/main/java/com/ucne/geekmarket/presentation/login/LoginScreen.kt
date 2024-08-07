package com.ucne.geekmarket.presentation.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucne.geekmarket.R
import com.ucne.geekmarket.presentation.Common.AuthState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    goToSignup: () -> Unit,
    goToProductList: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val authState = viewModel.authState.observeAsState()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                viewModel.cambiarPersonaId()
                goToProductList()
                activity.finish()
                activity.startActivity(activity.intent)
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    LoginScreenBody(
        modifier = modifier,
        uiState = uiState,
        authState = authState,
        goToSignup = goToSignup,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        login = viewModel::login
    )

}

@Composable
private fun LoginScreenBody(
    modifier: Modifier,
    uiState: LoginUistate,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    login: () -> Unit,
    authState: State<AuthState?>,
    goToSignup: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(id = R.drawable.g_triangulo_vector),
            contentDescription = "Email Icon",
            modifier = Modifier.size(150.dp)
        )

        Text(text = "Login", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.email ?: "",
            onValueChange = onEmailChanged,
            label = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password ?: "",
            onValueChange = onPasswordChanged,
            label = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Email Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    if (showPassword) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "Password Icon"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = "Password Icon"
                        )
                    }
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = login,
            enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = goToSignup) {
            Text(text = "Don't have an account, Signup")
        }
    }
}