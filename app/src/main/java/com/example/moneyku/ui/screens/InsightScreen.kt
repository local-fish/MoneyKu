package com.example.moneyku.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.R
import java.text.NumberFormat
import java.util.Locale

private val InsBackground = Color(0xFFFAF8FF)
private val InsTextPrimary = Color(0xFF131B2E)
private val InsTextMuted = Color(0xFF6C7A71)
private val InsTextSecondary = Color(0xFF3C4A42)
private val InsGreen = Color(0xFF10B981)
private val InsGreenDark = Color(0xFF006C49)
private val InsRed = Color(0xFFBA1A1A)
private val InsCardBorder = Color(0x0D0F172A)
private val InsTrack = Color(0xFFF1F5F9)
private val InsAmber = Color(0xFFF59E0B)
private val InsAmberDark = Color(0xFFB45309)
private val InsAmberText = Color(0xFFD97706)
private val InsChartRed = Color(0xFFDC2626)
private val InsMonthRed = Color(0xFFE11D48)
private val InsSoftCard = Color(0x80F8FAFC)

private data class InsCategory(
    val name: String,
    val amount: Long,
    val color: Color,
    val ring: Color
)

private data class InsTopItem(
    val id: String,
    val title: String,
    val dateText: String,
    val category: String,
    val categoryText: Color,
    val categoryBackground: Color,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBackground: Color,
    val amount: Long,
    val method: String
)

private const val InsMonthShort = "Oct"
private const val InsTotalSpending = 3650000L
private const val InsLastMonthSpending = 3200000L
private const val InsBudgetLimit = 4500000L
private const val InsIncome = 18500000L
private const val InsCashLeft = 14850000L

private val InsCategories = listOf(
    InsCategory("Food & Drinks", 1400000L, Color(0xFF10B981), Color(0xFFD1FAE5)),
    InsCategory("Shopping", 1200000L, Color(0xFF4F46E5), Color(0xFFE0E7FF)),
    InsCategory("Transport", 650000L, Color(0xFFF59E0B), Color(0xFFFEF3C7)),
    InsCategory("Entertainment", 400000L, Color(0xFFF43F5E), Color(0xFFFFE4E6))
)

private val InsTrendMonths = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct")
private val InsTrendValues = listOf(0L, 0L, 0L, 0L, 0L, 3650000L)

private val InsTopItems = listOf(
    InsTopItem("ins_1", "Lunch at Ranch Market (Split Bill)", "14 Oct 2026 • 13:15", "Food & Drinks", Color(0xFF10B981), Color(0xFFECFDF5), Icons.Outlined.Restaurant, Color(0xFF059669), Color(0xFFECFDF5), 240000L, "QRIS BCA"),
    InsTopItem("ins_2", "Monthly Groceries Superindo", "06 Oct 2026 • 19:40", "Shopping", Color(0xFF4338CA), Color(0xFFEEF2FF), Icons.Outlined.ShoppingCart, Color(0xFF4F46E5), Color(0xFFEEF2FF), 177045L, "Debit Mandiri"),
    InsTopItem("ins_3", "Sports Shoes", "02 Oct 2026 • 15:20", "Shopping", Color(0xFF4338CA), Color(0xFFEEF2FF), Icons.Outlined.ShoppingBag, Color(0xFF4F46E5), Color(0xFFEEF2FF), 150000L, "ShopeePay")
)

@Composable
fun InsightScreen(
    onTransactionClick: (String) -> Unit = {},
    onSeeAllTransactionsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val comingSoon = rememberInsComingSoon()
    var showPercentage by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(InsBackground)
    ) {
        InsTopBar(
            onProfileClick = onProfileClick,
            onMoreClick = comingSoon,
            onNotificationsClick = onNotificationsClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { InsTitle() }
            item { InsSpendingCard() }
            item {
                InsStatCard(
                    label = "Income",
                    value = insRupiah(InsIncome),
                    icon = Icons.Default.ArrowDownward,
                    iconTint = Color(0xFF059669),
                    iconBackground = Color(0xFFECFDF5)
                )
            }
            item {
                InsStatCard(
                    label = "Available Cash Left",
                    value = insRupiah(InsCashLeft),
                    icon = Icons.Outlined.AccountBalanceWallet,
                    iconTint = Color(0xFF0D9488),
                    iconBackground = Color(0xFFF0FDFA)
                )
            }
            item {
                InsBreakdownCard(
                    showPercentage = showPercentage,
                    onModeChange = { showPercentage = it }
                )
            }
            item { InsTrendCard(onLastMonthClick = comingSoon) }
            item {
                InsTopSpendingCard(
                    onSeeAllClick = onSeeAllTransactionsClick,
                    onItemClick = onTransactionClick
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(8.dp, RoundedCornerShape(9999.dp))
                        .clip(RoundedCornerShape(9999.dp))
                        .background(InsGreen)
                        .clickable { comingSoon() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Full Cash Flow",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InsTitle() {
    Column {
        Text(
            text = "Analysis & Reports",
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.7).sp,
            color = InsTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Know where your money goes and build financial resilience.",
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = InsTextSecondary
        )
    }
}

@Composable
private fun InsCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, InsCardBorder, RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
private fun InsSpendingCard() {
    val growth = ((InsTotalSpending - InsLastMonthSpending) * 100 / InsLastMonthSpending).toInt()
    val usage = (InsTotalSpending * 100 / InsBudgetLimit).toInt()

    InsCard {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 24.dp, y = 24.dp)
                .size(128.dp)
                .blur(20.dp)
                .clip(CircleShape)
                .background(Color(0x1A10B981))
        )
        Column(modifier = Modifier.padding(21.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "TOTAL SPENDING THIS MONTH",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = InsTextSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = InsTextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = insRupiah(InsTotalSpending),
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.9).sp,
                color = InsTextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFFFFBEB))
                    .border(1.dp, Color(0x99FDE68A), RoundedCornerShape(9999.dp))
                    .padding(horizontal = 13.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    tint = InsAmberDark,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Up $growth% from September (${insRupiah(InsLastMonthSpending)})",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.24.sp,
                    color = InsAmberDark
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = InsTrack)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly budget usage (Max. ${insRupiah(InsBudgetLimit)})",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = InsTextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$usage% used",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.16).sp,
                    color = InsTextPrimary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            InsProgressBar(
                fraction = usage / 100f,
                color = InsAmber,
                track = InsTrack,
                height = 8
            )
        }
    }
}

@Composable
private fun InsProgressBar(
    fraction: Float,
    color: Color,
    track: Color,
    height: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(9999.dp))
            .background(track)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(9999.dp))
                .background(color)
        )
    }
}

@Composable
private fun InsStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color
) {
    InsCard {
        Row(
            modifier = Modifier.padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.24.sp,
                    color = InsTextSecondary
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.16).sp,
                    color = InsTextPrimary
                )
            }
        }
    }
}

@Composable
private fun InsSectionTitle(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = InsTextPrimary
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = InsTextSecondary
        )
    }
}

@Composable
private fun InsBreakdownCard(
    showPercentage: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    val total = InsCategories.sumOf { it.amount }

    InsCard {
        Column(modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 25.dp, bottom = 21.dp)) {
            InsSectionTitle(title = "Spending Breakdown", subtitle = "Cost breakdown by main category")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                InsToggleChip(label = "Percentage", isSelected = showPercentage, onClick = { onModeChange(true) })
                InsToggleChip(label = "Amount", isSelected = !showPercentage, onClick = { onModeChange(false) })
            }
            Spacer(modifier = Modifier.height(17.dp))
            HorizontalDivider(thickness = 1.dp, color = InsTrack)
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
            ) {
                InsDonut(
                    total = total,
                    modifier = Modifier
                        .size(224.dp)
                        .align(Alignment.Center)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL COST",
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = InsTextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = insRupiah(total),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.16).sp,
                        color = InsTextPrimary
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.maskot_waving),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-8).dp)
                        .width(98.dp)
                        .height(109.dp)
                        .graphicsLayer { scaleX = -1f }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InsCategories.forEach { category ->
                    InsLegendItem(
                        category = category,
                        percent = Math.round(category.amount * 100.0 / total).toInt(),
                        showPercentage = showPercentage
                    )
                }
            }
        }
    }
}

@Composable
private fun InsToggleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.4.sp,
        color = if (isSelected) Color.White else InsTextSecondary,
        modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(if (isSelected) InsGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
private fun InsDonut(
    total: Long,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.14f
        val inset = strokeWidth / 2f
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val gap = 2f
        var startAngle = -90f
        InsCategories.forEach { category ->
            val sweep = 360f * category.amount / total
            drawArc(
                color = category.color,
                startAngle = startAngle + gap / 2f,
                sweepAngle = (sweep - gap).coerceAtLeast(0f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweep
        }
    }
}

@Composable
private fun InsLegendItem(
    category: InsCategory,
    percent: Int,
    showPercentage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InsSoftCard)
            .border(1.dp, InsTrack, RoundedCornerShape(12.dp))
            .padding(13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(category.ring),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(category.color)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = category.name,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = InsTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            if (showPercentage) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($percent%)",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    color = category.color
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = insRupiah(category.amount),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.16).sp,
                color = InsTextPrimary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        InsProgressBar(
            fraction = percent / 100f,
            color = category.color,
            track = Color(0xB3E2E8F0),
            height = 6
        )
    }
}

@Composable
private fun InsTrendCard(onLastMonthClick: () -> Unit) {
    val average = InsTrendValues.sum() / InsTrendValues.size
    val lastMonth = InsTrendValues[InsTrendValues.size - 2]
    val thisMonth = InsTrendValues.last()
    val isHighest = thisMonth >= (InsTrendValues.maxOrNull() ?: 0L)

    InsCard {
        Column(modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 25.dp, bottom = 21.dp)) {
            InsSectionTitle(
                title = "6-Month Trend (May – Oct 2026)",
                subtitle = "Monthly spending comparison"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Avg: ${insRupiahShort(average)}/mo",
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.4.sp,
                color = InsTextSecondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFE2E7FF))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
            ) {
                InsLineChart(modifier = Modifier.fillMaxSize())
                Text(
                    text = insRupiahShort(thisMonth).removePrefix("Rp "),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = InsChartRed,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InsTrendMonths.forEachIndexed { index, label ->
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp,
                        color = if (index == InsTrendMonths.lastIndex) InsMonthRed else InsTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = InsTrack)
            Spacer(modifier = Modifier.height(17.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xB3F8FAFC))
                    .border(1.dp, InsTrack, RoundedCornerShape(12.dp))
                    .clickable { onLastMonthClick() }
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = InsTextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Last Month",
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp,
                            color = InsTextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = insRupiah(lastMonth),
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = InsTextPrimary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = InsTextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x99ECFDF5))
                    .border(1.dp, Color(0x80A7F3D0), RoundedCornerShape(12.dp))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = InsGreenDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "This Month ($InsMonthShort)",
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.4.sp,
                            color = InsGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = insRupiah(thisMonth),
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = InsGreen
                    )
                }
                if (isHighest) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color(0xB3FEF3C7))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = InsAmberText,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Highest",
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp,
                            color = InsAmberText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsLineChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val maxValue = (InsTrendValues.maxOrNull() ?: 0L).coerceAtLeast(1L).toFloat()
        val left = size.width * 0.06f
        val right = size.width * 0.94f
        val top = size.height * 0.19f
        val bottom = size.height * 0.81f
        val step = (right - left) / (InsTrendValues.size - 1)
        val points = InsTrendValues.mapIndexed { index, value ->
            Offset(left + step * index, bottom - (bottom - top) * (value / maxValue))
        }

        drawLine(
            color = Color(0xFFE2E8F0),
            start = Offset(size.width * 0.04f, bottom),
            end = Offset(size.width * 0.96f, bottom),
            strokeWidth = 1.3.dp.toPx()
        )

        val area = Path().apply {
            moveTo(points.first().x, bottom)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, bottom)
            close()
        }
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(InsChartRed.copy(alpha = 0.25f), InsChartRed.copy(alpha = 0f)),
                startY = top,
                endY = bottom
            )
        )

        val line = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(
            path = line,
            color = InsChartRed,
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        points.forEachIndexed { index, point ->
            val isLast = index == points.lastIndex
            drawCircle(
                color = Color.White,
                radius = if (isLast) 5.dp.toPx() else 3.5.dp.toPx(),
                center = point
            )
            drawCircle(
                color = if (isLast) InsChartRed else Color(0xFFCBD5E1),
                radius = if (isLast) 3.5.dp.toPx() else 2.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
private fun InsTopSpendingCard(
    onSeeAllClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    InsCard {
        Column(modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 25.dp, bottom = 21.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InsSectionTitle(
                        title = "Top Spending",
                        subtitle = "Top 3 highest transactions this month"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSeeAllClick() }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "All Transactions",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.24.sp,
                        color = InsGreenDark
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = InsGreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            InsTopItems.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(thickness = 1.dp, color = InsTrack)
                }
                InsTopRow(item = item, onClick = { onItemClick(item.id) })
            }
        }
    }
}

@Composable
private fun InsTopRow(
    item: InsTopItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(item.iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = InsTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.dateText,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = InsTextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.category,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = item.categoryText,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(item.categoryBackground)
                    .padding(horizontal = 8.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "-" + insRupiah(item.amount),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.16).sp,
                color = InsTextPrimary,
                maxLines = 1
            )
            Text(
                text = "Method: ${item.method}",
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = InsTextMuted
            )
        }
    }
}

@Composable
private fun rememberInsComingSoon(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
    }
}

@Composable
private fun InsTopBar(
    onProfileClick: () -> Unit,
    onMoreClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(InsBackground)
            .height(56.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.maskot_waving),
            contentDescription = "Profile",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(36.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onProfileClick() }
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = InsTextPrimary
            )
        }
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = InsTextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(InsBackground)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(InsRed)
            )
        }
    }
}

private fun insRupiah(amount: Long): String =
    "Rp " + NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(amount)

private fun insRupiahShort(amount: Long): String {
    val value = kotlin.math.abs(amount)
    val text = when {
        value >= 1_000_000L -> insTrimDecimal(value / 1_000_000.0) + "M"
        value >= 1_000L -> Math.round(value / 1_000.0).toString() + "K"
        else -> value.toString()
    }
    return (if (amount < 0) "-Rp " else "Rp ") + text
}

private fun insTrimDecimal(value: Double): String {
    val rounded = Math.round(value * 100) / 100.0
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else rounded.toString()
}