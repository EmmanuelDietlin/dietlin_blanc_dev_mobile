package com.dietlin_blanc.todo.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietlin_blanc.todo.data.Api
import com.dietlin_blanc.todo.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {
    private val webService = Api.userWebService

    //public val uriStateFlow = MutableStateFlow<Uri?>(null)
    public val userStateFlow = MutableStateFlow<User>(User("a", "a"))

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

    fun edit(nom : String?, email : String?) {
        var newName = nom ?: userStateFlow.value.name
        var newMail = email ?: userStateFlow.value.email
        var newUser = User(newName, newMail, userStateFlow.value.avatar)
        println(newUser)
        viewModelScope.launch {
            val response = webService.update(newUser)// Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
        }
    }



}