package com.example.expensetracker

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class Homepage : AppCompatActivity() {



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
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize views
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

        // Set toolbar
        setSupportActionBar(toolbar)

        // Set initial date
        updateDateTextView()

        // Setup TabLayout and navigation listeners
        setupTabLayout()
        setupBottomNavigation()

        // Set up floating action button listener to open fragment
        floatingActionButton.setOnClickListener {
            openFragment(AddTransactionFragment())
        }

        // Set up back and forward arrow listeners
        backArrow.setOnClickListener {
            changeDate(-1) // Go to the previous day
        }

        forwardArrow.setOnClickListener {
            changeDate(1) // Go to the next day
        }

        // Dummy values for income, expense, and total (Update with actual data)
        setIncomeExpenseTotal(1000.0, 500.0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> showToast("Daily tab selected")
                    1 -> showToast("Monthly tab selected")
                    2 -> showToast("Calendar tab selected")
                    3 -> showToast("Summary tab selected")
                    4 -> showToast("Notes tab selected")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transactions -> showToast("Transactions selected")
                R.id.stats -> showToast("Stats selected")
                R.id.accounts -> showToast("Accounts selected")
            }
            true
        }
    }

    private fun updateDateTextView() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        currentDateTextView.text = dateFormat.format(calendar.time)
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_YEAR, days)
        updateDateTextView()
    }

    private fun setIncomeExpenseTotal(income: Double, expense: Double) {
        incomeTextView.text = String.format("%.2f", income)
        expenseTextView.text = String.format("%.2f", expense)
        totalTextView.text = String.format("%.2f", income - expense)
    }

    private fun openFragment(fragment: Fragment) {
        // Open fragment as BottomSheet
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
