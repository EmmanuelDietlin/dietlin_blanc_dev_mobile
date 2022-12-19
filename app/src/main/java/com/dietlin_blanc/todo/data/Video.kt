package com.dietlin_blanc.todo.data

import kotlinx.serialization.SerialName

data class Video(@SerialName("id")
                 val id: String,
                 @SerialName("url")
                 val url: String,
                 @SerialName("thumbnail_url")
                 val thumbnail: String)
