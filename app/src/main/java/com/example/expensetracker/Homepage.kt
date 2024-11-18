package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Homepage : AppCompatActivity(), AddTransactionFragment.OnTransactionSavedListener {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var currentDateTextView: TextView
    private lateinit var incomeTextView: TextView
    private lateinit var expenseTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var backArrow: ImageView
    private lateinit var forwardArrow: ImageView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionsAdapter: TransactionsAdapter

    private lateinit var auth: FirebaseAuth

    private val calendar = Calendar.getInstance()
    private val transactions = ArrayList<Transaction>()
    private lateinit var realm: Realm

    private var isMonthlyView = false // Flag to determine the current view mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        auth = FirebaseAuth.getInstance()
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Set click listener for logout button
        logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    auth.signOut()
                    val loginIntent = Intent(this, Login::class.java)
                    startActivity(loginIntent)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        realm = (application as App).realm // Get Realm instance from the Application class

        // Initializing views
        toolbar = findViewById(R.id.toolbar)
        tabLayout = findViewById(R.id.tabLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        currentDateTextView = findViewById(R.id.currentdate)
        incomeTextView = findViewById(R.id.textView13)
        expenseTextView = findViewById(R.id.textView11)
        totalTextView = findViewById(R.id.textView9)
        backArrow = findViewById(R.id.imageView)
        forwardArrow = findViewById(R.id.imageView3)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)

        setSupportActionBar(toolbar)

        // Setup RecyclerView Adapter
        transactionsAdapter = TransactionsAdapter(this, transactions) { transaction ->
            deleteTransaction(transaction)
        }
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.adapter = transactionsAdapter

        // Setup Date display and navigation
        updateDateTextView()

        // Setup TabLayout and BottomNavigationView
        setupTabLayout()
        setupBottomNavigation()

        floatingActionButton.setOnClickListener {
            openFragment(AddTransactionFragment())
        }

        backArrow.setOnClickListener { updateDate(-1) }
        forwardArrow.setOnClickListener { updateDate(1) }

        loadTransactionsForCurrentDate()
    }

    override fun onTransactionSaved(transaction: Transaction) {
        lifecycleScope.launch {
            realm.write {
                transaction.date = Helper.formatDate(calendar.time)
                copyToRealm(transaction)
            }

            transactions.add(transaction)
            transactionsAdapter.notifyItemInserted(transactions.size - 1)
            updateIncomeExpenseTotals()
        }
    }

    private fun updateIncomeExpenseTotals() {
        var totalIncome = 0.0
        var totalExpense = 0.0

        for (transaction in transactions) {
            if (transaction.type == Constant.INCOME) {
                totalIncome += transaction.amount
            } else {
                totalExpense += transaction.amount
            }
        }

        incomeTextView.text = totalIncome.toString()
        expenseTextView.text = totalExpense.toString()
        totalTextView.text = (totalIncome - totalExpense).toString()
    }

    private fun updateDate(increment: Int) {
        if (isMonthlyView) {
            calendar.add(Calendar.MONTH, increment)
        } else {
            calendar.add(Calendar.DATE, increment)
        }
        updateDateTextView()
        if (isMonthlyView) {
            loadTransactionsForCurrentMonth()
        } else {
            loadTransactionsForCurrentDate()
        }
    }

    private fun updateDateTextView() {
        val dateFormat = if (isMonthlyView) {
            SimpleDateFormat("MMM yyyy", Locale.getDefault()) // Month and year format
        } else {
            SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) // Full date format
        }
        currentDateTextView.text = dateFormat.format(calendar.time)
    }

    private fun loadTransactionsForCurrentDate() {
        lifecycleScope.launch {
            val formattedDate = Helper.formatDate(calendar.time)
            val transactionsList = realm.query<Transaction>("date == $0", formattedDate).find()

            transactions.clear()
            transactions.addAll(transactionsList)
            transactionsAdapter.notifyDataSetChanged()

            updateIncomeExpenseTotals()
        }
    }

    private fun loadTransactionsForCurrentMonth() {
        lifecycleScope.launch {
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            val transactionsList = realm.query<Transaction>().find().filter {
                val transactionDate = Helper.parseDate(it.date)
                transactionDate != null && transactionDate.month == currentMonth && transactionDate.year == currentYear - 1900
            }

            transactions.clear()
            transactions.addAll(transactionsList)
            transactionsAdapter.notifyDataSetChanged()

            updateIncomeExpenseTotals()
        }
    }

    private fun deleteTransaction(transaction: Transaction) {
        lifecycleScope.launch {
            realm.write {
                val transactionToDelete = query<Transaction>("id == $0", transaction.id).first()
                transactionToDelete?.let { delete(it) }
            }

            transactions.remove(transaction)
            transactionsAdapter.notifyDataSetChanged()
            Toast.makeText(this@Homepage, "Transaction deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Monthly" -> {
                        isMonthlyView = true
                        updateDateTextView()
                        loadTransactionsForCurrentMonth()
                    }
                    "Daily" -> {
                        isMonthlyView = false
                        updateDateTextView()
                        loadTransactionsForCurrentDate()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transactions -> showToast("Home clicked")
                R.id.stats -> showToast("Status clicked")
                R.id.accounts -> showToast("Account clicked")
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}