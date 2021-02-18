package com.x.coroutineskot

import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ContentApiService {
    @Multipart
    @POST("getMyLikes")
    fun getUserLikesVideos(
        @Part("uid") uid: RequestBody,
        @Part("offset") offset: RequestBody
    ): Flow<Boolean>

    @GET("todos")
     suspend fun getToDO(): List<TodoEntity>

}