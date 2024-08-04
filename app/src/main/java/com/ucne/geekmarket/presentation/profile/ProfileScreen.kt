package com.ucne.geekmarket.presentation.profile

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucne.geekmarket.R
import com.ucne.geekmarket.presentation.Common.AuthState


@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    goToLogin: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as Activity
    val uiState by viewModel.uiState.collectAsState()
    val authState = viewModel.authState.observeAsState()
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
    ) {
        ProfileCard(user = uiState.email ?: "Email")

        ListContentAbout()

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (authState.value is AuthState.Authenticated) {
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(48.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        viewModel.signout()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "",
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Cerrar sesión",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            } else if (authState.value is AuthState.Unauthenticated) {
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(48.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = goToLogin
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "",
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Iniciar sesión",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

        }
    }
}






@Composable
fun ProfileCard(
    user: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            border = BorderStroke(width = 2.dp, color = Black),
            shape = RoundedCornerShape(40.dp),
        ) {
            Image(
                modifier = Modifier.height(80.dp),
                painter = painterResource(id = R.drawable.g_vector),
                contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = "Nombre de perfil",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = user,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
fun ListContentAbout(
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Opciones(
                image = R.drawable.pixel_heart_icon_fill,
                title = "Editar perfil"
            )
        }
        item {
            Opciones(
                image = R.drawable.pixel_heart_icon_fill,
                title = "CartShopping"
            )
        }
        item {
            Opciones(
                image = R.drawable.pixel_heart_icon_fill,
                title = "WishList"
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
}

@Composable
fun Opciones(
    image: Int,
    title: String,
) {
    Column {
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = image),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .weight(1f),
                text = title,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = ""
            )
        }
    }
}