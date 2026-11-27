package com.example.moneyku.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class MascotType {
    COINS_EXCITED,
    INSPECTING_RECEIPT,
    WAVING_PURSE
}

@Composable
fun MascotImage(
    type: MascotType,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    val drawableRes = when (type) {
        MascotType.COINS_EXCITED -> com.example.moneyku.R.drawable.maskot_coins
        MascotType.INSPECTING_RECEIPT -> com.example.moneyku.R.drawable.maskot_receipt
        MascotType.WAVING_PURSE -> com.example.moneyku.R.drawable.maskot_waving
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = "MoneyKu Mascot PNG",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size * 0.85f)
        )
    }
}
