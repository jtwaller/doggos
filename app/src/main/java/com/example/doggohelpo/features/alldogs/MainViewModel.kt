package com.example.doggohelpo.features.alldogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggohelpo.features.FinishedState
import com.example.doggohelpo.features.LoadingState
import com.example.doggohelpo.features.ModelState
import com.example.doggohelpo.network.Animal
import com.example.doggohelpo.network.PetFinder
import com.example.doggohelpo.network.PetfinderService
import com.example.doggohelpo.network.SecretInfo
import kotlinx.coroutines.launch

data class AllDogsScreenModel(val state: ModelState = LoadingState, val dogs: List<Dog> = emptyList())
data class DogDetailsScreenModel(val state: ModelState = LoadingState, val dog: Dog? = null)

// TODO: move to petfinder repository?
data class Dog(
    val id: String,
    val name: String = "No Name",
    val imgUrl: String,
    val gender: String,
    val age: String,
    val location: String,
    val breed: String? = null,
    val url: String
) {
    companion object {
        fun fromAnimal(animal: Animal): Dog =
            Dog(
                "${animal.id}",
                animal.name!!,
                animal.photo!!.small!!,
                animal.gender ?: "Undeclared Gender",
                animal.age ?: "Young at heart",
                "${animal.contact.address.city ?: "Unknown"}, ${animal.contact.address.state ?: "USA"}",
                animal.breeds?.primary,
                animal.url ?: "https://www.petfinder.com"
            )
    }
}

class MainViewModel : ViewModel() {
    private val _allDogsScreenModel = MutableLiveData<AllDogsScreenModel>()
    private val _dogDetailsScreenModel = MutableLiveData<DogDetailsScreenModel>()
    val allDogsScreenModel: LiveData<AllDogsScreenModel> = _allDogsScreenModel
    val dogDetailsScreenModel: LiveData<DogDetailsScreenModel> = _dogDetailsScreenModel

    fun getAllDogsData() {
        viewModelScope.launch {
            val token = getAccessToken()
            val dogs = getAllDogs(token)
            val model = AllDogsScreenModel(
                state = FinishedState,
                dogs = dogs
            )
            _allDogsScreenModel.postValue(model)
        }
    }

    fun getDogDetailData(id: String) {
        _dogDetailsScreenModel.value = DogDetailsScreenModel()
        viewModelScope.launch {
            val token = getAccessToken()
            val dog = getDog(token, id)
            val model = DogDetailsScreenModel(
                state = FinishedState,
                dog = dog
            )
            _dogDetailsScreenModel.postValue(model)
        }
    }

    // TODO: move to auth repository
    private suspend fun getAccessToken(): String {
        val info = PetfinderService.buildService(PetFinder::class.java).getAuthInfo(
            grantType = "client_credentials",
            clientId = SecretInfo.CLIENT_ID,
            clientSecret = SecretInfo.CLIENT_SECRET
        )
        return info.access_token
    }

    // TODO: move to petfinder repository
    private suspend fun getAllDogs(token: String): List<Dog> {
        val response = PetfinderService.buildService(PetFinder::class.java).getAnimals(" Bearer $token", "dog", 100)
        return response.animals.filter { it.isDisplayable() }
            .map { Dog.fromAnimal(it) }
    }

    private suspend fun getDog(token: String, id: String): Dog {
        val response = PetfinderService.buildService(PetFinder::class.java).getAnimal(" Bearer $token", id)
        return Dog.fromAnimal(response.animal)
    }

    private fun Animal.isDisplayable(): Boolean = (name != null) && (photo?.small != null) && (id != null)
}