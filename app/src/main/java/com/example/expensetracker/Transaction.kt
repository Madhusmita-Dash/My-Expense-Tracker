package com.example.expensetracker


import java.util.Date

data class Transaction(
    var type: String = "",
    var category: String = "",
    var account: String = "",
    var note: String = "",
    var date: Date = Date(),
    var amount: Double = 0.0,
    var id: Long = 0L
)
