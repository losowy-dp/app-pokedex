package com.example.apppokedex.util

sealed class Screens(val route: String){
    object PokedexListScreen: Screens("pokedex_list_screen")
    object PokedexDetailScreen: Screens("pokedex_detail_screen/{dominantColor}/{pokemonName}")
}
