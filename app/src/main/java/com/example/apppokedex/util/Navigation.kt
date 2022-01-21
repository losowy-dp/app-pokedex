package com.example.apppokedex.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apppokedex.data.remote.responses.Pokemon
import com.example.apppokedex.pokemonList.PokemonListScreen
import com.example.apppokedex.pokemondescription.PokemonDescriptionScreen
import java.util.*

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.PokedexListScreen.route
    ){
        composable(Screens.PokedexListScreen.route){
            PokemonListScreen(navController = navController)
        }

        composable(
            Screens.PokedexDetailScreen.route,
            arguments = listOf(
                navArgument("dominantColor"){
                    type = NavType.IntType
                },
                navArgument("pokemonName"){
                    type = NavType.StringType
                }
            )){
            val dominantColor = remember {
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?: Color.White
            }
            val pokemonName = remember {
                it.arguments?.getString("pokemonName")
            }
            if (pokemonName != null) {
                PokemonDescriptionScreen(dominantColor = dominantColor,
                    pokemonName = pokemonName.toLowerCase(Locale.ROOT) ?: "",
                    navController = navController
                )
            }
        }
    }
}