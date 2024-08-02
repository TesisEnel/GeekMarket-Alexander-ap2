package com.ucne.geekmarket.presentation.Common

import java.text.NumberFormat

fun formatNumber(number: Double?): String {
    val numberFormat = NumberFormat.getInstance()
    return numberFormat.format(number?: 0.0)
}