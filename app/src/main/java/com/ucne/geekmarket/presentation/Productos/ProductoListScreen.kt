package com.ucne.geekmarket.presentation.Productos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.R
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
    goToCategoria: (String) -> Unit,
    goToSearchScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProductoListBody(
        onVerProducto = onVerProducto,
        innerPadding = innerPadding,
        onAddItem = viewModel::onAddItem,
        uiState = uiState,
        goToPromotion = goToPromotion,
        goToCategoria = goToCategoria,
        goToSearchScreen = goToSearchScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoListBody(
    uiState: ProductoUistate,
    onVerProducto: (ProductoEntity) -> Unit,
    innerPadding: PaddingValues,
    goToPromotion: (PromocionEntity) -> Unit,
    onAddItem: (ItemEntity) -> Unit,
    goToCategoria: (String) -> Unit,
    goToSearchScreen: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 10.dp, end = 10.dp, bottom = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val context = LocalContext.current
            val iconColor = if (isSystemInDarkTheme()) {
                Color(ContextCompat.getColor(context, R.color.white))
            } else {
                Color(ContextCompat.getColor(context, R.color.black))
            }

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {
                    goToSearchScreen()
                }
            ) {
                OutlinedTextField(
                    value = "Buscar",
                    onValueChange = { },
                    readOnly = true,
                    placeholder = { Text(text = "Buscar") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(50),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                )
            }

        }
        item {
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
                if (uiState.laptops.isEmpty()) {
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
                        items(uiState.laptops) { item ->
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
                TextButton(onClick = { goToCategoria("Accesorio") }) {
                    Text(
                        text = "Accesorios",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
                if (uiState.accesorios.isEmpty()) {
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
                        items(uiState.accesorios) { item ->
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
                if (uiState.laptopsGaming.isEmpty()) {
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
                        items(uiState.laptopsGaming) { item ->
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
                            .padding(4.dp)
                    )
                }
                if (uiState.desktops.isEmpty()) {
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
                        items(uiState.desktops) { item ->
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
        uiState.promociones?.size ?: 0
    })
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    ) {

        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LaunchedEffect(key1 = pagerState) {
                while (true) {
                    delay(3000)
                    var nextPage = 0
                    if (pagerState.currentPage < 3) {
                        nextPage = (pagerState.currentPage + 1)
                    }
                    scope.launch {
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (uiState.promociones.isNullOrEmpty()) {
                    CircularProgressIndicator()
                } else {
                    AsyncImage(
                        model = uiState.promociones!![page].imagen,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { goToProduct(uiState.promociones!![page]) }
                            .fillMaxHeight()
                            .height(151.dp)
                    )
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
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
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