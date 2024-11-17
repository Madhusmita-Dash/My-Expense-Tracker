package com.example.expensetracker

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App : Application() {
    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm
        val config = RealmConfiguration.Builder(schema = setOf(Transaction::class))
            .name("transaction.realm")
            .build()

        realm = Realm.open(config)
    }
}
