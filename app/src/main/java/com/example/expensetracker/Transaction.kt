package com.example.expensetracker

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Transaction : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    var account: String = ""
    var category: String = ""
    var date: String = "" // Store date as a String (e.g., "dd MMM, yyyy")
    var amount: Double = 0.0
    var type: String = "" // "INCOME" or "EXPENSE"
}
