@file:OptIn(ExperimentalMaterial3Api::class)

package com.ucne.geekmarket.presentation.ProductoDetail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.R
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.presentation.Common.formatNumber


@Composable
fun ProductDetailed(
    viewModel: ProductoDetailScreenViewModel = hiltViewModel(),
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
        onAddItem = viewModel::onAddItem,
        innerPadding = innerPadding
    )
}


@Composable
fun ProductCardDetailedBody(
    uiState: ProductoDetailUiState,
    onAddItem: (ItemEntity) -> Unit,
    goToListaProducto: () -> Unit,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .clip(RoundedCornerShape(100.dp))
            .padding(innerPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            ProductDetail(uiState, onAddItem)
        }
    }

}

@Composable
private fun ProductDetail(uiState: ProductoDetailUiState, onAddItem: (ItemEntity) -> Unit) {
    var wishListAdded by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    Column {
        IconButton(
            onClick = {
                wishListAdded = !wishListAdded
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 10.dp)
        ) {
            if (!wishListAdded) {
                Image(
                    painter = painterResource(id = R.drawable.pixel_heart_icon_fill_grey),
                    contentDescription = "Agregar a la lista de deseos"
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pixel_heart_icon_fill),
                    contentDescription = "Agregar a la lista de deseos"
                )
            }
        }
        AsyncImage(
            model = uiState.imagen,
            contentDescription = uiState.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
    var cantidad by remember { mutableStateOf(1) }
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { cantidad-- },
                    enabled = cantidad > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease Quantity"
                    )
                }
                Text(text = formatNumber(cantidad.toDouble()))
                IconButton(onClick = { cantidad++ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase Quantity")
                }
                Button(
                    onClick = {
                        onAddItem(
                            ItemEntity(
                                productoId = uiState.productoId,
                                cantidad = cantidad
                            )
                        )
                        showToast = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Agregar al carrito"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Agregar al carrito")
                }

            }
            Text(
                text = uiState.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Categoría: ${uiState.categoria}")
            Text(text = "Precio: $${formatNumber(uiState.precio)}")
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
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = uiState.especificacion ?: "", style = MaterialTheme.typography.bodyMedium)

        }
    }
    if (showToast) {
        Notification("${uiState.nombre} se ha agregado al carrito")
        showToast = false
    }
}

@Composable
fun Notification(message: String){
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_LONG).show()
}

