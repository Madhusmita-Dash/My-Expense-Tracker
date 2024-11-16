package com.example.expensetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RowAccountBinding
import com.example.expensetracker.databinding.RowTransactionBinding

class AccountsAdapter(
    private val context: Context,
    private val accountArrayList: ArrayList<Account>,
    private val accountsClickListener: AccountsClickListener
) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    interface AccountsClickListener {
        fun onAccountSelected(account: Account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val binding = RowAccountBinding.inflate(LayoutInflater.from(context), parent, false)
        return AccountsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val account = accountArrayList[position]
        // Make sure you use the correct ID from your layout file
        holder.binding.accountName.text = account.accountName // Use 'transactionCategory' instead of 'accountName'

        holder.itemView.setOnClickListener {
            accountsClickListener.onAccountSelected(account)
        }
    }

    override fun getItemCount(): Int = accountArrayList.size

    inner class AccountsViewHolder(val binding: RowAccountBinding) : RecyclerView.ViewHolder(binding.root)
}
