package com.example.recipe_searcher.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.recipe_searcher.model.Area
import com.example.recipe_searcher.model.AreaMeal
import com.example.recipe_searcher.view_model.RecipeSearcherViewModel
import com.example.recipe_searcher.view_model.RequestStatus

class MainScreenView (private val recipeViewModel: RecipeSearcherViewModel) {

    private val modifier = Modifier

    @Composable
    fun BuildMainRecipeScreen(){

        var chosenArea by remember { mutableStateOf(Area.None) }

        Box{
            when (recipeViewModel.requestState.value.status) {

                RequestStatus.NotRequested -> {

                    Column (modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(text = "please select a country")

                        Spacer(modifier.height(100.dp))

                        Row (
                            modifier
                                .fillMaxWidth()
                                .padding(36.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween) {

                            Box {
                                var expandMenuItem by remember { mutableStateOf(false) }

                                Button(onClick = { expandMenuItem = true }, modifier.size(250.dp, 50.dp)) {

                                    Text(text = chosenArea.toString())

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandMenuItem,
                                    onDismissRequest = { expandMenuItem = false }) {
                                    Area.entries.forEach { a ->
                                        DropdownMenuItem(text = { Text(text = a.toString()) },
                                            onClick = { chosenArea = a; expandMenuItem = false })
                                    }
                                }
                            }

                            Box{
                                Button(onClick = { recipeViewModel.getAreaMeals(chosenArea) }, modifier.size(50.dp)) {

                                }
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier.align(Alignment.Center),
                                    tint = Color.White
                                )                            }
                        }
                    }

                }

                RequestStatus.Loading -> {
                    CircularProgressIndicator(modifier.align(Alignment.Center))
                }

                RequestStatus.Error ->{
                    Toast.makeText(LocalContext.current, recipeViewModel.requestState.value.error, Toast.LENGTH_LONG).show()
                }

                RequestStatus.Success ->{

                    if (recipeViewModel.requestState.value.requestContent == null){
                        Toast.makeText(LocalContext.current, "na data could be loaded, please try again :(", Toast.LENGTH_LONG).show()
                        return
                    }

                    ShowAreaMealSession(recipeViewModel.requestState.value.requestContent!!, recipeViewModel.areaName)
                }
            }
        }
    }

    @Composable
    private fun ShowAreaMealSession(meals: List<AreaMeal>, areaName: String){

        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween){

            Row (modifier = Modifier.padding(8.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){

                Text(text = "$areaName receipts!", modifier = Modifier.align(Alignment.CenterVertically).padding(35.dp, 0.dp))

                Box{

                    Button(onClick = { /* TODO, make this button go back to previous screen */} , modifier = Modifier.padding(8.dp)) {  }

                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "arrowBackIcon",
                        modifier = Modifier.align(Alignment.Center),
                        tint = Color.White)
                }
            }


            Divider(color = Color.Black, modifier = Modifier
                .height(1.dp)
                .fillMaxWidth())

            LazyVerticalGrid(columns = GridCells.Fixed(2)){
                items(meals){
                        m -> ShowMeal(meal = m)
                }
            }
        }
    }
}



@Composable
private fun ShowMeal(meal: AreaMeal){

    Column(
        Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally){

        Image(painter = rememberAsyncImagePainter(meal.strMealThumb),
            contentDescription = "mealImage",
            modifier = Modifier
                .size(125.dp)
                .aspectRatio(1f))

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = meal.strMeal, textAlign = TextAlign.Center)
    }

}

@Preview
@Composable
fun ShowRecipeMainScreenPreview(){
    MainScreenView(RecipeSearcherViewModel()).BuildMainRecipeScreen()
}