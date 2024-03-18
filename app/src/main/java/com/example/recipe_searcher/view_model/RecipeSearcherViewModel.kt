package com.example.recipe_searcher.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_searcher.model.AreaMeal
import com.example.recipe_searcher.model.Area
import com.example.recipe_searcher.model.getHttpRequestImplementation
import kotlinx.coroutines.launch

class RecipeSearcherViewModel: ViewModel() {

    private val _requestState = mutableStateOf(APIRequestState())
    val requestState: State<APIRequestState> = _requestState


    fun getAreaMeals(areaRequested: Area){

        _requestState.value = _requestState.value.copy(
            status = RequestStatus.Loading,
        )

        if (areaRequested.toString().isEmpty()) {

            _requestState.value =  _requestState.value.copy(
                status = RequestStatus.Error,
                error = "it is necessary to choose a valid country!",
                requestContent = null
            )

            return
        }

        viewModelScope.launch {

            try {

                _requestState.value = _requestState.value.copy(
                    status = RequestStatus.Success,
                    requestContent = getHttpRequestImplementation().getResponseObject(areaRequested.name).meals
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
    NotRequested
}