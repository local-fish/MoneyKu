package com.example.moneyku.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.R
import com.example.moneyku.data.model.HomeScreenUiState
import java.text.NumberFormat
import java.util.Locale

private val HomeBackground = Color(0xFFFAF8FF)
private val HomeTextPrimary = Color(0xFF131B2E)
private val HomeTextMuted = Color(0xFF6C7A71)
private val HomeRed = Color(0xFFBA1A1A)
private val HomeGreenDark = Color(0xFF006C49)
private val HomeGreen = Color(0xFF10B981)
private val HomeTeal = Color(0xFF006A61)
private val HomeTint = Color(0xFFF2F3FF)
private val HomeCardBorder = Color(0x99E2E8F0)

private enum class HomeTxIcon(val icon: ImageVector, val background: Color, val tint: Color) {
    FOOD(Icons.Outlined.Restaurant, Color(0xFFFFFBEB), Color(0xFFD97706)),
    TRANSPORT(Icons.Outlined.DirectionsCar, Color(0xFFFFF1F2), Color(0xFFE11D48)),
    INCOME(Icons.Outlined.Payments, Color(0xFFECFDF5), Color(0xFF059669)),
    DRINK(Icons.Outlined.LocalCafe, Color(0xFFFFF1F2), Color(0xFFE11D48))
}

private data class HomeTransaction(
    val id: String,
    val title: String,
    val category: String,
    val time: String,
    val amount: Long,
    val isIncome: Boolean,
    val icon: HomeTxIcon
)

private data class HomeDayGroup(
    val dateLabel: String,
    val items: List<HomeTransaction>
) {
    val total: Long
        get() = items.sumOf { if (it.isIncome) it.amount else -it.amount }
}

private const val HomeMonthLabel = "October 2026"
private const val HomeTransactionCount = 31
private const val HomePendingFromFriends = 120000L

private val HomeDayGroups = listOf(
    HomeDayGroup(
        dateLabel = "9 October 2026",
        items = listOf(
            HomeTransaction("tx_1", "Makan Siang Ranch Market", "Restaurant", "12:30 WIB", 240000L, false, HomeTxIcon.FOOD),
            HomeTransaction("tx_2", "Grab Transport", "Transportation", "09:15 WIB", 24000L, false, HomeTxIcon.TRANSPORT)
        )
    ),
    HomeDayGroup(
        dateLabel = "8 October 2026",
        items = listOf(
            HomeTransaction("tx_3", "Freelance UI Pay", "Income", "09:15 WIB", 3500000L, true, HomeTxIcon.INCOME),
            HomeTransaction("tx_4", "Kopi Kenangan", "Drinks", "14:20 WIB", 38000L, false, HomeTxIcon.DRINK)
        )
    )
)

private fun rupiah(amount: Long): String =
    "Rp " + NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(amount)

private fun signedRupiah(amount: Long): String =
    if (amount < 0) "-" + rupiah(-amount) else "+" + rupiah(amount)

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState = HomeScreenUiState(),
    onScanReceiptClick: () -> Unit = {},
    onSplitBillClick: () -> Unit = {},
    onAddTransactionClick: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val comingSoon = rememberHomeComingSoon()
    var query by remember { mutableStateOf("") }
    var isBalanceHidden by remember { mutableStateOf(false) }
    var isAddMenuOpen by remember { mutableStateOf(false) }

    val visibleGroups = if (query.isBlank()) {
        HomeDayGroups
    } else {
        HomeDayGroups
            .map { group ->
                group.copy(items = group.items.filter {
                    it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
                })
            }
            .filter { it.items.isNotEmpty() }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HomeBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeHeader(
                query = query,
                onQueryChange = { query = it },
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    MonthSelector(
                        label = HomeMonthLabel,
                        onPrevious = comingSoon,
                        onNext = comingSoon,
                        onPick = comingSoon
                    )
                }

                item {
                    BalanceHeroCard(
                        monthName = HomeMonthLabel.substringBefore(" "),
                        balance = uiState.totalBalance,
                        pendingFromFriends = HomePendingFromFriends,
                        isHidden = isBalanceHidden,
                        onToggleHidden = { isBalanceHidden = !isBalanceHidden },
                        onPendingClick = onSplitBillClick
                    )
                }

                item {
                    MonthlySummaryCard(
                        transactionCount = HomeTransactionCount,
                        income = uiState.totalIncome,
                        expense = uiState.totalExpense,
                        budgetSpent = uiState.budgetSpent,
                        budgetLimit = uiState.budgetLimit
                    )
                }

                if (visibleGroups.isEmpty()) {
                    item {
                        Text(
                            text = "No transactions found",
                            fontSize = 14.sp,
                            color = HomeTextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(visibleGroups, key = { it.dateLabel }) { group ->
                        DayGroupCard(group = group, onTransactionClick = onTransactionClick)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 19.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(10.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(HomeGreen, HomeTeal)))
                    .clickable { isAddMenuOpen = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            DropdownMenu(
                expanded = isAddMenuOpen,
                onDismissRequest = { isAddMenuOpen = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Add Transaction") },
                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                    onClick = {
                        isAddMenuOpen = false
                        onAddTransactionClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Scan Receipt") },
                    leadingIcon = { Icon(Icons.Outlined.DocumentScanner, contentDescription = null) },
                    onClick = {
                        isAddMenuOpen = false
                        onScanReceiptClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Split Bill") },
                    leadingIcon = { Icon(Icons.Outlined.AltRoute, contentDescription = null) },
                    onClick = {
                        isAddMenuOpen = false
                        onSplitBillClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberHomeComingSoon(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
    }
}

@Composable
private fun HomeHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HomeBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.maskot_waving),
            contentDescription = "MoneyKu",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(36.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onProfileClick() }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .shadow(1.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0x4DBBCABF), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = HomeTextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Transaction",
                        fontSize = 14.sp,
                        color = HomeTextMuted
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = HomeTextPrimary),
                    cursorBrush = SolidColor(HomeGreen),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = HomeTextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-10).dp, y = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(HomeBackground)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(HomeRed)
            )
        }
    }
}

@Composable
private fun MonthSelector(
    label: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onPick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0x33BBCABF), RoundedCornerShape(12.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                tint = HomeTextPrimary
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onPick() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = HomeGreen,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = HomeTextPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = HomeTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }

        IconButton(onClick = onNext, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                tint = HomeTextPrimary
            )
        }
    }
}

@Composable
private fun BalanceHeroCard(
    monthName: String,
    balance: Long,
    pendingFromFriends: Long,
    isHidden: Boolean,
    onToggleHidden: () -> Unit,
    onPendingClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(HomeGreenDark, HomeGreen)))
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
                        text = "Total Net Balance $monthName",
                        fontSize = 12.sp,
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
                    text = if (isHidden) "Rp ••••••••" else rupiah(balance),
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
                        color = Color(0xFFFEF3C7)
                    )
                }
                Text(
                    text = if (isHidden) "Rp •••••" else rupiah(pendingFromFriends),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.24.sp,
                    color = Color(0xFFFDE68A)
                )
            }
        }
    }
}

@Composable
private fun MonthlySummaryCard(
    transactionCount: Int,
    income: Long,
    expense: Long,
    budgetSpent: Long,
    budgetLimit: Long
) {
    val progress = if (budgetLimit > 0) (budgetSpent.toFloat() / budgetLimit).coerceIn(0f, 1f) else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0x0F0F172A), RoundedCornerShape(12.dp))
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Month's Summary",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.24.sp,
                    color = HomeTextMuted
                )
                Text(
                    text = "$transactionCount Transactions",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    color = HomeTeal,
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(Color(0xFFEAEDFF))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            HorizontalDivider(thickness = 1.dp, color = HomeTint)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryTile(
                label = "Income",
                value = "+" + rupiah(income),
                icon = Icons.Default.ArrowDownward,
                accent = HomeGreen,
                background = HomeTint.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                label = "Expense",
                value = "-" + rupiah(expense),
                icon = Icons.Default.ArrowUpward,
                accent = HomeRed,
                background = Color(0x33FFDAD6),
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(HomeTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DonutLarge,
                    contentDescription = null,
                    tint = HomeTeal,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "This Month's Budget",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.14.sp,
                color = HomeTextPrimary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(9999.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(HomeGreen)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = rupiah(budgetSpent), fontSize = 12.sp, color = HomeTextMuted)
            Text(
                text = rupiah(budgetLimit),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = HomeTextPrimary
            )
        }
    }
}

@Composable
private fun SummaryTile(
    label: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    background: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = HomeTextMuted
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp,
            color = accent,
            maxLines = 1
        )
    }
}

@Composable
private fun DayGroupCard(
    group: HomeDayGroup,
    onTransactionClick: (String) -> Unit
) {
    val total = group.total

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, HomeCardBorder), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HomeTint)
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = HomeGreenDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = group.dateLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    color = HomeTextPrimary
                )
            }
            Text(
                text = signedRupiah(total),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp,
                color = if (total < 0) HomeRed else HomeGreenDark
            )
        }
        HorizontalDivider(thickness = 1.dp, color = HomeCardBorder)

        group.items.forEachIndexed { index, tx ->
            if (index > 0) {
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))
            }
            TransactionRow(transaction = tx, onClick = { onTransactionClick(tx.id) })
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: HomeTransaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(transaction.icon.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.icon.icon,
                contentDescription = null,
                tint = transaction.icon.tint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontSize = 14.sp,
                lineHeight = 19.25.sp,
                fontWeight = FontWeight.SemiBold,
                color = HomeTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.category,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = HomeTextMuted
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = (if (transaction.isIncome) "+" else "-") + rupiah(transaction.amount),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.16).sp,
                color = if (transaction.isIncome) HomeGreenDark else HomeRed,
                maxLines = 1
            )
            Text(
                text = transaction.time,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = HomeTextMuted
            )
        }
    }
}

fun formatRupiah(amount: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace(",00", "").replace("Rp", "Rp ")
}