package com.example.expensetracker

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RowTransactionBinding

class TransactionsAdapter(
    private val context: Context,
    private val transactions: ArrayList<Transaction>,
    private val onTransactionDeleted: (Transaction) -> Unit // This is the missing parameter
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

        // Set the amount color based on the transaction type (INCOME or EXPENSE)
        if (transaction.type == Constant.INCOME) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.dark_green))
        } else if (transaction.type == Constant.EXPENSE) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor))
        }

        // Set the OnLongClickListener for the item view
        holder.itemView.setOnLongClickListener {
            showDeleteDialog(transaction, position)
            true // Indicates the long-click was handled
        }
    }

    override fun getItemCount(): Int = transactions.size

    private fun showDeleteDialog(transaction: Transaction, position: Int) {
        // Create an AlertDialog to confirm deletion
        val deleteDialog = AlertDialog.Builder(context)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { dialogInterface, i ->
                // Call the method to delete the transaction
                deleteTransaction(transaction, position)
            }
            .setNegativeButton("No") { dialogInterface, i ->
                // Dismiss the dialog if 'No' is clicked
                dialogInterface.dismiss()
            }
            .create()

        // Show the dialog
        deleteDialog.show()
    }

    private fun deleteTransaction(transaction: Transaction, position: Int) {
        // Remove the transaction from the list
        transactions.removeAt(position)
        // Notify the adapter that an item was removed
        notifyItemRemoved(position)

        // Call the callback to notify the deletion
        onTransactionDeleted(transaction)
    }

    inner class TransactionViewHolder(val binding: RowTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
