package com.dietlin_blanc.todo.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TwitchWebService {
    @GET("/helix/videos?user_id={id}")
    suspend fun fetchVideos(@Path("id") id: String) : Response<VideoList>

}