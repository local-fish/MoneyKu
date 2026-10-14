package com.example.moneyku.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.ui.screens.CalendarScreen
import com.example.moneyku.ui.screens.HomeScreen
import com.example.moneyku.ui.screens.InsightScreen
import com.example.moneyku.ui.screens.ProfileScreen
import com.example.moneyku.ui.screens.WalletScreen

private val NavActive = Color(0xFF006C49)
private val NavInactive = Color(0xFF3C4A42)
private const val ProfileTab = 4

sealed class NavTab(val title: String, val selectedIcon: ImageVector, val icon: ImageVector) {
    object Home : NavTab("Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Calendar : NavTab("Calendar", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object Insight : NavTab("Insight", Icons.Filled.Insights, Icons.Outlined.Insights)
    object Wallet : NavTab("Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
}

@Composable
fun MainScreen(
    onOpenReceiptScan: () -> Unit = {},
    onOpenAddTransaction: () -> Unit = {},
    onOpenTransactionDetail: (String) -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onOpenSplitBill: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        NavTab.Home,
        NavTab.Calendar,
        NavTab.Insight,
        NavTab.Wallet
    )

    BackHandler(enabled = selectedTab != 0) { selectedTab = 0 }

    Scaffold(
        bottomBar = {
            MoneyKuBottomBar(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            0 -> HomeScreen(
                onScanReceiptClick = onOpenReceiptScan,
                onSplitBillClick = onOpenSplitBill,
                onAddTransactionClick = onOpenAddTransaction,
                onTransactionClick = onOpenTransactionDetail,
                onNotificationsClick = onOpenNotifications,
                onProfileClick = { selectedTab = ProfileTab },
                modifier = modifier
            )
            1 -> CalendarScreen(
                onAddTransactionClick = onOpenAddTransaction,
                onTransactionClick = onOpenTransactionDetail,
                onNotificationsClick = onOpenNotifications,
                onProfileClick = { selectedTab = ProfileTab },
                modifier = modifier
            )
            2 -> InsightScreen(
                onTransactionClick = onOpenTransactionDetail,
                onSeeAllTransactionsClick = { selectedTab = 1 },
                onNotificationsClick = onOpenNotifications,
                onProfileClick = { selectedTab = ProfileTab },
                modifier = modifier
            )
            3 -> WalletScreen(
                onAddTransactionClick = onOpenAddTransaction,
                onSplitBillClick = onOpenSplitBill,
                onNotificationsClick = onOpenNotifications,
                onProfileClick = { selectedTab = ProfileTab },
                modifier = modifier
            )
            ProfileTab -> ProfileScreen(
                onLogoutClick = onLogout,
                onNotificationsClick = onOpenNotifications,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun MoneyKuBottomBar(
    tabs: List<NavTab>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Color.White)
            .navigationBarsPadding()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            val color = if (isSelected) NavActive else NavInactive
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onTabSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.icon,
                    contentDescription = tab.title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tab.title,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = color,
                    maxLines = 1
                )
            }
        }
    }
}