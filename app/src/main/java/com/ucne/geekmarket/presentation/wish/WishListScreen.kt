package com.ucne.geekmarket.presentation.wish

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.presentation.Common.formatNumber
import com.ucne.geekmarket.presentation.components.EmptyContent

@Composable
fun WishListScreen(
    innerPadding: PaddingValues,
    deleteAllowed: Boolean,
    goToProducto: (ProductoEntity) -> Unit,
    viewModel: WishListViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CarritoListScreenBody(
        uiState = uiState,
        innerPadding = innerPadding,
        onDeleteWish = viewModel::deleteWish,

        deleteAllowed = deleteAllowed,
        goToProducto = goToProducto,
        onAddItem = viewModel::onAddItem
    )
}

@Composable
fun CarritoListScreenBody(
    uiState: WishListUistate,
    goToProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    onAddItem: (ItemEntity) -> Unit,
    onDeleteWish: (Int) -> Unit,
    deleteAllowed: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ProductoEntity?>(null) }
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
        ) {
            item {
                Text(
                    text = "Wish List",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(uiState.productos ?: emptyList()) { producto ->
                CartItemCard(
                    producto = producto,
                    onDeleteWish = {
                        showDialog = !showDialog
                        selectedItem = it
                    },
                    deleteAllowed = deleteAllowed,
                    goToProducto = goToProducto
                )
            }
        }
        if(uiState.productos.isNullOrEmpty()){
            Column(Modifier.align(Alignment.Center)){
                EmptyContent()
            }
        }

    }
    if (showDialog) {
        var cantidad by remember { mutableIntStateOf(1) }
        AlertDialog(
            onDismissRequest = {

            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text(text = "Cancelar")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddItem(
                            ItemEntity(
                                productoId = selectedItem?.productoId ?: 0,
                                cantidad = cantidad
                            )
                        )
                        onDeleteWish(selectedItem?.productoId ?: 0)
                        showDialog = false
                    }
                ) {
                    Text("Carrito")
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Filled.AddShoppingCart,
                        contentDescription = "Agregar al carrito"
                    )

                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = selectedItem?.nombre ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    AsyncImage(
                        model = selectedItem?.imagen,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp))

                    )
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
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Quantity"
                            )
                        }


                    }
                }
            }
        )
    }

}

@Composable
fun CartItemCard(
    producto: ProductoEntity,
    onDeleteWish: (ProductoEntity) -> Unit,
    goToProducto: (ProductoEntity) -> Unit,
    deleteAllowed: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                goToProducto(producto)
            }
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
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row {
                        Text(
                            text = producto.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.30f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${formatNumber(producto.precio)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                onDeleteWish(producto)
                            },
                            modifier = Modifier.padding(1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = "Remove Item"
                            )
                        }
                    }

                }
            }
        }
    }
}

