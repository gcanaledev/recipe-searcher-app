package com.example.recipe_searcher.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.recipe_searcher.model.Area
import com.example.recipe_searcher.model.AreaMeal
import com.example.recipe_searcher.view_model.RecipeSearcherViewModel
import com.example.recipe_searcher.view_model.RequestStatus

class MainScreenView (private val recipeViewModel: RecipeSearcherViewModel) {

    private val modifier = Modifier

    private val primaryColor = Color(49, 65, 39, 255)
    private val secondaryColor = Color(186, 197, 158, 255)
    private val tertiaryColor = Color(237, 240, 228, 255)



    @Composable
    fun BuildMainRecipeScreen(){

        var chosenArea by remember { mutableStateOf(Area.None) }

        val context = LocalContext.current
        fun resetApplication(){
            (context as Activity).recreate()
        }

        Box{
            when (recipeViewModel.requestState.value.status) {

                RequestStatus.NotRequested -> {

                    Column (Modifier.background(tertiaryColor)){

                        val mainBoxPadding = 40.dp

                        Column (
                            modifier
                                .fillMaxSize()
                                .padding(mainBoxPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {


                            Column (
                                Modifier
                                    .background(secondaryColor, RoundedCornerShape(30.dp))
                                    .padding(0.dp, 40.dp)) {

                                Text(text = "please select a country to get it's famous receipts!",
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(mainBoxPadding, 0.dp))

                                Spacer(modifier.height(100.dp))

                                Row (
                                    modifier
                                        .fillMaxWidth()
                                        .padding(36.dp, 0.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween) {

                                    Box {
                                        var expandMenuItem by remember { mutableStateOf(false) }

                                        Button(onClick = { expandMenuItem = true },
                                            modifier.size(180.dp, 50.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)) {

                                            Text(text = chosenArea.toString())

                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = expandMenuItem,
                                            onDismissRequest = { expandMenuItem = false },
                                            modifier = Modifier.height(250.dp)) {
                                            Area.entries.forEach { a ->
                                                DropdownMenuItem(text = { Text(text = a.toString()) },
                                                    onClick = { chosenArea = a; expandMenuItem = false },
                                                    modifier = Modifier.height(36.dp))
                                            }
                                        }
                                    }

                                    Box{
                                        Button(onClick = { recipeViewModel.getAreaMeals(chosenArea) },
                                            modifier.size(50.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)) { }

                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            modifier.align(Alignment.Center),
                                            tint = secondaryColor
                                        )                            }
                                }

                            }
                        }
                    }
                }

                RequestStatus.Loading -> {
                    CircularProgressIndicator(modifier.align(Alignment.Center))
                }

                RequestStatus.Error ->{

                    Column (horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize().background(tertiaryColor)) {


                        Box{

                            Button(onClick = { resetApplication() },
                                modifier = Modifier.padding(50.dp),
                                colors = ButtonDefaults.buttonColors(primaryColor)) {  }

                            Icon(imageVector = Icons.Default.Refresh,
                                contentDescription = "refreshIcon",
                                modifier = Modifier.align(Alignment.Center),
                                tint = secondaryColor)
                        }

                    }

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

        val context = LocalContext.current
        fun resetApplication(){
            (context as Activity).recreate()
        }

        // at this moment, i do not learned navigation neither prepared the app to it :( so a found this option to user request request again :)

        Column (modifier = Modifier
            .fillMaxSize()
            .background(tertiaryColor)){

            Column {

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .background(secondaryColor),
                     horizontalArrangement = Arrangement.SpaceBetween){

                    val insideRowWidgetVerticalPadding = 8.dp
                    val insideRowWidgetHorizontalPadding = 32.dp

                    Text(text = "$areaName receipts!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(
                                insideRowWidgetHorizontalPadding,
                                insideRowWidgetVerticalPadding
                            ))

                    Box{

                        Button(onClick = { resetApplication() },
                            modifier = Modifier.padding(insideRowWidgetHorizontalPadding, insideRowWidgetVerticalPadding),
                            colors = ButtonDefaults.buttonColors(primaryColor)) {  }

                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "arrowBackIcon",
                            modifier = Modifier.align(Alignment.Center),
                            tint = secondaryColor)
                    }
                }


                Divider(color = Color.Black, modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth())
            }

            LazyVerticalGrid(columns = GridCells.Fixed(2)){
                items(meals){
                        m -> ShowMeal(meal = m)
                }
            }
        }
    }

    @Composable
    private fun ShowMeal(meal: AreaMeal){

        Column (
            Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(2.dp, primaryColor),
            horizontalAlignment = Alignment.CenterHorizontally){

            Column (
                Modifier
                    .padding(16.dp)
                    .height(225.dp)) {

                Image(painter = rememberAsyncImagePainter(meal.strMealThumb),
                    contentDescription = "mealImage",
                    modifier = Modifier
                        .size(125.dp)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(20.dp))

                Divider(thickness = 5.dp, color = primaryColor)

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = meal.strMeal,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}


@Preview
@Composable
fun ShowRecipeMainScreenPreview(){
    MainScreenView(RecipeSearcherViewModel()).BuildMainRecipeScreen()
}