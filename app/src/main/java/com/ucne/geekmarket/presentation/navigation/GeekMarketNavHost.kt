package com.ucne.alexandersuarez_ap2_p1.presentation.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.ucne.geekmarket.presentation.Carritos.CarritoListScreen
import com.ucne.geekmarket.presentation.ProductoDetail.ProductDetailed
import com.ucne.geekmarket.presentation.Productos.ProductoListScreen
import com.ucne.geekmarket.presentation.components.BottonBar
import com.ucne.geekmarket.presentation.components.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeekMarketNavHost(
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isInCarrito by remember { mutableStateOf(false) }
    var showDeleteButton by remember { mutableStateOf(false) }
    //Mover el Scaffold a otro lugar TODO
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CustomTopAppBar(
            lable = "GeekMarket",
            topBarAction = { showDeleteButton = !showDeleteButton },
            isInCarrito = isInCarrito
        )
    },
        bottomBar = {
            BottonBar(
                goToListaProducto = { navHostController.navigate(Screen.ProductList) },
                goToCarrito = { navHostController.navigate(Screen.CarritoList) },
                currentRoute = currentRoute
            )
//            BottomBarPrueba()
        }
    ) { innerPadding ->
        NavHost(navController = navHostController, startDestination = Screen.ProductList) {
            composable<Screen.ProductList> {
                ProductoListScreen(
                    onVerProducto = {
                        navHostController.navigate(
                            Screen.ProductDetail(
                                it.productoId ?: 0
                            )
                        )
                    },
                    innerPadding = innerPadding,
                )
                isInCarrito = false
            }
            composable<Screen.ProductDetail> {
                val args = it.toRoute<Screen.ProductDetail>().productoId
                ProductDetailed(
                    goToListaProducto = { navHostController.navigate(Screen.ProductList) },
                    innerPadding = innerPadding,
                    productoId = args
                )
                isInCarrito = false
            }
            composable<Screen.CarritoList> {
                isInCarrito = true
                CarritoListScreen(innerPadding, showDeleteButton )
            }
        }
    }
}