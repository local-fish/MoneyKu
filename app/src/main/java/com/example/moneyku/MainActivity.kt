package com.example.moneyku

import android.os.Bundle
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.moneyku.ui.MainScreen
import com.example.moneyku.ui.screens.LoginScreen
import com.example.moneyku.ui.screens.NotificationsScreen
import com.example.moneyku.ui.screens.OnboardingScreen
import com.example.moneyku.ui.screens.ReceiptVerificationScreen
import com.example.moneyku.ui.screens.RegisterScreen
import com.example.moneyku.ui.screens.SplitBillScreen
import com.example.moneyku.ui.screens.TrackScreen
import com.example.moneyku.ui.screens.TransactionDetailScreen
import com.example.moneyku.ui.theme.MoneyKuTheme

sealed class AppDestination {
    object Onboarding : AppDestination()
    object Login : AppDestination()
    object Register : AppDestination()
    object Main : AppDestination()
    object ReceiptScan : AppDestination()
    object AddTransaction : AppDestination()
    data class TransactionDetail(val id: String) : AppDestination()
    object SplitBill : AppDestination()
    object Notifications : AppDestination()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyKuTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoneyKuAppNavigationShell()
                }
            }
        }
    }
}

@Composable
fun MoneyKuAppNavigationShell() {
    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.Onboarding) }

    when (val dest = currentDestination) {
        is AppDestination.Onboarding -> {
            OnboardingScreen(
                onNextClick = { currentDestination = AppDestination.Login },
                onLoginClick = { currentDestination = AppDestination.Login },
                onSkipClick = { currentDestination = AppDestination.Login }
            )
        }
        is AppDestination.Login -> {
            LoginScreen(
                onLoginSuccess = { currentDestination = AppDestination.Main },
                onSignUpClick = { currentDestination = AppDestination.Register }
            )
        }
        is AppDestination.Register -> {
            RegisterScreen(
                onRegisterSuccess = { currentDestination = AppDestination.Main },
                onLoginClick = { currentDestination = AppDestination.Login },
                onBackClick = { currentDestination = AppDestination.Login }
            )
        }
        is AppDestination.Main -> {
            MainScreen(
                onOpenReceiptScan = { currentDestination = AppDestination.ReceiptScan },
                onOpenAddTransaction = { currentDestination = AppDestination.AddTransaction },
                onOpenTransactionDetail = { id -> currentDestination = AppDestination.TransactionDetail(id) },
                onOpenNotifications = { currentDestination = AppDestination.Notifications },
                onOpenSplitBill = { currentDestination = AppDestination.SplitBill },
                onLogout = { currentDestination = AppDestination.Login }
            )
        }
        is AppDestination.ReceiptScan -> {
            ReceiptVerificationScreen(
                onCloseClick = { currentDestination = AppDestination.Main },
                onSaveAndSplit = { currentDestination = AppDestination.SplitBill },
                onRetakePhoto = {},
                onManualEdit = {}
            )
        }
        is AppDestination.AddTransaction -> {
            TrackScreen(
                onSaveSuccess = { currentDestination = AppDestination.Main }
            )
        }
        is AppDestination.TransactionDetail -> {
            TransactionDetailScreen(
                transactionId = dest.id,
                onBackClick = { currentDestination = AppDestination.Main },
                onEditClick = { currentDestination = AppDestination.AddTransaction },
                onDeleteClick = { currentDestination = AppDestination.Main },
                onViewSplitBillClick = { currentDestination = AppDestination.SplitBill }
            )
        }
        is AppDestination.SplitBill -> {
            SplitBillScreen(
                onBackClick = { currentDestination = AppDestination.Main },
                onShareClick = {}
            )
        }
        is AppDestination.Notifications -> {
            NotificationsScreen(
                onBackClick = { currentDestination = AppDestination.Main }
            )
        }
    }
}
