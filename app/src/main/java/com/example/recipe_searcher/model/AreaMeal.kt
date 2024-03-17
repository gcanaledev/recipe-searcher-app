package com.example.recipe_searcher.model

data class AreaMeal(val strMeal: String, val strMealThumb: String, val idMeal: String)

data class AreaMeals(val meals: List<AreaMeal>)
