package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun parseDate(dateString: String): Date? {
        return try {
            val formattedDateString = if (!dateString.contains("-")) {
                "01-$dateString"  // Add a default day if only "MMM-yyyy" is provided
            } else {
                dateString
            }

            val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            dateFormat.parse(formattedDateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
