package com.ucne.geekmarket.presentation.Search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.presentation.Common.formatNumber
import com.ucne.geekmarket.presentation.components.SearchBar


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    goToProducto: (ProductoEntity) -> Unit,
    goToHomeScreen: () -> Unit
) {

    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 16.dp)
                .fillMaxWidth(fraction = 0.60F)
        ) {


            Text(
                text = "Search",
                modifier = Modifier.padding(start = 50.dp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
//                color = AppOnPrimaryColor
            )
        }

        SearchBar(
            onSearch = {},
            onSearchParamChanged = { searchViewModel.search(it) },
            searchParam = searchUiState.paramater?:""
        )

        LazyVerticalGrid(
            modifier = Modifier

                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {}
                .clip(shape = RoundedCornerShape(10.dp))
                .padding(10.dp),
            columns = GridCells.Adaptive(minSize = 158.dp)
        )  {
            items(searchUiState.productos) { producto ->
                ProductoCard(producto = producto, goToProducto = goToProducto)
            }
//            when (searchResult.loadState.refresh)
        }
    }
}

@Composable
fun ProductoCard(producto: ProductoEntity, goToProducto: (ProductoEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .clickable {
                goToProducto(producto)
            }
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
        }
    }
}
