package com.example.recipe_searcher.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.recipe_searcher.HTTPCallImplementation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_searcher.AreaMeal
import kotlinx.coroutines.launch

class RecipeSearcherViewModel: ViewModel() {

    private val _requestState = mutableStateOf(APIRequestState())
    val requestState: State<APIRequestState> = _requestState


    fun getAreaMeals(){

        viewModelScope.launch {

            try {

                _requestState.value = _requestState.value.copy(
                    status = RequestStatus.Success,
                    requestContent = HTTPCallImplementation.getResponseObject().areaMeals
                )

            }
            catch(exception: Exception) {

                _requestState.value = _requestState.value.copy(
                    error = "an error occurred while getting data :(, please verify your internet connection and retry. if error persist, contact our support :)",
                    status = RequestStatus.Error,
                    requestContent = null)

            }

        }
    }

}

data class APIRequestState(var error: String? =  null,
                           var status: RequestStatus = RequestStatus.NotRequested,
                           var requestContent: List<AreaMeal>? = emptyList())



enum class RequestStatus{
    Loading,
    Error,
    Success,
    NotAnswered,
    NotRequested
}