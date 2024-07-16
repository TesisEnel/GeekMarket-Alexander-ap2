package com.ucne.alexandersuarez_ap2_p1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ucne.geekmarket.presentation.Productos.ProductDetailed
import com.ucne.geekmarket.presentation.Productos.ProductoListScreen

@Composable
fun GeekMarketNavHost(
    navHostController: NavHostController,
    ) {

    NavHost(navController = navHostController, startDestination = Screen.ProductList) {
        composable<Screen.ProductList> {
            ProductoListScreen(
                onVerProducto = {navHostController.navigate(Screen.ProductDetail(it.productoId ?: 0))}
            )

        }
        composable<Screen.ProductDetail> {
            val args = it.toRoute<Screen.ProductDetail>().productoId
            ProductDetailed(
                goToListaProducto =  {navHostController.navigate(Screen.ProductList)},
                productoId = args
            )

        }
    }
}