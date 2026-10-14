package com.example.moneyku.ui.components

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    fun formatRupiah(amount: BigDecimal): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(amount).replace("Rp", "Rp ")
    }
}
