package com.ucne.geekmarket.presentation.categoria

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.dto.PromocionDto
import com.ucne.geekmarket.presentation.Common.formatNumber
import com.ucne.geekmarket.presentation.Productos.ProductoUistate
import com.ucne.geekmarket.presentation.Productos.ProductoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CategoriaListScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    categoria: String,
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    goToProduct: (PromocionEntity) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getByCategoria(categoria)
    }

    CategoriaListBody(
        onVerProducto = onVerProducto,
        innerPadding = innerPadding,
        onAddItem = viewModel::onAddItem,
        uiState = uiState,
        goToProduct = goToProduct
    )
}

@Composable
fun CategoriaListBody(
    uiState: CategoriaUistate,
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    goToProduct: (PromocionEntity) -> Unit,
    onAddItem: (ItemEntity) -> Unit,
) {

    var cantidad by remember { mutableStateOf(1) }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(bottom = 1.dp,start = 10.dp, end = 10.dp),
        columns = GridCells.Adaptive(minSize = 158.dp)
    ) {


        items(uiState.productos?: emptyList()) { item ->
            if (uiState.productos.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),

                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    CircularProgressIndicator()
                }
            }
            else {
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

@Composable
fun ProductCard(producto: ProductoEntity, onAddToCart: (ItemEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .padding(1.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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