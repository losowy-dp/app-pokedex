package com.example.apppokedex.pokemonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusOrder
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apppokedex.R

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
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                )
                SearchBar(
                    hint = "Search",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){

                }
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
                    //todo we have a problem( (part 3)
                    //isHintDisplay = it != FocusState.Active
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