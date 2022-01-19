package com.example.apppokedex.pokemonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.Coil
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.apppokedex.R
import com.example.apppokedex.data.remote.responses.Pokemon
import com.example.apppokedex.models.PokedexListEntry
import com.example.apppokedex.ui.theme.*
import com.example.apppokedex.util.ColorsForGradient

@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(painter = painterResource(id = R.mipmap.ic_internation_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally) //todo size
                )
                SearchBar(
                    hint = "Search",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    //
                }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
)
{
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplay by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {text = it; onSearch(it)},
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(20.dp, 12.dp)
                .onFocusChanged {
                    isHintDisplay = !it.isFocused
                }
        )
        if(isHintDisplay){
            Text(
               text = hint,
               color =  Color.LightGray,
                modifier = Modifier
                    .padding(20.dp,12.dp)
            )
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp))
    {
        val itemCount = if(pokemonList.size % 2 == 0){
            pokemonList.size / 2
        }else
        {
            pokemonList.size / 2 + 1
        }
        items(itemCount)
        {
            if(it >= itemCount -1 && !endReached)
            {
                viewModel.loadPokemonPaginated()
            }
            PokeRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
}

@Composable
fun PokedexEntery(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel(),
    pokemonDwa: Boolean = false
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    val painter = rememberImagePainter(
        data = entry.imageUrl,
        builder = {
            crossfade(true)
        }
    )
    Box(
      contentAlignment = Alignment.Center,
      modifier = if(pokemonDwa == true){Modifier
          .shadow(5.dp, RoundedCornerShape(10.dp))
          .clip(RoundedCornerShape(10.dp))
          .fillMaxWidth(1f)
          .background(
              Brush.verticalGradient(
                  listOf(
                      addColorDominant(entry.colorType),
                      defaultDominantColor
                  )
              )
          )
          .clickable {
              navController.navigate(
                  "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
              )
          }}else{Modifier
          .shadow(5.dp, RoundedCornerShape(10.dp))
          .clip(RoundedCornerShape(10.dp))
          .fillMaxWidth(0.5f)
          .background(
              Brush.verticalGradient(
                  listOf(
                      addColorDominant(entry.colorType),
                      defaultDominantColor
                  )
              )
          )
          .clickable {
              navController.navigate(
                  "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
              )
          }}

    ){
        Column {
            Image(
                painter = painter,
                contentDescription = entry.pokemonName,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),


            )
            when(painter.state){
                is ImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                }
                is ImagePainter.State.Success -> {
                    Text(
                        text = entry.pokemonName,
                        fontFamily = Roboto,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

fun addColorDominant(colorType: String): Color {
    when(colorType){
        "normal"-> return TypeNormal
        "fighting" -> return TypeFighting
        "flying" -> return TypeFlying
        "poison" -> return TypePoison
        "ground" -> return TypeGround
        "rock" -> return TypeRock
        "bug" -> return TypeBug
        "ghost" -> return TypeGhost
        "steel" -> return TypeSteel
        "fire" -> return TypeFire
        "water" -> return TypeWater
        "grass" -> return TypeGrass
        "electric" -> return TypeElectric
        "ice" -> return TypeIce
        "dragon" -> return TypeDragon
        "dark" -> return TypeDark
        "fairy" -> return TypeFairy
    }
    return TypeNormal
}

@Composable
fun PokeRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
) {
    Column {
        Row  {
            PokedexEntery(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2){
                PokedexEntery(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                    pokemonDwa = true
                )

            } else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}