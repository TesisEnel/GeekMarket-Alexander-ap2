package com.ucne.alexandersuarez_ap2_p1.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.ucne.geekmarket.presentation.Carritos.CarritoListScreen
import com.ucne.geekmarket.presentation.ProductoDetail.ProductDetailed
import com.ucne.geekmarket.presentation.Productos.ProductoListScreen
import com.ucne.geekmarket.presentation.Search.SearchScreen
import com.ucne.geekmarket.presentation.categoria.CategoriaListScreen
import com.ucne.geekmarket.presentation.components.buttombar.BottonBar
import com.ucne.geekmarket.presentation.components.CustomTopAppBar
import com.ucne.geekmarket.presentation.wish.WishListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeekMarketNavHost(
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showDeleteButton by remember { mutableStateOf(false) }
    //Mover el Scaffold a otro lugar TODO
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                lable = "GeekMarket",
                topBarAction = { showDeleteButton = !showDeleteButton },
                isInCarrito = currentRoute?.contains(Screen.CarritoList.toString())?: false,
            )
        },
        bottomBar = {
            BottonBar(
                goToListaProducto = { navHostController.navigate(Screen.ProductList) },
                goToCarrito = { navHostController.navigate(Screen.CarritoList) },
                currentRoute = currentRoute,
                goToWishList = { navHostController.navigate(Screen.WishList) }
            )
        }
    ) { innerPadding ->
        NavHost(navController = navHostController, startDestination = Screen.ProductList) {
            composable<Screen.ProductList> {
                ProductoListScreen(
                    onVerProducto = {
                        navHostController.navigate(
                            Screen.ProductDetail(
                                it.productoId
                            )
                        )
                    },
                    innerPadding = innerPadding,
                    goToPromotion = {
                        navHostController.navigate(
                            Screen.ProductDetail(
                                it.productoId
                            )
                        )
                    },
                    goToCategoria = {
                        navHostController.navigate(
                            Screen.Categoria(
                                it
                            )
                        )
                    },
                    goToSearchScreen = {
                        navHostController.navigate(
                            Screen.Search
                        )
                    }
                )
            }
            composable<Screen.ProductDetail> {
                val args = it.toRoute<Screen.ProductDetail>().productoId
                ProductDetailed(
                    goToListaProducto = { navHostController.navigate(Screen.ProductList) },
                    innerPadding = innerPadding,
                    productoId = args
                )
            }
            composable<Screen.CarritoList> {
                CarritoListScreen(innerPadding, showDeleteButton)
            }
            composable<Screen.Categoria> {
                CategoriaListScreen(
                    innerPadding = innerPadding,
                    categoria = it.toRoute<Screen.Categoria>().categoria,
                    onVerProducto = {
                        navHostController.navigate(
                            Screen.ProductDetail(
                                it.productoId
                            )
                        )
                    },
                    goToProduct = {
                        navHostController.navigate(
                            Screen.ProductDetail(
                                it.productoId
                            )
                        )
                    }
                )
            }
            composable<Screen.WishList> {
                WishListScreen(innerPadding, true)
            }
            composable<Screen.Search> {
                SearchScreen(
                    goToHomeScreen = { navHostController.navigate(Screen.ProductList) },
                    goToProducto = {navHostController.navigate(Screen.ProductDetail(it.productoId))}
                )
            }

        }
    }
}