package com.ucne.geekmarket.presentation.Productos

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ucne.geekmarket.presentation.Common.formatNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductoListScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    goToPromotion: (PromocionEntity) -> Unit,
    goToCategoria: (String) -> Unit
) {
    val promociones = viewModel.promociones.collectAsStateWithLifecycle()
    val laptops = viewModel.laptops.collectAsStateWithLifecycle()
    val laptopsGaming = viewModel.laptopsGaming.collectAsStateWithLifecycle()
    val desktops = viewModel.descktops.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductoListBody(
        laptops = laptops.value,
        laptopsGaming = laptopsGaming.value,
        desktops = desktops.value,
        onVerProducto = onVerProducto,

        innerPadding = innerPadding,
        onAddItem = viewModel::onAddItem,
        uiState = uiState,
        goToPromotion = goToPromotion,
        goToCategoria = goToCategoria
    )
}

@Composable
fun ProductoListBody(
    uiState: ProductoUistate,
    laptops: List<ProductoEntity>,
    desktops: List<ProductoEntity>,
    laptopsGaming: List<ProductoEntity>,
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    goToPromotion: (PromocionEntity) -> Unit,
    onAddItem: (ItemEntity) -> Unit,
    goToCategoria: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 10.dp, end = 10.dp, bottom = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            PromotionCard(uiState, goToPromotion)
        }
        item {
            Column {
                TextButton(onClick = { goToCategoria("Laptop") }) {
                    Text(
                        text = "Laptops",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
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
                TextButton(onClick = { goToCategoria("Laptop-Gaming") }) {
                    Text(
                        text = "Laptops Gaming",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
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
                TextButton(onClick = { goToCategoria("Desktop") }) {
                    Text(
                        text = "Desktops",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
//                            .align(Alignment.Start)
                            .padding(4.dp)
                    )
                }
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
            .height(360.dp)
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

@Composable
fun PromotionCard(uiState: ProductoUistate, goToProduct: (PromocionEntity) -> Unit) {
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    val scope = rememberCoroutineScope()
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    ){

        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LaunchedEffect(key1 = pagerState) {
                while (true) {
                    delay(3000)
                    var nextPage = 0
                    if(pagerState.currentPage < 3) {
                        nextPage = (pagerState.currentPage + 1)
                    }
                    scope.launch {
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }
            HorizontalPager(state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (uiState.promociones.isNotEmpty()) {
                    uiState.promociones[page].imagen.let {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { goToProduct(uiState.promociones[page]) }
                                .fillMaxHeight()
                                .height(151.dp)
                        )
                    }
                }
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }

        }

    }
}