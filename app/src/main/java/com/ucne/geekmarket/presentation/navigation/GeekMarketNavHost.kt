package com.ucne.geekmarket.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ucne.geekmarket.presentation.Carritos.CarritoListScreen
import com.ucne.geekmarket.presentation.ProductoDetail.ProductDetailed
import com.ucne.geekmarket.presentation.Productos.ProductoListScreen
import com.ucne.geekmarket.presentation.Search.SearchScreen
import com.ucne.geekmarket.presentation.login.LoginScreen
import com.ucne.geekmarket.presentation.signup.SignupScreen
import com.ucne.geekmarket.presentation.categoria.CategoriaListScreen
import com.ucne.geekmarket.presentation.profile.ProfileScreen
import com.ucne.geekmarket.presentation.wish.WishListScreen

@Composable
fun GeekMarketNavHost(
    navHostController: NavHostController,
    innerPadding: PaddingValues,
    showDeleteButton: Boolean = false
) {

    NavHost(navController = navHostController, startDestination = Screen.ProductList) {
        composable<Screen.Login> {
            LoginScreen(
                goToSignup = {
                    navHostController.navigate(Screen.Signup)
                },
                goToProductList = {
                    navHostController.navigate(Screen.ProductList)
                }
            )
        }
        composable<Screen.Signup> {
            SignupScreen(
                goToLogin = {
                    navHostController.navigate(Screen.Login)
                }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                innerPadding = innerPadding,
                goToLogin = {
                    navHostController.navigate(Screen.Login)
                },

            )
        }
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
            CarritoListScreen(
                innerPadding = innerPadding,
                deleteAllowed = showDeleteButton,
                goToLoging = {
                    navHostController.navigate(Screen.Login)
                }
            )
        }
        composable<Screen.Categoria> { categoria ->
            CategoriaListScreen(
                innerPadding = innerPadding,
                categoria = categoria.toRoute<Screen.Categoria>().categoria,
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
            WishListScreen(
                innerPadding = innerPadding,
                deleteAllowed = true,
                goToProducto = { navHostController.navigate(Screen.ProductDetail(it.productoId)) }
            )
        }
        composable<Screen.Search> {
            SearchScreen(
                goToHomeScreen = { navHostController.navigate(Screen.ProductList) },
                goToProducto = { navHostController.navigate(Screen.ProductDetail(it.productoId)) }
            )
        }

    }
}

