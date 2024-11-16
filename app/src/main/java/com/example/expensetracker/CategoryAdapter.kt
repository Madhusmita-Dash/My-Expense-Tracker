package com.example.expensetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.SampleCategoryItemBinding
import com.example.expensetracker.Category

class CategoryAdapter(
    private val context: Context,
    private val categories: ArrayList<Category>,
    private val categoryClickListener: CategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // Interface for click listener
    interface CategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = SampleCategoryItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.apply {
            categoryText.text = category.categoryName
            categoryIcon.setImageResource(category.categoryImage)
            categoryIcon.backgroundTintList = context.getColorStateList(category.categoryColor)  // Set the background color for the icon
        }

        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClicked(category)  // This triggers the fragment's method
        }
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(val binding: SampleCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
