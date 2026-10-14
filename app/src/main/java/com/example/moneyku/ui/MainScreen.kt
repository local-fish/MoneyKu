package com.example.moneyku.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.moneyku.ui.screens.CalendarScreen
import com.example.moneyku.ui.screens.HomeScreen
import com.example.moneyku.ui.screens.InsightScreen
import com.example.moneyku.ui.screens.ProfileScreen
import com.example.moneyku.ui.screens.WalletScreen

sealed class NavTab(val title: String, val icon: ImageVector) {
    object Home : NavTab("Transactions", Icons.Default.Home)
    object Calendar : NavTab("Calendar", Icons.Default.CalendarMonth)
    object Stats : NavTab("Stats", Icons.Default.Insights)
    object Wallet : NavTab("Wallet", Icons.Default.AccountBalanceWallet)
    object Profile : NavTab("Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    onOpenReceiptScan: () -> Unit = {},
    onOpenAddTransaction: () -> Unit = {},
    onOpenTransactionDetail: (String) -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        NavTab.Home,
        NavTab.Calendar,
        NavTab.Stats,
        NavTab.Wallet,
        NavTab.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            0 -> HomeScreen(
                onScanReceiptClick = onOpenReceiptScan,
                onSplitBillClick = { selectedTab = 2 },
                onAddTransactionClick = onOpenAddTransaction,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            1 -> CalendarScreen(
                onAddTransactionClick = onOpenAddTransaction,
                onTransactionClick = onOpenTransactionDetail,
                onNotificationsClick = onOpenNotifications,
                modifier = modifier
            )
            2 -> InsightScreen(
                modifier = modifier
            )
            3 -> WalletScreen(
                onNotificationsClick = onOpenNotifications,
                modifier = modifier
            )
            4 -> ProfileScreen(
                onLogoutClick = onLogout,
                onNotificationsClick = onOpenNotifications,
                modifier = modifier
            )
        }
    }
}
