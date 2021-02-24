package com.example.doggohelpo.features.alldogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.navigate
import coil.imageLoader
import coil.request.ImageRequest
import com.example.doggohelpo.LocalMainViewModel
import com.example.doggohelpo.LocalNavController
import com.example.doggohelpo.features.ErrorState
import com.example.doggohelpo.features.FinishedState
import com.example.doggohelpo.features.LoadingState
import com.example.doggohelpo.ui.common.ErrorModelState
import com.example.doggohelpo.ui.common.LoadingModelState
import com.example.doggohelpo.ui.common.composableFetch

@Composable
fun AllDogsScreen() {
    LocalMainViewModel.current.getAllDogsData()
    AllDogsContent()
}

@Composable
fun AllDogsContent() {
    val screenModel: AllDogsScreenModel by LocalMainViewModel.current.allDogsScreenModel.observeAsState(
        AllDogsScreenModel()
    )
    Column {
        TopAppBar(
            title = { Text("Find all the doggos") }
        )
        when (screenModel.state) {
            is ErrorState -> ErrorModelState(screenModel.state as ErrorState)
            LoadingState -> LoadingModelState()
            FinishedState -> DogsList(screenModel.dogs)
        }
    }
}

@Composable
fun DogsList(dogs: List<Dog>) {
    LazyColumn {
        items(dogs) { dog ->
            DogView(dog)
        }
    }
}

@Composable
fun DogView(dog: Dog) {
    val navController = LocalNavController.current
    Card(
        Modifier.padding(4.dp),
        elevation = 4.dp
    ) {
        Row(Modifier
            .clickable { navController.navigate("dogdetails/${dog.id}") }
            .fillMaxWidth()
        ) {
            Picture(dog.imgUrl, Modifier.size(100.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(dog.name, style = TextStyle(fontWeight = FontWeight.Bold))
                dog.breed?.let { Text(it) }
            }
        }
    }
}

@Composable
fun ReusablePicture(url: String, preLoaded: @Composable () -> Unit, content: @Composable (image: ImageBitmap) -> Unit) {
    val context = LocalContext.current
    composableFetch {
        val req = ImageRequest.Builder(context).data(url).build()
        context.imageLoader.execute(req).drawable
    }?.let { drawable ->
        content(drawable.toBitmap().asImageBitmap())
    } ?: run {
        preLoaded()
    }
}

@Composable
fun Picture(url: String, modifier: Modifier) {
    ReusablePicture(
        url = url,
        preLoaded = {
            Box(
                modifier = modifier
                    .background(Color.Gray)
            )
        },
        content = { image ->
            Image(
                modifier = modifier,
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    )
}