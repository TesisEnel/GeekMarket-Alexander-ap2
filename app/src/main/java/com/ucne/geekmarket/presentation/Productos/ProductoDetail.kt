@file:OptIn(ExperimentalMaterial3Api::class)

package com.ucne.geekmarket.presentation.Productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage


@Composable
fun ProductDetailed(
    viewModel: ProductoViewModel = hiltViewModel(),
    goToListaProducto: () -> Unit,
    productoId: Int?,
    innerPadding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onSetProducto(productoId ?: 0)
    }
    ProductCardDetailedBody(
        uiState = uiState,
        goToListaProducto = goToListaProducto,
        innerPadding = innerPadding
    )
}


@Composable
fun ProductCardDetailedBody(
    uiState: ProductoUistate,
    goToListaProducto: () -> Unit,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(innerPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item {

            ProductDetail(uiState)

        }
    }

}

@Composable
private fun ProductDetail(uiState: ProductoUistate) {
    AsyncImage(
        model = uiState.imagen,
        contentDescription = uiState.nombre,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = uiState.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Categoría: ${uiState.categoria}")
            Text(text = "Precio: $${uiState.precio}")
            Text(text = "Stock: ${uiState.stock}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = uiState.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Especificaciones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = uiState.especificacion ?: "", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

