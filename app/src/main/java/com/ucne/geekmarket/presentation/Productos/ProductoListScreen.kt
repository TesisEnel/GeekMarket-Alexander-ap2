package com.ucne.geekmarket.presentation.Productos

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.presentation.Carritos.CarritoViewModel
import com.ucne.geekmarket.presentation.Common.formatNumber


@Composable
fun ProductoListScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
) {

    val laptops = viewModel.laptops.collectAsStateWithLifecycle()
    val laptopsGaming = viewModel.laptopsGaming.collectAsStateWithLifecycle()
    val desktops = viewModel.descktops.collectAsStateWithLifecycle()

    ProductoListBody(
        laptops = laptops.value,
        laptopsGaming = laptopsGaming.value,
        desktops = desktops.value,
        onVerProducto = onVerProducto,
        innerPadding = innerPadding,
        onAddItem = viewModel::onAddItem,
    )
}

@Composable
fun ProductoListBody(
    laptops: List<ProductoEntity>,
    desktops: List<ProductoEntity>,
    laptopsGaming: List<ProductoEntity>,
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    onAddItem: (ItemEntity) -> Unit,
) {

    var cantidad by remember { mutableStateOf(1) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column {

                Text(
                    text = "Laptops",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                )
                if (laptops.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(laptops) { item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .width(200.dp)
                                .clickable {
                                    onVerProducto(item)
                                }
                                .padding(3.dp),
                                verticalAlignment = Alignment.CenterVertically) {

                                ProductCard(
                                    producto = item,
                                    onAddToCart = onAddItem,
                                )


                            }
                        }
                    }

                }
            }
        }
        item {
            Column {

                Text(
                    text = "Laptops Gaming",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                )
                if (laptopsGaming.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(laptopsGaming) { item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .width(200.dp)
                                .clickable {
                                    onVerProducto(item)
                                }
                                .padding(3.dp),
                                verticalAlignment = Alignment.CenterVertically) {

                                ProductCard(
                                    producto = item,
                                    onAddToCart = onAddItem,
                                )

                            }
                        }
                    }
                }
            }
        }
        item {
            Column {
                Text(
                    text = "Desktops",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(4.dp)
                )
                if (laptops.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(desktops) { item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .width(200.dp)
                                .clickable {
                                    onVerProducto(item)
                                }
                                .padding(3.dp),
                                verticalAlignment = Alignment.CenterVertically) {

                                ProductCard(
                                    producto = item,
                                    onAddToCart = onAddItem,
                                )

                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ProductCard(producto: ProductoEntity, onAddToCart: (ItemEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(1.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Descripción: ${producto.especificacion}", maxLines = 3)
            Text(text = "Precio: ${formatNumber(producto.precio)}")
        }
    }
}