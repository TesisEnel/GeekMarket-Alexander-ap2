package com.ucne.geekmarket.presentation.Productos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.ui.theme.BottonBar


@Composable
fun ProductoListScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    onVerProducto: (ProductoDto) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getProductosByCategoria()
    }

    ProductoListBody(
        laptops = uiState.laptops,
        laptopsGaming = uiState.laptopsGaming,
        desktops = uiState.descktops,
        onVerProducto = onVerProducto,
        onList = viewModel::getProductos,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoListBody(
    laptops: List<ProductoDto>,
    desktops: List<ProductoDto>,
    laptopsGaming: List<ProductoDto>,
    onVerProducto: (ProductoDto) -> Unit,
    onList: () -> Unit,
//    uistate: ProductoUistate,
) {

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Row {
                    Text(text = "GeekMarket")
                    TextButton(onClick = { onList() }) {
                        Text(text = "Get Productos")
                    }
                }
            }
        )
    },
        bottomBar = {
            BottonBar()
//            BottomBarPrueba()
        }
        ) { innerPadding ->
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
                                .fillMaxSize()
                                .padding(innerPadding),
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

                                    ProductCard(product = item, onAddToCart = {})


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
                                .fillMaxSize()
                                .padding(innerPadding),
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

                                    ProductCard(product = item, onAddToCart = {})

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
                                .fillMaxSize()
                                .padding(innerPadding),
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

                                    ProductCard(product = item, onAddToCart = {})

                                }
                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun ProductCard(product: ProductoDto, onAddToCart: (ProductoDto) -> Unit) { // Add onAddToCartlambda
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(410.dp)
            .padding(1.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        IconButton(
            onClick = { onAddToCart(product) },
            modifier = Modifier
                .align(Alignment.End)
                .size(30.dp)
        )
        {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Agregar al carrito")
        }


        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = product.imagen,
                contentDescription = product.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Descripción: ${product.especificacion}")
            Text(text = "Precio: ${product.precio}")

        }
    }
}

