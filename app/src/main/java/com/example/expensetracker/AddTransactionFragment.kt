package com.example.expensetracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.content.ContextCompat
import com.example.expensetracker.databinding.FragmentAddTransactionBinding
import com.example.expensetracker.databinding.ListDialogBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var transaction: Transaction

    interface OnTransactionSavedListener {
        fun onTransactionSaved(transaction: Transaction)
    }

    private var listener: OnTransactionSavedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTransactionSavedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnTransactionSavedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        transaction = Transaction() // Initialize a new transaction object
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        // Listener for the account field
        binding.transactionAccount.setOnClickListener {
            showAccountSelectionDialog()
        }

        // Listener for the category field
        binding.transactionCategory.setOnClickListener {
            showCategorySelectionDialog()
        }

        // Listener for the date field
        binding.transactionDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Listener for the income button
        binding.radioButtonIncome.setOnClickListener {
            binding.radioButtonIncome.setBackgroundResource(R.drawable.income_selector)
            binding.radioButtonExpense.setBackgroundResource(R.drawable.default_selector)
            binding.radioButtonIncome.setTextColor(ContextCompat.getColor(requireContext(), R.color.greenColor))
            binding.radioButtonExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            transaction.type = Constant.INCOME
        }

        // Listener for the expense button
        binding.radioButtonExpense.setOnClickListener {
            binding.radioButtonIncome.setBackgroundResource(R.drawable.default_selector)
            binding.radioButtonExpense.setBackgroundResource(R.drawable.expense_selector)
            binding.radioButtonIncome.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            binding.radioButtonExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.redColor))
            transaction.type = Constant.EXPENSE
        }

        // Save button listener
        binding.saveTransactionBtn.setOnClickListener {
            if (isValidInput()) {
                transaction.account = binding.transactionAccount.text.toString()
                transaction.category = binding.transactionCategory.text.toString()
                transaction.date = binding.transactionDate.text.toString()  // Already in "dd-MMM-yyyy" format
                transaction.amount = binding.transactionAmount.text.toString().toDouble()

                // Notify listener (Homepage) about the saved transaction
                listener?.onTransactionSaved(transaction)

                // Close the fragment
                requireActivity().onBackPressed()
            } else {
                showToast("Please fill all the fields correctly!")
            }
        }
    }

    private fun isValidInput(): Boolean {
        val amountText = binding.transactionAmount.text?.toString()
        val amountValue = amountText?.toDoubleOrNull() ?: -1.0

        return !binding.transactionAmount.text.isNullOrEmpty() &&
                !binding.transactionDate.text.isNullOrEmpty() &&
                !binding.transactionAccount.text.isNullOrEmpty() &&
                !binding.transactionCategory.text.isNullOrEmpty() &&
                amountValue > 0
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                // Set the date to the TextView
                val selectedDate = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(calendar.time)
                binding.transactionDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

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
                transaction.account = account.accountName
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
                transaction.category = category.categoryName
                categoryDialog.dismiss()
            }
        })

        dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        dialogBinding.recyclerView.adapter = categoryAdapter
        categoryDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
