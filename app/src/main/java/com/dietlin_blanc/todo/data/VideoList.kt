package com.dietlin_blanc.todo.data

import kotlinx.serialization.SerialName

data class VideoList(@SerialName("data")
                     val data: List<Video>)
