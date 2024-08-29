package com.gultekinahmetabdullah.sirdas.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Category
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.CategoryItem

class HealthViewModel : ViewModel() {

    private val _categories = mutableStateListOf<Category>()
    val categories: List<Category> get() = _categories

    fun addCategory(name: String) {
        val newCategory = Category(name = name)
        _categories.add(newCategory)
    }

    fun editCategory(categoryId: String, newName: String) {
        val categoryIndex = _categories.indexOfFirst { it.id == categoryId }
        if (categoryIndex != -1) {
            _categories[categoryIndex] = _categories[categoryIndex].copy(
                name = newName,
                lastEditedTime = System.currentTimeMillis()
            )
        }
    }

    fun deleteCategory(categoryId: String) {
        _categories.removeIf { it.id == categoryId }
    }

    fun addItemToCategory(categoryId: String, itemName: String, itemValue: String) {
        val categoryIndex = _categories.indexOfFirst { it.id == categoryId }
        if (categoryIndex != -1) {
            val updatedItems = _categories[categoryIndex].items + CategoryItem(
                name = itemName,
                value = itemValue
            )
            _categories[categoryIndex] = _categories[categoryIndex].copy(
                items = updatedItems,
                lastEditedTime = System.currentTimeMillis()
            )
        }
    }

    fun editItemInCategory(categoryId: String, itemId: String, newName: String, newValue: String) {
        val categoryIndex = _categories.indexOfFirst { it.id == categoryId }
        if (categoryIndex != -1) {
            val updatedItems = _categories[categoryIndex].items.map {
                if (it.id == itemId) {
                    it.copy(
                        name = newName,
                        value = newValue,
                        lastEditedTime = System.currentTimeMillis()
                    )
                } else {
                    it
                }
            }
            _categories[categoryIndex] = _categories[categoryIndex].copy(
                items = updatedItems,
                lastEditedTime = System.currentTimeMillis()
            )
        }
    }

    fun deleteItemFromCategory(categoryId: String, itemId: String) {
        val categoryIndex = _categories.indexOfFirst { it.id == categoryId }
        if (categoryIndex != -1) {
            val updatedItems = _categories[categoryIndex].items.filterNot { it.id == itemId }
            _categories[categoryIndex] = _categories[categoryIndex].copy(
                items = updatedItems,
                lastEditedTime = System.currentTimeMillis()
            )
        }
    }
}
