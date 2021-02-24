package com.example.doggohelpo.features.dogdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.doggohelpo.LocalMainViewModel
import com.example.doggohelpo.features.ErrorState
import com.example.doggohelpo.features.FinishedState
import com.example.doggohelpo.features.LoadingState
import com.example.doggohelpo.features.alldogs.Dog
import com.example.doggohelpo.features.alldogs.DogDetailsScreenModel
import com.example.doggohelpo.features.alldogs.Picture
import com.example.doggohelpo.ui.common.ErrorModelState
import com.example.doggohelpo.ui.common.LoadingModelState

@Composable
fun DogDetailsScreen(dogId: String) {
    LocalMainViewModel.current.getDogDetailData(dogId)
    DogDetailContent()
}

@Composable
fun DogDetailContent() {
    val screenModel: DogDetailsScreenModel by LocalMainViewModel.current.dogDetailsScreenModel.observeAsState(
        DogDetailsScreenModel()
    )
    Column {
        TopAppBar(
            title = { Text("Find all the doggos") }
        )
        when (screenModel.state) {
            is ErrorState -> ErrorModelState(screenModel.state as ErrorState)
            LoadingState -> LoadingModelState()
            FinishedState -> DogDetails(screenModel.dog ?: throw IllegalStateException("Who let the dog out?"))
        }
    }
}

@Composable
fun DogDetails(dog: Dog) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse(dog.url))
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Picture(url = dog.imgUrl, modifier = Modifier.size(300.dp))
        Text(dog.name)
        Text(dog.gender)
        Text(dog.age)
        dog.breed?.let { Text(it) }
        Text(dog.location)
    }
}