package com.example.recipe_searcher

data class AreaMeal(val strMeal: String, val strMealThumb: String, val idMeal: String)

data class AreaMeals(val areaMeals: List<AreaMeal>)
