package com.example.apppokedex.pokemondescription

import androidx.lifecycle.ViewModel
import com.example.apppokedex.data.remote.responses.Pokemon
import com.example.apppokedex.repository.PokemonRepository
import com.example.apppokedex.util.Resourses
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDescriptionViewModel  @Inject constructor (
    private val repository: PokemonRepository
    ) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resourses<Pokemon>
    {
        return repository.getPokemonInfo(pokemonName)
    }
}