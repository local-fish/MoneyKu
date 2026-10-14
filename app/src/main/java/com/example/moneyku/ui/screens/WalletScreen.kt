package com.example.moneyku.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.R
import java.text.NumberFormat
import java.util.Locale

private val WalBackground = Color(0xFFFAF8FF)
private val WalTextPrimary = Color(0xFF131B2E)
private val WalTextMuted = Color(0xFF6C7A71)
private val WalTextSecondary = Color(0xFF3C4A42)
private val WalGreen = Color(0xFF10B981)
private val WalGreenDark = Color(0xFF006C49)
private val WalTeal = Color(0xFF006A61)
private val WalRed = Color(0xFFBA1A1A)
private val WalDashed = Color(0xFFBBCABF)
private val WalIndigo = Color(0xFF494BD6)
private val WalPillBackground = Color(0xFFEAEDFF)
private val WalCardBorder = Color(0x0D000000)

private data class WalWallet(
    val name: String,
    val type: String,
    val balance: Long,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBackground: Color,
    val iconRing: Color,
    val isActive: Boolean
)

private data class WalDebt(
    val name: String,
    val amount: Long,
    val isOwed: Boolean
)

private const val WalMonthName = "October"
private const val WalTotalBalance = 14850000L
private const val WalPendingFromFriends = 120000L

private val WalWallets = listOf(
    WalWallet("Cash", "Cash", 785000L, Icons.Outlined.Payments, Color(0xFF059669), Color(0xFFECFDF5), Color(0xFFA7F3D0), true),
    WalWallet("OVO", "E-Wallet", 0L, Icons.Outlined.AccountBalance, Color(0xFF9333EA), Color(0xFFFAF5FF), Color(0xFFE9D5FF), false)
)

private val WalDebts = listOf(
    WalDebt("Calvin", 100000L, true)
)

@Composable
fun WalletScreen(
    onAddTransactionClick: () -> Unit = {},
    onSplitBillClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val comingSoon = rememberWalComingSoon()
    var isBalanceHidden by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WalBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            WalTopBar(
                onProfileClick = onProfileClick,
                onMoreClick = comingSoon,
                onNotificationsClick = onNotificationsClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    WalHeroCard(
                        isHidden = isBalanceHidden,
                        onToggleHidden = { isBalanceHidden = !isBalanceHidden },
                        onPendingClick = onSplitBillClick
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WalSectionHeader(
                            title = "Wallet",
                            barColor = WalGreen,
                            manageLabel = "Manage (${WalWallets.size})",
                            onManageClick = comingSoon
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            WalWallets.forEach { wallet ->
                                WalWalletCard(
                                    wallet = wallet,
                                    isHidden = isBalanceHidden,
                                    onClick = comingSoon,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        WalDashedButton(
                            label = "+ Add Wallet",
                            icon = Icons.Outlined.AddCircleOutline,
                            onClick = comingSoon
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WalSectionHeader(
                            title = "Budget",
                            barColor = WalTeal,
                            manageLabel = "Manage",
                            onManageClick = comingSoon
                        )
                        WalEmptyCard(
                            icon = Icons.Outlined.DonutLarge,
                            iconTint = WalTeal,
                            iconBackground = Color(0x6686F2E4),
                            title = "No budget limits yet",
                            subtitle = "Set a monthly spending limit to save money.",
                            buttonLabel = "+ Add Budget",
                            onClick = comingSoon
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WalSectionHeader(
                            title = "Goal",
                            barColor = WalIndigo,
                            pill = "Target",
                            pillColor = WalIndigo,
                            manageLabel = "Manage",
                            onManageClick = comingSoon
                        )
                        WalEmptyCard(
                            icon = Icons.Outlined.Savings,
                            iconTint = WalIndigo,
                            iconBackground = Color(0xFFE1E0FF),
                            title = "Achieve your financial dreams",
                            subtitle = "Create savings goals for holidays, gadgets, or emergencies.",
                            buttonLabel = "+ Add Goal",
                            onClick = comingSoon
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WalSectionHeader(
                            title = "Debt",
                            barColor = WalRed,
                            manageLabel = "Manage (${WalDebts.size})",
                            onManageClick = comingSoon
                        )
                        WalDebts.forEach { debt ->
                            WalDebtCard(
                                debt = debt,
                                isHidden = isBalanceHidden,
                                onClick = comingSoon
                            )
                        }
                        WalDashedButton(
                            label = "+ Add Debt Record",
                            icon = Icons.Outlined.AddCircleOutline,
                            onClick = comingSoon
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WalSectionHeader(
                            title = "Recurring",
                            barColor = WalGreen,
                            pill = "Routine",
                            pillColor = WalGreenDark,
                            manageLabel = "Manage",
                            onManageClick = comingSoon
                        )
                        WalDashedButton(
                            label = "+ Add Recurring Subscription",
                            icon = Icons.Outlined.Autorenew,
                            onClick = comingSoon,
                            verticalPadding = 15.dp
                        )
                    }
                }
            }
        }

        WalAddFab(
            onClick = onAddTransactionClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}

private fun Modifier.walDashedBorder(color: Color, radius: Dp): Modifier = drawBehind {
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
        style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()), 0f)
        )
    )
}

@Composable
private fun WalHeroCard(
    isHidden: Boolean,
    onToggleHidden: () -> Unit,
    onPendingClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(WalGreenDark, WalGreen)))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 32.dp, y = (-32).dp)
                .size(144.dp)
                .blur(12.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Net Balance $WalMonthName",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Icon(
                        imageVector = if (isHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (isHidden) "Show balance" else "Hide balance",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable { onToggleHidden() }
                    )
                }
                Text(
                    text = if (isHidden) "Rp ••••••••" else walRupiah(WalTotalBalance),
                    fontSize = 36.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.9).sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0x4DFBBF24))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x33F59E0B))
                    .clickable { onPendingClick() }
                    .padding(start = 20.dp, end = 20.dp, top = 11.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.HourglassTop,
                        contentDescription = null,
                        tint = Color(0xFFFDE68A),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Waiting from friends:",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Color(0xFFFEF3C7)
                    )
                }
                Text(
                    text = if (isHidden) "Rp •••••" else walRupiah(WalPendingFromFriends),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.24.sp,
                    color = Color(0xFFFDE68A)
                )
            }
        }
    }
}

@Composable
private fun WalSectionHeader(
    title: String,
    barColor: Color,
    manageLabel: String,
    onManageClick: () -> Unit,
    pill: String? = null,
    pillColor: Color = WalGreenDark
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(9999.dp))
                .background(barColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = WalTextPrimary
        )
        if (pill != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pill,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = pillColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(WalPillBackground)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onManageClick() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = manageLabel,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.24.sp,
                color = WalGreenDark
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = WalGreenDark,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun WalWalletCard(
    wallet: WalWallet,
    isHidden: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(128.dp)
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, WalCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        if (wallet.isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 16.dp, y = 16.dp)
                    .size(64.dp)
                    .blur(12.dp)
                    .clip(CircleShape)
                    .background(Color(0x4D89F5E7))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(wallet.iconBackground)
                        .border(1.dp, wallet.iconRing, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = wallet.icon,
                        contentDescription = null,
                        tint = wallet.iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (wallet.isActive) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(WalGreen)
                    )
                } else {
                    Text(
                        text = wallet.type,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.4.sp,
                        color = WalDashed
                    )
                }
            }
            Column {
                Text(
                    text = wallet.name,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    color = WalTextSecondary
                )
                Text(
                    text = if (isHidden) "Rp •••••" else walRupiah(wallet.balance),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.16).sp,
                    color = if (wallet.balance > 0) WalTextPrimary else WalTextMuted,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun WalDashedButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    verticalPadding: Dp = 13.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(WalGreen)
            .clickable { onClick() }
            .padding(horizontal = 17.dp, vertical = verticalPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.14.sp,
            color = Color.White
        )
    }
}

@Composable
private fun WalEmptyCard(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    title: String,
    subtitle: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .walDashedBorder(WalDashed, 12.dp)
            .padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = WalTextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = WalTextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(9999.dp))
                .background(WalGreen)
                .clickable { onClick() }
                .padding(horizontal = 17.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = buttonLabel.removePrefix("+ "),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.24.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun WalDebtCard(
    debt: WalDebt,
    isHidden: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, WalCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFDAD6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = debt.name.take(1).uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF93000A)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = debt.name,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = WalTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isHidden) "Rp •••••" else (if (debt.isOwed) "-" else "+") + walRupiah(debt.amount),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (debt.isOwed) WalRed else WalGreenDark,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun rememberWalComingSoon(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
    }
}

@Composable
private fun WalTopBar(
    onProfileClick: () -> Unit,
    onMoreClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(WalBackground)
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
                tint = WalTextPrimary
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
                    tint = WalTextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(WalBackground)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(WalRed)
            )
        }
    }
}

@Composable
private fun WalAddFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(WalGreen, WalTeal)))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Transaction",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

private fun walRupiah(amount: Long): String =
    "Rp " + NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(amount)