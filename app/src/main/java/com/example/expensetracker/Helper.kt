package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {

    // Function to format date to a string in "dd MMM, yyyy" format
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    // Function to parse a date string into a Date object
    fun parseDate(dateString: String): Date? {
        return try {
            // Check if the dateString is in "MMM-yyyy" format (missing day)
            val formattedDateString = if (!dateString.contains("-")) {
                "01-$dateString"  // Add a default day (01) if only "MMM-yyyy" is provided
            } else {
                dateString
            }

            // Use "dd-MMM-yyyy" format to parse the date
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            dateFormat.parse(formattedDateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null  // Return null if the date string can't be parsed
        }
    }
}
