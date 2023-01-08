package com.dietlin_blanc.todo.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietlin_blanc.todo.data.Api
import com.dietlin_blanc.todo.data.User
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class UserViewModel : ViewModel() {
    private val webService = Api.userWebService

    //public val uriStateFlow = MutableStateFlow<Uri?>(null)
    public val userStateFlow = MutableStateFlow<User>(User("", ""))

    fun refresh() {
        viewModelScope.launch {
            val response = webService.fetchUser() // Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedUser = response.body()!!
            userStateFlow.value = fetchedUser // on modifie le flow, ce qui déclenche ses observers
        }
    }

    fun updateAvatar(avatar : MultipartBody.Part) {
        viewModelScope.launch {
            val response = webService.updateAvatar(avatar)// Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedUser = response.body()!!
            userStateFlow.value = fetchedUser
        }
    }

    fun edit(user: User) {
        viewModelScope.launch {

            var args = JsonObject()
            var body = JsonObject()
            args.add("email", JsonPrimitive(user.email))
            args.add("full_name", JsonPrimitive(user.name))
            body.add("type", JsonPrimitive("user_update"))
            body.add("uuid", JsonPrimitive(UUID.randomUUID().toString()))
            body.add("args", args)

            var requestBody = "commands='[$body]'".toRequestBody()

            val response = webService.update(requestBody)// Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
        }
    }



}