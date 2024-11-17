package com.example.expensetracker

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App : Application() {

    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(schema = setOf(Transaction::class))
            .name("expensetracker.realm")
            .schemaVersion(1)
            .build()

        realm = Realm.open(config)
    }
}
