package com.ucne.geekmarket.presentation.components.buttombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.alexandersuarez_ap2_p1.presentation.navigation.Screen
import com.ucne.geekmarket.R
import com.ucne.geekmarket.presentation.Carritos.CarritoViewModel
import com.ucne.geekmarket.presentation.components.ButtomNavigationItem
import com.ucne.geekmarket.ui.theme.CardColor
import com.ucne.geekmarket.ui.theme.SecondaryColor
import com.ucne.geekmarket.ui.theme.ThinTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Composable
fun BottonBar(
    goToListaProducto: () -> Unit,
    goToCarrito: () -> Unit,
    goToWishList: () -> Unit,
    currentRoute: String?
) {
    val viewModel: ButtonBarViewModel = hiltViewModel()
    val itemList by viewModel.items.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsState()
    val quantity = uiState.quantity
    viewModel.getPromociones()

    Box(contentAlignment = Alignment.BottomCenter) {
        NavigationBar(
            modifier = Modifier
                .height(80.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .background(CardColor)
                .align(Alignment.BottomCenter)
        ) {
            val items = listOf(
                ButtomNavigationItem(
                    title = "ShoppingCart",
                    selectedIcon = Icons.Filled.ShoppingCart,
                    unselectedIcon = Icons.Outlined.ShoppingCart,
                    hasNews = false,
                    badgeCount = 45,
                    direction = Screen.CarritoList.toString()
                ),
                ButtomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    hasNews = false,
                    direction = Screen.ProductList.toString()
                ),
                ButtomNavigationItem(
                    title = "WishList",
                    selectedIcon = Icons.Filled.Favorite,
                    unselectedIcon = Icons.Outlined.FavoriteBorder,
                    hasNews = false,
                    direction = Screen.WishList.toString()
                ),
                ButtomNavigationItem(
                    title = "Profile",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    hasNews = false,
                    direction = Screen.Profile.toString()
                ),


                )
            //TODO: Arreglar que cando les das a back se ponga el icono correcto
            var selectedeDirection by rememberSaveable {
                mutableStateOf(currentRoute ?: Screen.ProductList.toString())
            }

            items.forEach { item ->


                NavigationBarItem(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(top = 30.dp),
                    alwaysShowLabel = false,
                    selected = item.direction == selectedeDirection,
                    onClick = {
                        selectedeDirection = item.direction ?: ""
                        when (selectedeDirection) {
                            Screen.ProductList.toString() -> goToListaProducto()
                            Screen.CarritoList.toString() -> goToCarrito()
                            Screen.WishList.toString() -> goToWishList()
                            else -> goToListaProducto()
                        }
                    },
                    label = {
                        Text(text = item.title)
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.direction == Screen.CarritoList.toString() &&
                                    (quantity?: 0) > 0
                                ) {
                                    Badge {
                                        Text(text = (uiState.quantity).toString())
                                    }

                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (item.direction == selectedeDirection)
                                    item.selectedIcon
                                else
                                    item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    }
                )
            }
        }

    }
}