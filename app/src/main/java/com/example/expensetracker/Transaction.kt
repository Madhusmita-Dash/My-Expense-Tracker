package com.example.expensetracker


import java.util.Date
data class Transaction(
    var account: String = "",
    var category: String = "",
    var date: String = "",  // Ensure the date is in "dd-MMM-yyyy" format
    var amount: Double = 0.0,
    var type: String = ""   // Either Constant.INCOME or Constant.EXPENSE
)
