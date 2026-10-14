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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyku.R
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val CalBackground = Color(0xFFFAF8FF)
private val CalTextPrimary = Color(0xFF131B2E)
private val CalTextMuted = Color(0xFF6C7A71)
private val CalGreen = Color(0xFF10B981)
private val CalGreenDark = Color(0xFF006C49)
private val CalTeal = Color(0xFF006A61)
private val CalRed = Color(0xFFBA1A1A)
private val CalCardBorder = Color(0xCCE2E8F0)
private val CalSky = Color(0xFF0EA5E9)
private val CalSkyText = Color(0xFF0284C7)
private val CalRose = Color(0xFFF43F5E)
private val CalRoseDark = Color(0xFFE11D48)
private val CalOutsideDay = Color(0xFFCBD5E1)
private val CalDivider = Color(0xFFF1F5F9)
private val CalAmber = Color(0xFFFBBF24)
private val CalAmberText = Color(0xFFD97706)
private val CalMint = Color(0xFF6FFBBE)

private enum class CalIcon(val icon: ImageVector, val background: Color, val tint: Color, val ring: Color) {
    GROCERY(Icons.Outlined.ShoppingBag, Color(0xFFFFFBEB), Color(0xFFD97706), Color(0x99FDE68A)),
    TRANSPORT(Icons.Outlined.LocalTaxi, Color(0xFFECFDF5), Color(0xFF059669), Color(0x99A7F3D0)),
    SPLIT(Icons.Outlined.Groups, Color(0xFFFFFBEB), Color(0xFFD97706), Color(0x99FDE68A))
}

private data class CalTransaction(
    val id: String,
    val date: LocalDate,
    val title: String,
    val place: String,
    val time: String,
    val amount: Long,
    val isIncome: Boolean,
    val method: String,
    val icon: CalIcon,
    val isPending: Boolean = false
)

private val CalTransactions = listOf(
    CalTransaction("cal_1", LocalDate.of(2026, 10, 9), "Food & Grocery Shopping", "Ranch Market", "13:20", 240000L, false, "Debit Mandiri", CalIcon.GROCERY),
    CalTransaction("cal_2", LocalDate.of(2026, 10, 9), "GrabCar Transport", "Office to Mall", "08:45", 24000L, false, "GoPay", CalIcon.TRANSPORT),
    CalTransaction("cal_3", LocalDate.of(2026, 10, 8), "Dinner Split Bill", "Waiting from friends", "19:30", 120000L, false, "Pending", CalIcon.SPLIT, isPending = true)
)

private val CalMonthFormat = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
private val CalDayFormat = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale.ENGLISH)
private val CalWeekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@Composable
fun CalendarScreen(
    onAddTransactionClick: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val comingSoon = rememberCalComingSoon()
    var month by remember { mutableStateOf(YearMonth.of(2026, 10)) }
    var selectedDate by remember { mutableStateOf(LocalDate.of(2026, 10, 9)) }

    val monthItems = CalTransactions.filter { YearMonth.from(it.date) == month && !it.isPending }
    val income = monthItems.filter { it.isIncome }.sumOf { it.amount }
    val expense = monthItems.filter { !it.isIncome }.sumOf { it.amount }
    val dayItems = CalTransactions.filter { it.date == selectedDate }.sortedByDescending { it.time }
    val settledDayItems = dayItems.filter { !it.isPending }
    val dayTotal = settledDayItems.sumOf { if (it.isIncome) it.amount else -it.amount }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CalBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CalTopBar(
                onProfileClick = onProfileClick,
                onMoreClick = comingSoon,
                onNotificationsClick = onNotificationsClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    CalMonthHeader(
                        label = month.format(CalMonthFormat),
                        onPrevious = {
                            month = month.minusMonths(1)
                            selectedDate = month.atDay(1)
                        },
                        onNext = {
                            month = month.plusMonths(1)
                            selectedDate = month.atDay(1)
                        }
                    )
                }

                item {
                    CalSummaryCard(income = income, expense = expense)
                }

                item {
                    CalMonthGrid(
                        month = month,
                        selectedDate = selectedDate,
                        onDateClick = { selectedDate = it }
                    )
                }

                item {
                    CalDayHeader(
                        label = selectedDate.format(CalDayFormat).uppercase(Locale.ENGLISH),
                        total = dayTotal,
                        showTotal = settledDayItems.isNotEmpty()
                    )
                }

                if (dayItems.isEmpty()) {
                    item {
                        Text(
                            text = "No transactions on this day",
                            fontSize = 14.sp,
                            color = CalTextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(dayItems, key = { it.id }) { transaction ->
                        CalTransactionCard(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction.id) }
                        )
                    }
                }
            }
        }

        CalAddFab(
            onClick = onAddTransactionClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun CalMonthHeader(
    label: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalRoundButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            description = "Previous month",
            onClick = onPrevious
        )
        Text(
            text = label,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.55).sp,
            color = CalTextPrimary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        CalRoundButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            description = "Next month",
            onClick = onNext
        )
    }
}

@Composable
private fun CalRoundButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xB3E2E8F0), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = CalTextPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun CalSummaryCard(income: Long, expense: Long) {
    val total = income - expense
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CalCardBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 17.dp, horizontal = 13.dp)
    ) {
        CalSummaryItem(
            label = "Income",
            dot = CalSky,
            value = calRupiahShort(income),
            valueColor = CalSkyText,
            modifier = Modifier.weight(1f)
        )
        CalSummaryDivider()
        CalSummaryItem(
            label = "Expense",
            dot = CalRose,
            value = calRupiahShort(-expense),
            valueColor = CalRose,
            modifier = Modifier.weight(1f)
        )
        CalSummaryDivider()
        CalSummaryItem(
            label = "Total",
            dot = CalTextPrimary,
            value = calRupiahShort(total),
            valueColor = CalTextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CalSummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(44.dp)
            .background(CalDivider)
    )
}

@Composable
private fun CalSummaryItem(
    label: String,
    dot: Color,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dot)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.24.sp,
                color = CalTextMuted
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp,
            color = valueColor,
            maxLines = 1
        )
    }
}

@Composable
private fun CalMonthGrid(
    month: YearMonth,
    selectedDate: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDay = month.atDay(1)
    val leading = firstDay.dayOfWeek.value % 7
    val cellCount = ((leading + month.lengthOfMonth() + 6) / 7) * 7
    val start = firstDay.minusDays(leading.toLong())
    val dates = List(cellCount) { start.plusDays(it.toLong()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CalCardBorder, RoundedCornerShape(16.dp))
            .padding(13.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 5.dp)
        ) {
            CalWeekdays.forEachIndexed { index, day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.SemiBold,
                    letterSpacing = 0.24.sp,
                    color = if (index == 0) CalRose else CalTextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = CalDivider)
        Spacer(modifier = Modifier.height(4.dp))

        dates.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    CalDayCell(
                        date = date,
                        isInMonth = YearMonth.from(date) == month,
                        isSelected = date == selectedDate,
                        onClick = { onDateClick(date) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalDayCell(
    date: LocalDate,
    isInMonth: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = CalTransactions.filter { it.date == date }
    val dotColor = when {
        items.isEmpty() -> null
        isSelected -> CalMint
        items.any { it.isPending } -> CalAmber
        else -> CalGreen
    }
    val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
    val label = if (date.dayOfMonth == 1 && isInMonth) "${date.monthValue}/1" else date.dayOfMonth.toString()

    Box(
        modifier = modifier
            .height(52.dp)
            .then(if (isInMonth) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(36.dp)
                .height(44.dp)
                .then(
                    if (isSelected) {
                        Modifier
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(CalGreenDark)
                            .border(2.dp, Color(0x9910B981), RoundedCornerShape(12.dp))
                    } else {
                        Modifier
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = if (label.contains("/")) 12.sp else 14.sp,
                lineHeight = 20.sp,
                fontWeight = when {
                    isSelected -> FontWeight.Bold
                    label.contains("/") -> FontWeight.SemiBold
                    isSunday && isInMonth -> FontWeight.Medium
                    else -> FontWeight.Normal
                },
                color = when {
                    isSelected -> Color.White
                    !isInMonth -> CalOutsideDay
                    label.contains("/") -> CalTextMuted
                    isSunday -> CalRose
                    else -> CalTextPrimary
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (dotColor != null && isInMonth) dotColor else Color.Transparent)
            )
        }
    }
}

@Composable
private fun CalDayHeader(
    label: String,
    total: Long,
    showTotal: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 4.dp, end = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0x3310B981)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(CalGreen)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp,
                color = CalTextPrimary
            )
        }
        if (showTotal) {
            val isNegative = total < 0
            Text(
                text = (if (isNegative) "-" else "+") + calRupiah(kotlin.math.abs(total)),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.24.sp,
                color = if (isNegative) CalRoseDark else CalGreenDark,
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(if (isNegative) Color(0xFFFFF1F2) else Color(0xFFECFDF5))
                    .border(
                        1.dp,
                        if (isNegative) Color(0xCCFECDD3) else Color(0xCCA7F3D0),
                        RoundedCornerShape(9999.dp)
                    )
                    .padding(horizontal = 11.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun CalTransactionCard(
    transaction: CalTransaction,
    onClick: () -> Unit
) {
    val amountText = when {
        transaction.isPending -> calRupiah(transaction.amount)
        transaction.isIncome -> "+" + calRupiah(transaction.amount)
        else -> "-" + calRupiah(transaction.amount)
    }
    val amountColor = when {
        transaction.isPending -> CalAmberText
        transaction.isIncome -> CalGreenDark
        else -> CalRoseDark
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, CalCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(transaction.icon.background)
                .border(1.dp, transaction.icon.ring, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.icon.icon,
                contentDescription = null,
                tint = transaction.icon.tint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = CalTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = transaction.place,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = CalTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(CalOutsideDay)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = transaction.time,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = CalTextMuted
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amountText,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.16).sp,
                color = amountColor,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.method,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.4.sp,
                color = CalTextMuted
            )
        }
    }
}

@Composable
private fun rememberCalComingSoon(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
    }
}

@Composable
private fun CalTopBar(
    onProfileClick: () -> Unit,
    onMoreClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(CalBackground)
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
                tint = CalTextPrimary
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
                    tint = CalTextPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(CalBackground)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(CalRed)
            )
        }
    }
}

@Composable
private fun CalAddFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(CalGreen, CalTeal)))
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

private fun calRupiah(amount: Long): String =
    "Rp " + NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(amount)

private fun calRupiahShort(amount: Long): String {
    val value = kotlin.math.abs(amount)
    val text = when {
        value >= 1_000_000L -> calTrimDecimal(value / 1_000_000.0) + "M"
        value >= 1_000L -> Math.round(value / 1_000.0).toString() + "K"
        else -> value.toString()
    }
    return (if (amount < 0) "-Rp " else "Rp ") + text
}

private fun calTrimDecimal(value: Double): String {
    val rounded = Math.round(value * 100) / 100.0
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else rounded.toString()
}