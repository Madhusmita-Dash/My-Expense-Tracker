package com.example.expensetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RowTransactionBinding

class TransactionsAdapter(
    private val context: Context,
    private val transactions: ArrayList<Transaction>
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = RowTransactionBinding.inflate(LayoutInflater.from(context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        // Set data in the view
        holder.binding.transactionAmount.text = "$${transaction.amount}"
        holder.binding.accountLbl.text = transaction.account
        holder.binding.transactionCategory.text = transaction.category

        // Parse the date string to Date object and then format it
        val transactionDate = Helper.parseDate(transaction.date)
        holder.binding.transactionDate.text = transactionDate?.let {
            Helper.formatDate(it)
        } ?: "Date not available"




        // Get category details from Constants
        val transactionCategory = Constant.getCategoryDetails(transaction.category)

        // Set category icon and background color
        transactionCategory?.let {
            holder.binding.categoryIcon.setImageResource(it.categoryImage)
            holder.binding.categoryIcon.setBackgroundTintList(
                context.getColorStateList(it.categoryColor)
            )
        }

        // Set account label background color based on account
        holder.binding.accountLbl.setBackgroundTintList(
            context.getColorStateList(Constant.getAccountsColor(transaction.account))
        )

        // Set the amount color based on the transaction type (INCOME or EXPENSE)
        if (transaction.type == Constant.INCOME) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.dark_green))
        } else if (transaction.type == Constant.EXPENSE) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor))
        }
    }


    override fun getItemCount(): Int = transactions.size

    inner class TransactionViewHolder(val binding: RowTransactionBinding) : RecyclerView.ViewHolder(binding.root)
}
