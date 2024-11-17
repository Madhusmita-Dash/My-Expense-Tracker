package com.example.expensetracker

object Constant {

    // Constant values for transaction types
    const val INCOME = "INCOME"
    const val EXPENSE = "EXPENSE"

    // List to hold categories, initialized in the init block
    var category: ArrayList<Category> = ArrayList()

    // Initialize categories in the init block
    init {
        setCategories()
    }

    // Set categories with images and colors
    private fun setCategories() {
        category.add(Category("Salary", R.drawable.ic_salary, R.color.category1))   // Ensure these resources exist
        category.add(Category("Business", R.drawable.ic_business, R.color.category2))
        category.add(Category("Investment", R.drawable.ic_investment, R.color.category3))
        category.add(Category("Loan", R.drawable.ic_loan, R.color.category4))
        category.add(Category("Rent", R.drawable.ic_rent, R.color.category5))
        category.add(Category("Other", R.drawable.ic_other, R.color.category6))
    }

    // Get details of a category by name
    fun getCategoryDetails(categoryName: String): Category? {
        return category.find { it.categoryName == categoryName }
    }

    // Get color for account based on account name
    fun getAccountsColor(accountName: String): Int {
        return when (accountName) {
            "Bank" -> R.color.bank_color  // Ensure these resources ex
            // ist
            "Cash" -> R.color.cash_color
            "Card" -> R.color.card_color
            else -> R.color.default_color
        }
    }
}