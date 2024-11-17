package com.example.expensetracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentAddTransactionBinding
import com.example.expensetracker.databinding.ListDialogBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {

    private lateinit var binding: FragmentAddTransactionBinding
    private var selectedDate: Date? = null

    interface OnTransactionSavedListener {
        fun onTransactionSaved(transaction: Transaction)
    }

    companion object {
        fun newInstance(selectedDate: Date): AddTransactionFragment {
            val fragment = AddTransactionFragment()
            val bundle = Bundle().apply {
                putSerializable("selectedDate", selectedDate)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        selectedDate = arguments?.getSerializable("selectedDate") as? Date

        // Set the selected date in the TextView
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        binding.transactionDate.setText(selectedDate?.let { dateFormat.format(it) } ?: dateFormat.format(Date()))

        // Set default values for EditTexts (Optional)
        binding.transactionAccount.setText("")  // Set initial text value
        binding.transactionCategory.setText("") // Set initial text value
        binding.transactionAmount.setText("") // Set initial text value

        // Show account selection dialog when account EditText is clicked
        binding.transactionAccount.setOnClickListener {
            showAccountSelectionDialog()
        }

        // Show category selection dialog when category EditText is clicked
        binding.transactionCategory.setOnClickListener {
            showCategorySelectionDialog()
        }

        binding.saveTransactionBtn.setOnClickListener {
            val transaction = Transaction().apply {
                account = binding.transactionAccount.text.toString() // Retrieve account
                category = binding.transactionCategory.text.toString() // Retrieve category
                amount = binding.transactionAmount.text.toString().toDoubleOrNull() ?: 0.0 // Convert to Double safely
                type = if (binding.radioButtonIncome.isChecked) Constant.INCOME else Constant.EXPENSE
                date = Helper.formatDate(selectedDate ?: Date()) // Use selected date or current date
            }

            // Notify the activity that a transaction was saved
            (activity as? OnTransactionSavedListener)?.onTransactionSaved(transaction)

            // Close the fragment after saving the transaction
            activity?.onBackPressed()
        }

        return binding.root
    }

    // Function to show account selection dialog
    private fun showAccountSelectionDialog() {
        val dialogBinding = ListDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val accountsDialog = AlertDialog.Builder(requireContext()).create()
        accountsDialog.setView(dialogBinding.root)

        val accounts = arrayListOf(
            Account("Cash", 0.0),
            Account("Bank", 0.0),
            Account("PayTM", 0.0),
            Account("EasyPaisa", 0.0),
            Account("Other", 0.0)
        )

        val adapter = AccountsAdapter(requireContext(), accounts, object : AccountsAdapter.AccountsClickListener {
            override fun onAccountSelected(account: Account) {
                binding.transactionAccount.setText(account.accountName)
                accountsDialog.dismiss()
            }
        })

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = adapter
        dialogBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
        accountsDialog.show()
    }

    // Function to show category selection dialog
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

        val categoryAdapter = CategoryAdapter(requireContext(), categories, object : CategoryAdapter.CategoryClickListener {
            override fun onCategoryClicked(category: Category) {
                binding.transactionCategory.setText(category.categoryName)
                categoryDialog.dismiss()
            }
        })

        dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        dialogBinding.recyclerView.adapter = categoryAdapter
        categoryDialog.show()
    }
}
