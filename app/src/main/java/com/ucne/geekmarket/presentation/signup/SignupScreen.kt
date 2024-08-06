package com.ucne.geekmarket.presentation.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.Color
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SignupScreen(
    goToLogin: () -> Unit,
    authViewModel: SignupViewModel = hiltViewModel()
) {

    val uiState by authViewModel.uiState.collectAsState()
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> goToLogin()
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    SingupScreen(
        uiState = uiState,
        authState = authState,
        goToLogin = goToLogin,
        onEmailChanged = authViewModel::onEmailChanged,
        onPasswordChanged = authViewModel::onPasswordChanged,
        singout = authViewModel::signup,
        onNombreChanged = authViewModel::onNombreChanged,
        onApellidoChanged = authViewModel::onApellidoChanged,
        onFechaChaged = authViewModel::onFechaChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingupScreen(
    uiState: LoginUistate,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNombreChanged: (String) -> Unit,
    onApellidoChanged: (String) -> Unit,
    onFechaChaged: (String) -> Unit,
    authState: State<AuthState?>,
    singout: () -> Unit,
    goToLogin: () -> Unit,
) {
    var showPassword by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val unDia = 86400000
    val state = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis() - unDia
        }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(id = R.drawable.g_triangulo_vector),
            contentDescription = "Email Icon",
            modifier = Modifier.size(150.dp)
        )

        Text(text = "Signup", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nombre ?: "",
            onValueChange = onNombreChanged,
            label = {
                Text(text = "Nombre")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Email Icon"
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.apellido ?: "",
            onValueChange = onApellidoChanged,
            label = {
                Text(text = "Apellido")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Email Icon"
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            label = { Text(text = "Fecha") },
            value = uiState.fechaNacimiento.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
            onValueChange = onFechaChaged,
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                IconButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Picker"
                    )
                }
            },
            modifier = Modifier.clickable(enabled = true) {
                showDatePicker = true
            },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))


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
            onClick = {
                singout()
            }, enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Create account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            goToLogin()
        }) {
            Text(text = "Already have an account, Login")
        }
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            onFechaChaged(
                                state.selectedDateMillis?.let {
                                    Instant.ofEpochMilli(it).atZone(
                                        ZoneId.of("UTC")
                                    ).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                                }.toString()
                            )
                            showDatePicker = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Aceptar")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDatePicker = false }) {
                        Text(text = "Cancelar")
                    }
                },
            )
            {
                DatePicker(state)
            }
        }
    }
}