package com.example.moneyku.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "aman", "lunas", "paid", "settled", "success" -> Pair(
            Color(0xFFDCFCE7),
            Color(0xFF15803D)
        )
        "pending", "belum lunas", "hampir habis", "warning" -> Pair(
            Color(0xFFFEF3C7),
            Color(0xFFB45309)
        )
        "over budget", "bahaya", "danger", "overdue" -> Pair(
            Color(0xFFFEE2E2),
            Color(0xFFB91C1C)
        )
        else -> Pair(
            Color(0xFFF1F5F9),
            Color(0xFF475569)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
