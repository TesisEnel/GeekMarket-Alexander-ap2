package com.ucne.geekmarket.presentation.Carritos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.data.local.entities.Items
import com.ucne.geekmarket.presentation.components.CenteredTextDivider
import kotlin.reflect.KFunction0

@Composable
fun CarritoListScreen(
    innerPadding: PaddingValues,
    viewModel: CarritoViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        viewModel.getLastCarrito()

    CarritoListScreenBody(
        uiState = uiState,
        innerPadding = innerPadding,
        onRemoveItem = viewModel::deleteItem,
    )
}

@Composable
fun CarritoListScreenBody(
    uiState: carritoUistate,
    innerPadding: PaddingValues,
    onRemoveItem: (Int) -> Unit) {

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(uiState.items ?: emptyList()) { item ->
            CartItemCard(
                item = item,
                onRemoveItem = onRemoveItem
            )
        }
        item {
            val total = uiState.items?.sumOf { item ->
                (item.cantidad ?: 0) * (item.producto?.precio ?: 0.0)
            }
            CenteredTextDivider(text = "Total: ${total} ")
        }
    }

}

@Composable
fun CartItemCard(
    item: Items,
    onRemoveItem: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item.producto?.imagen?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = item.producto.nombre,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column {
                    Row {
                        Text(
                            text = item.producto?.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.30f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Precio: $${item.producto?.precio ?: 0}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "x:${item.cantidad ?: 0}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = { item.producto?.productoId?.let { onRemoveItem(it) } }, modifier = Modifier.padding(1.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Item"
                            )
                        }
                    }
                    Text(
                        text = "Monto: ${(item.producto?.precio ?: 0.0) * (item.cantidad ?: 0)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

