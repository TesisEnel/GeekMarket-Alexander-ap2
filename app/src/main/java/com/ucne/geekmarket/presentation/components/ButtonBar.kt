package com.ucne.geekmarket.presentation.components

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ucne.alexandersuarez_ap2_p1.presentation.navigation.Screen
import com.ucne.geekmarket.R
import com.ucne.geekmarket.presentation.Carritos.CarritoViewModel
import com.ucne.geekmarket.ui.theme.CardColor
import com.ucne.geekmarket.ui.theme.SecondaryColor
import com.ucne.geekmarket.ui.theme.ThinTextColor

@Composable
fun BottonBar(goToListaProducto: () -> Unit, goToCarrito: () -> Unit) {
    val viewModel: CarritoViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
//    LaunchedEffect(Unit) {
//        viewModel.getLastCarrito()
//    }
    Box(contentAlignment = Alignment.BottomCenter){
        NavigationBar(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
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
                    badgeCount = 45
                ),
                ButtomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    hasNews = false
                ),
                ButtomNavigationItem(
                    title = "Profile",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    hasNews = false
                ),


                )
            var selectedeItemIndex by rememberSaveable {
                mutableStateOf(1)
            }

            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier.align(Alignment.Bottom).height(60.dp),
                    alwaysShowLabel = false,
                    selected = selectedeItemIndex == index,
                    onClick = {
                        selectedeItemIndex = index
                        when(selectedeItemIndex){
                            0 -> {goToCarrito()}
                            1 -> goToListaProducto()
                            else -> goToListaProducto()
//                            2 -> { TODO()}
                        }
    //                                    navController.navigate(item.title)
                    },
                    label = {
                        Text(text = item.title)
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if(index == 0){
                                    if (uiState.items?.isNotEmpty() == true) {
                                        Badge {
                                            Text(text = uiState.items?.size.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (index == selectedeItemIndex)
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


@Preview
@Composable
private fun prueba() {
//    BottomBarPrueba()

//    BottonBar({ }) { navHostController.navigate(Screen.CarritoList) }
}


@Composable
fun BottomBarPrueba(modifier: Modifier = Modifier) {

    Box(contentAlignment = Alignment.BottomCenter) {
        var navNum by remember {
            mutableStateOf(0)
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(CardColor)
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (navNum == 0) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_filled),
                            contentDescription = "home",
                            tint = SecondaryColor,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }

                } else {
                    IconButton(onClick = { navNum = 0 }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_light),
                            contentDescription = "home",
                            tint = ThinTextColor,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.width(8.dp))
                if (navNum == 1) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_filled),
                            contentDescription = "home",
                            tint = SecondaryColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { navNum = 1 }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_light),
                            contentDescription = "home",
                            tint = ThinTextColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (navNum == 2) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message_filled),
                            contentDescription = "home",
                            tint = SecondaryColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { navNum = 2 }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message_light),
                            contentDescription = "home",
                            tint = ThinTextColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (navNum == 3) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.user_filled),
                            contentDescription = "home",
                            tint = SecondaryColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { navNum = 3 }) {
                        Icon(
                            painter = painterResource(id = R.drawable.user_light),
                            contentDescription = "home",
                            tint = ThinTextColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(bottom = 35.dp)
                .clip(CircleShape)
                .background(SecondaryColor)
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        )
        {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}