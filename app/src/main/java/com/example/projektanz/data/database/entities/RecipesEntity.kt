package com.example.projektanz.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projektanz.models.FoodRecipe
import com.example.projektanz.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}