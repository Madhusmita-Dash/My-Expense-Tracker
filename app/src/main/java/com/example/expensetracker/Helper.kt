package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.*

object Helper {
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

    fun formatDate(date: Date): String = dateFormat.format(date)

    fun parseDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
}
