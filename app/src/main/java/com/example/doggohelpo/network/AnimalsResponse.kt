package com.example.doggohelpo.network

import com.google.gson.annotations.SerializedName

data class AnimalsResponse(val animals: List<Animal>)
data class AnimalResponse(val animal: Animal)

data class Animal(
    val id: Int?,
    val age: String?,
    val name: String?,
    val gender: String?,
    @SerializedName("primary_photo_cropped")
    val photo: AnimalPhoto?,
    val contact: AnimalContact,
    val breeds: Breed?,
    val url: String?
)

data class AnimalPhoto(
    val small: String?
)

data class AnimalContact(val address: AnimalAddress)

data class AnimalAddress(
    val city: String?,
    val state: String?
)

data class Breed(val primary: String?)