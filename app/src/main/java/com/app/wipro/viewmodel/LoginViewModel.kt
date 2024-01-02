package com.app.wipro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.wipro.model.LoginRequest
import com.app.wipro.model.LoginResponse
import com.app.wipro.network.WChangeService
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private var _loginResponseData = MutableLiveData<LoginResponse?>()
    val loginResponseData : LiveData<LoginResponse?> = _loginResponseData
    var _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable : LiveData<Boolean> = _isNetworkAvailable
    var _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage


    fun getLoginDetails(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                val user = JsonObject()
                user.addProperty("username", loginRequest.userName)
                user.addProperty("password", loginRequest.password)



                WChangeService.RetrofitInstance.wChangeAPI.postData(user)
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                            if (response.isSuccessful) {
                                // Successful response, update LiveData
                                _loginResponseData.value = response.body()
                            } else {
                                // Unsuccessful response, handle the error
                                _loginResponseData.value = null
                                handleErrorResponse(response.code())
                            }
                        }
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.d("TAG",t.message.toString())
                        }
                    })

            } catch (e: Exception) {
                // Handle exception (e.g., show error message)
                e.printStackTrace()
            }
        }
    }


    private fun handleErrorResponse(httpStatusCode: Int) {
        when (httpStatusCode) {
            400 -> _errorMessage.value = "Bad Request: Invalid credentials or malformed request."
            401 -> _errorMessage.value = "Unauthorized: Authentication failed."
            403 -> _errorMessage.value = "Forbidden: Access to the resource is forbidden."
            404 -> _errorMessage.value = "Not Found: The requested resource was not found."
            500 -> _errorMessage.value = "Internal Server Error: Something went wrong on the server."
            // Add more cases for other HTTP status codes as needed
            else -> _errorMessage.value = "Unexpected Error: An unexpected error occurred. Status code: $httpStatusCode"
        }
    }

}