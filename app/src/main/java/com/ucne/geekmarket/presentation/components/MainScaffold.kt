package com.ucne.geekmarket.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ucne.geekmarket.presentation.components.buttombar.BottonBar
import com.ucne.geekmarket.presentation.navigation.Screen

@Composable
fun MainScaffold(
    navHostController: NavHostController,
    showDeleteButton: ()-> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?: Screen.ProductList.toString()
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            if(!currentRoute.contains(Screen.Login.toString()) && !currentRoute.contains(Screen.Signup.toString())){
                CustomTopAppBar(
                    lable = "eekMarket",
                    showDeleteButton = showDeleteButton,
                    isInCarrito = currentRoute.contains(Screen.CarritoList.toString()),
                )
            }
        },
        bottomBar = {
            if(!currentRoute.contains(Screen.Login.toString()) && !currentRoute.contains(Screen.Signup.toString())){
                BottonBar(
                    goToListaProducto = { navHostController.navigate(Screen.ProductList) },
                    goToCarrito = { navHostController.navigate(Screen.CarritoList) },
                    currentRoute = currentRoute,
                    goToWishList = { navHostController.navigate(Screen.WishList) },
                    goToProfile = { navHostController.navigate(Screen.Profile) }
                )
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}