package com.example.expensetracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentAddTransactionBinding
import com.example.expensetracker.databinding.ListDialogBinding
import java.text.SimpleDateFormat
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var transaction: Transaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        // Initialize transaction object
        transaction = Transaction()

        // Set up the listeners for the buttons
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        // Listener for the account field
        binding.account.setOnClickListener {
            showAccountSelectionDialog()
        }

        // Listener for the category field
        binding.category.setOnClickListener {
            showCategorySelectionDialog()
        }

        // Listener for the date field
        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up the listeners for income and expense buttons
        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.setBackgroundResource(R.drawable.income_selector)
            binding.expenseBtn.setBackgroundResource(R.drawable.default_selector)
            binding.expenseBtn.setTextColor(resources.getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(resources.getColor(R.color.greenColor))

            transaction.type = Constants.INCOME
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.setBackgroundResource(R.drawable.default_selector)
            binding.expenseBtn.setBackgroundResource(R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(resources.getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(resources.getColor(R.color.redColor))

            transaction.type = Constants.EXPENSE
        }
    }

    private fun showAccountSelectionDialog() {
        // Inflate the dialog view
        val dialogBinding = ListDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val accountsDialog = AlertDialog.Builder(requireContext()).create()
        accountsDialog.setView(dialogBinding.root)

        // Create a list of accounts
        val accounts = arrayListOf(
            Account("Cash", 0.0),
            Account("Bank", 0.0),
            Account("PayTM", 0.0),
            Account("EasyPaisa", 0.0),
            Account("Other", 0.0)
        )

        // Set up the adapter for the RecyclerView
        val adapter = AccountsAdapter(
            requireContext(),
            accounts,
            object : AccountsAdapter.AccountsClickListener {
                override fun onAccountSelected(account: Account) {
                    // Set the selected account name to the input field
                    binding.account.setText(account.accountName)
                    // Assign the account to the transaction object
                    transaction.account = account.accountName
                    // Dismiss the dialog
                    accountsDialog.dismiss()
                }
            }
        )

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = adapter

        // Optional: Add item decoration for better UI
        val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dialogBinding.recyclerView.addItemDecoration(itemDecoration)

        // Show the dialog
        accountsDialog.show()
    }

    private fun showCategorySelectionDialog() {
        val dialogBinding = ListDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val categoryDialog = AlertDialog.Builder(requireContext()).create()
        categoryDialog.setView(dialogBinding.root)

        val categories = arrayListOf(
            Category("Salary", R.drawable.ic_salary, R.color.dark_green),
            Category("Business", R.drawable.ic_business, R.color.category4),
            Category("Investment", R.drawable.ic_investment, R.color.yellow),
            Category("Loan", R.drawable.ic_loan, R.color.category2),
            Category("Rent", R.drawable.ic_rent, R.color.category3),
            Category("Other", R.drawable.ic_other, R.color.greenColor)
        )

        val categoryAdapter = CategoryAdapter(
            requireContext(),
            categories,
            object : CategoryAdapter.CategoryClickListener {
                override fun onCategoryClicked(category: Category) {
                    binding.category.setText(category.categoryName)
                    transaction.category = category.categoryName
                    categoryDialog.dismiss()
                }
            }
        )

        dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        dialogBinding.recyclerView.adapter = categoryAdapter
        categoryDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd-MMM, yyyy", Locale.getDefault())
                binding.date.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data classes for Transaction and Account
    data class Transaction(var account: String = "", var category: String = "", var type: String = "")

    object Constants {
        const val INCOME = "Income"
        const val EXPENSE = "Expense"
    }
}
