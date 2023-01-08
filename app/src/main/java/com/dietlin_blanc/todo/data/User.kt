package com.dietlin_blanc.todo.data

import kotlinx.serialization.SerialName
import java.util.*

@kotlinx.serialization.Serializable
data class User(@SerialName("email")
                val email: String,
                @SerialName("full_name")
                val name: String,
                @SerialName("avatar_medium")
                val avatar: String? = null)

@kotlinx.serialization.Serializable
data class UserUpdate(@SerialName("args")
                      val args : HashMap<String, String>,
                      @SerialName("uuid")
                      val uuid : String = UUID.randomUUID().toString(),
                      @SerialName("type")
                      val type : String = "user_update"
)