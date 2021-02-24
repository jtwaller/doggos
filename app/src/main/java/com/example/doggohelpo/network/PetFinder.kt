package com.example.doggohelpo.network

import retrofit2.http.*

interface PetFinder {

    @FormUrlEncoded
    @POST("v2/oauth2/token")
    suspend fun getAuthInfo(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): AuthInfo

    @GET("/v2/animals")
    suspend fun getAnimals(
        @Header("Authorization") token: String,
        @Query("type") type: String,
        @Query("limit") limit: Int,
        @Query("status") status: String = "adoptable"
    ): AnimalsResponse

    @GET("/v2/animals/{id}")
    suspend fun getAnimal(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AnimalResponse
}