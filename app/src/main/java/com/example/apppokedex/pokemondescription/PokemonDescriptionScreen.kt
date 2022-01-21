package com.example.apppokedex.pokemondescription


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.apppokedex.data.remote.responses.Pokemon
import com.example.apppokedex.data.remote.responses.Type
import com.example.apppokedex.util.Resourses
import com.example.apppokedex.util.parseTypeToColor
import java.util.*
import kotlin.math.round

@Composable
fun PokemonDescriptionScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImegeSize: Dp = 200.dp,
    viewModel: PokemonDescriptionViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resourses<Pokemon>>(initialValue = Resourses.Loading())
    {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value
    Column(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)
    ) {
        PokemonDescriptionTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.Start)
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            if(pokemonInfo is Resourses.Success)
            {

                PokemonDescriptionStateWrapper(
                    pokemonInfo = pokemonInfo,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            //top = topPadding,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    loadingModifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .padding(
                            top = topPadding + pokemonImegeSize / 2f,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                )
                pokemonInfo.data?.sprites?.let{
                    val painter = rememberImagePainter(
                        data = it.front_default,
                        builder = {
                            crossfade(true)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(5f)

                    )
                }
            }

        }


    }
}

@Composable
fun PokemonDescriptionTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
        )
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDescriptionStateWrapper(
    pokemonInfo: Resourses<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier:Modifier = Modifier
) {
    when(pokemonInfo)
    {
        is Resourses.Success -> {
            PokemonDescriptionSection(pokemonInfo = pokemonInfo.data!!, modifier = modifier)
        }
        is Resourses.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resourses.Loading ->{
            CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            modifier = loadingModifier
            )
        }
    }
}

@Composable
fun PokemonDescriptionSection(
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    )
    {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDescriptionDataSection(pokemonWeight = pokemonInfo.weight, pokemonHeight = pokemonInfo.height)
    }
}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ){
        for(type in types)
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ){
                Text(
                    text = type.type.name.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDescriptionDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    selectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember{
        round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInMeters = remember{
        round(pokemonHeight * 100f) / 1000f
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    )
    {
        PokemonDescriptionDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit ="kg",
            dataIcon = Icons.Outlined.FitnessCenter,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, selectionHeight)
            .background(Color.LightGray))
        PokemonDescriptionDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit ="m",
            dataIcon = Icons.Outlined.Height,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun PokemonDescriptionDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: ImageVector,
    modifier: Modifier = Modifier
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ){
        Icon(dataIcon, contentDescription = null, tint = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onSurface
        )
    }
}