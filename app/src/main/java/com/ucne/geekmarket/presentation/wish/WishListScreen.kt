package com.ucne.geekmarket.presentation.wish

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
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.presentation.Common.formatNumber

@Composable
fun WishListScreen(
    innerPadding: PaddingValues,
    deleteAllowed: Boolean,
    viewModel: WishListViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CarritoListScreenBody(
        uiState = uiState,
        innerPadding = innerPadding,
        onDeleteWish = viewModel::deleteWish,
        deleteAllowed = deleteAllowed
    )
}

@Composable
fun CarritoListScreenBody(
    uiState: wishListUistate,
    innerPadding: PaddingValues,
    onDeleteWish: (Int) -> Unit,
    deleteAllowed: Boolean
) {

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
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
                onDeleteWish = onDeleteWish,
                deleteAllowed = deleteAllowed
            )
        }
    }

}

@Composable
fun CartItemCard(
    producto: ProductoEntity?,
    onDeleteWish: (Int) -> Unit,
    deleteAllowed: Boolean
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
                AsyncImage(
                    model = producto?.imagen,
                    contentDescription = producto?.nombre,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row {
                        Text(
                            text = producto?.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.30f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${formatNumber(producto?.precio)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                onDeleteWish(producto?.productoId ?: 0)
                            },
                            modifier = Modifier.padding(1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Item"
                            )
                        }
                    }

                }
            }
        }
    }
}

