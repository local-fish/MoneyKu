package com.example.moneyku.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val TrkBackground = Color(0xFFFAF8FF)
private val TrkTextPrimary = Color(0xFF131B2E)
private val TrkTextMuted = Color(0xFF6C7A71)
private val TrkTextSecondary = Color(0xFF3C4A42)
private val TrkGreen = Color(0xFF10B981)
private val TrkGreenDark = Color(0xFF006C49)
private val TrkGreenSoft = Color(0xFFECFDF5)
private val TrkRed = Color(0xFFBA1A1A)
private val TrkRose = Color(0xFFE11D48)
private val TrkTint = Color(0xFFF2F3FF)
private val TrkToggleTrack = Color(0xFFEAEDFF)
private val TrkBorder = Color(0xFFE2E8F0)
private val TrkKey = Color(0xFFF2F3FF)

private data class TrkCategory(val name: String, val icon: ImageVector)

private val TrkCategories = listOf(
    TrkCategory("Food & Drinks", Icons.Outlined.Restaurant),
    TrkCategory("Transport", Icons.Outlined.DirectionsCar),
    TrkCategory("Shopping", Icons.Outlined.ShoppingBag),
    TrkCategory("Bills", Icons.AutoMirrored.Outlined.ReceiptLong),
    TrkCategory("Entertainment", Icons.Outlined.Movie),
    TrkCategory("Health", Icons.Outlined.MedicalServices),
    TrkCategory("Education", Icons.Outlined.School),
    TrkCategory("Others", Icons.Outlined.MoreHoriz)
)

private val TrkWallets = listOf("Cash", "BCA", "GoPay", "OVO")
private val TrkPaymentMethods = listOf("Cash", "Debit BCA", "Debit Mandiri", "GoPay", "OVO", "QRIS")
private const val TrkMaxDigits = 12
private const val TrkSampleAttachment = "struk_lunch.jpg"
private val TrkDateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
private val TrkDayDateFormat = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)

private fun trkGroupDigits(digits: String): String =
    NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(digits.toLongOrNull() ?: 0L)

private fun trkDateLabel(date: LocalDate): String =
    if (date == LocalDate.now()) "Today, " + date.format(TrkDateFormat) else date.format(TrkDayDateFormat)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    onSaveSuccess: () -> Unit,
    onBackClick: () -> Unit = onSaveSuccess,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isExpense by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("50000") }
    var wallet by remember { mutableStateOf(TrkWallets.first()) }
    var isWalletMenuOpen by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(TrkCategories.first().name) }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("Team lunch") }
    var paymentMethod by remember { mutableStateOf(TrkPaymentMethods.first()) }
    var isPaymentMenuOpen by remember { mutableStateOf(false) }
    var attachment by remember { mutableStateOf<String?>(TrkSampleAttachment) }
    var isSplitBill by remember { mutableStateOf(true) }

    BackHandler(onBack = onBackClick)

    fun reset() {
        isExpense = true
        amount = "0"
        wallet = TrkWallets.first()
        category = TrkCategories.first().name
        date = LocalDate.now()
        note = ""
        paymentMethod = TrkPaymentMethods.first()
        attachment = null
        isSplitBill = false
    }

    fun appendDigits(digits: String) {
        val next = (if (amount == "0") "" else amount) + digits
        val trimmed = next.trimStart('0').ifEmpty { "0" }
        if (trimmed.length <= TrkMaxDigits) amount = trimmed
    }

    fun deleteDigit() {
        amount = amount.dropLast(1).ifEmpty { "0" }
    }

    val canSave = (amount.toLongOrNull() ?: 0L) > 0L

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrkBackground)
    ) {
        TrkTopBar(onBackClick = onBackClick, onResetClick = { reset() })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TrkTypeToggle(isExpense = isExpense, onChange = { isExpense = it })

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(TrkGreenSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = TrkGreenDark,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isExpense) "Expense Amount" else "Income Amount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TrkTextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Rp",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrkTextPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = trkGroupDigits(amount),
                        fontSize = 40.sp,
                        lineHeight = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp,
                        color = TrkTextPrimary,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 2.dp, bottom = 8.dp)
                            .width(2.dp)
                            .height(34.dp)
                            .background(TrkGreen)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color.White)
                            .border(1.dp, TrkBorder, RoundedCornerShape(9999.dp))
                            .clickable { isWalletMenuOpen = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = TrkGreenDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = wallet,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TrkTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TrkTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = isWalletMenuOpen,
                        onDismissRequest = { isWalletMenuOpen = false }
                    ) {
                        TrkWallets.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    wallet = option
                                    isWalletMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SELECT CATEGORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                        color = TrkTextSecondary
                    )
                    Text(
                        text = "${TrkCategories.size} Categories",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TrkGreenDark
                    )
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(TrkCategories, key = { it.name }) { item ->
                        TrkCategoryTile(
                            category = item,
                            isSelected = item.name == category,
                            onClick = { category = item.name }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrkDetailRow(
                    icon = Icons.Outlined.CalendarMonth,
                    label = "Date",
                    onClick = { isDatePickerOpen = true },
                    trailing = {
                        Icon(
                            imageVector = Icons.Outlined.EditCalendar,
                            contentDescription = "Change date",
                            tint = TrkGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                ) {
                    Text(
                        text = trkDateLabel(date),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TrkTextPrimary
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = TrkTint)

                TrkDetailRow(
                    icon = Icons.Outlined.EditNote,
                    label = "Note",
                    trailing = {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = TrkTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                ) {
                    Box {
                        if (note.isEmpty()) {
                            Text(
                                text = "Add a note",
                                fontSize = 14.sp,
                                color = TrkTextMuted
                            )
                        }
                        BasicTextField(
                            value = note,
                            onValueChange = { note = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TrkTextPrimary
                            ),
                            cursorBrush = SolidColor(TrkGreen),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = TrkTint)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Payment Method",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TrkTextMuted
                    )
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, TrkBorder, RoundedCornerShape(10.dp))
                                .clickable { isPaymentMenuOpen = true }
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = paymentMethod,
                                fontSize = 14.sp,
                                color = TrkTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = TrkTextMuted
                            )
                        }
                        DropdownMenu(
                            expanded = isPaymentMenuOpen,
                            onDismissRequest = { isPaymentMenuOpen = false }
                        ) {
                            TrkPaymentMethods.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        paymentMethod = option
                                        isPaymentMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = TrkTint)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            if (attachment == null) {
                                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TrkIconBox(icon = Icons.Outlined.PhotoCamera)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Attach Receipt Photo",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TrkTextPrimary
                        )
                        Text(
                            text = if (attachment != null) "1 file attached ($attachment)" else "Tap to attach a receipt",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (attachment != null) TrkGreenDark else TrkTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (attachment != null) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TrkTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                                contentDescription = null,
                                tint = TrkTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(2.dp)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(TrkTextSecondary)
                                    .clickable { attachment = null },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove attachment",
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = TrkTint)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TrkIconBox(icon = Icons.Outlined.Groups)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Split bill with friends",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TrkTextPrimary
                        )
                        Text(
                            text = if (isSplitBill) "Split Bill enabled automatically" else "Turn on to share this bill",
                            fontSize = 10.sp,
                            color = TrkTextMuted
                        )
                    }
                    Switch(
                        checked = isSplitBill,
                        onCheckedChange = { isSplitBill = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TrkGreen,
                            checkedBorderColor = TrkGreen
                        )
                    )
                }
            }

            TrkNumpad(
                onDigits = { appendDigits(it) },
                onDelete = { deleteDigit() }
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TrkBackground)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(if (canSave) 8.dp else 0.dp, RoundedCornerShape(9999.dp))
                    .clip(RoundedCornerShape(9999.dp))
                    .background(if (canSave) TrkGreen else TrkBorder)
                    .clickable(enabled = canSave) { onSaveSuccess() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = if (canSave) Color.White else TrkTextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Transaction",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canSave) Color.White else TrkTextMuted
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Data syncs automatically to MoneyKu balance and analytics",
                fontSize = 10.sp,
                color = TrkTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }

    if (isDatePickerOpen) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let {
                            date = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                        }
                        isDatePickerOpen = false
                    }
                ) {
                    Text("OK", color = TrkGreenDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Cancel", color = TrkTextMuted)
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

@Composable
private fun TrkTopBar(
    onBackClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TrkTextPrimary
            )
        }
        Text(
            text = "Add Transaction",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TrkTextPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = "Reset",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TrkRed,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onResetClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun TrkTypeToggle(
    isExpense: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9999.dp))
            .background(TrkToggleTrack)
            .padding(4.dp)
    ) {
        TrkToggleOption(
            label = "Expense",
            icon = Icons.Default.NorthEast,
            isSelected = isExpense,
            selectedColor = TrkRose,
            onClick = { onChange(true) },
            modifier = Modifier.weight(1f)
        )
        TrkToggleOption(
            label = "Income",
            icon = Icons.Default.ArrowDownward,
            isSelected = !isExpense,
            selectedColor = TrkGreenDark,
            onClick = { onChange(false) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TrkToggleOption(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) selectedColor else TrkTextSecondary
    Row(
        modifier = modifier
            .height(36.dp)
            .then(if (isSelected) Modifier.shadow(1.dp, RoundedCornerShape(9999.dp)) else Modifier)
            .clip(RoundedCornerShape(9999.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun TrkCategoryTile(
    category: TrkCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isSelected) TrkGreenSoft else Color.White)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) TrkGreenDark else TrkBorder,
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = if (isSelected) TrkGreenDark else TrkTextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TrkGreenDark else TrkTextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TrkIconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(TrkTint),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TrkGreenDark,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun TrkDetailRow(
    icon: ImageVector,
    label: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrkIconBox(icon = icon)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TrkTextMuted
            )
            content()
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun TrkNumpad(
    onDigits: (String) -> Unit,
    onDelete: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("000", "0", "DEL")
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TrkKey)
                            .clickable { if (key == "DEL") onDelete() else onDigits(key) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "DEL") {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                contentDescription = "Delete",
                                tint = TrkTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = key,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = TrkTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}