package com.example.recipe_searcher.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipe_searcher.model.Area
import com.example.recipe_searcher.view_model.RecipeSearcherViewModel
import com.example.recipe_searcher.view_model.RequestStatus

class MainScreenView (private val recipeViewModel: RecipeSearcherViewModel) {

    private val modifier = Modifier

    private val requestState = recipeViewModel.requestState
    private val requestStatus = requestState.value.status

    @Composable
    fun BuildMainRecipeScreen(){

        var chosenArea by remember { mutableStateOf(Area.None) }

        Box{
            when (requestStatus) {

                RequestStatus.NotRequested -> {

                    Column (modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(text = "please select a country")

                        Spacer(modifier.height(100.dp))

                        Row (modifier.fillMaxWidth().padding(36.dp, 0.dp),
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
                    Toast.makeText(LocalContext.current, requestState.value.error, Toast.LENGTH_LONG).show()
                }

                RequestStatus.Success ->{

                }
            }

        }

    }
}

@Preview
@Composable
fun ShowRecipeMainScreenPreview(){
    MainScreenView(RecipeSearcherViewModel()).BuildMainRecipeScreen()
}