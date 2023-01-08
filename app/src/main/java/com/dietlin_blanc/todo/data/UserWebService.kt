package com.dietlin_blanc.todo.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface UserWebService {
    @GET("/sync/v9/user/")
    suspend fun fetchUser(): Response<User>

    @Multipart
    @POST("sync/v9/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<User>

    @POST("sync/v9/sync")
    suspend fun update(@Body body : RequestBody): Response<Unit>
    //suspend fun update(@): Response<Unit>


}