package com.example.apppokedex.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.apppokedex.models.PokedexListEntry
import com.example.apppokedex.repository.PokemonRepository
import com.example.apppokedex.util.Constants.PAGE_SIZE
import com.example.apppokedex.util.Resourses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init
    {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated(){
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE,curPage * PAGE_SIZE)
            when(result)
            {
                is Resourses.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEnteries = result.data.results.mapIndexed { index, entry ->
                        val  number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        val resultForSinglePokemon = repository.getPokemonInfo(entry.name)
                        var typeColor = "normal"
                        when(resultForSinglePokemon){
                            is Resourses.Success -> {
                                if(resultForSinglePokemon.data?.types?.get(0)?.type?.name.toString()=="unknown" || resultForSinglePokemon.data?.types?.get(0)?.type?.name.toString()=="shadow")
                                    typeColor = "normal"
                                else
                                    typeColor = resultForSinglePokemon.data?.types?.get(0)?.type?.name.toString()
                            }
                        }
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt(),typeColor )
                    }
                    curPage++

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEnteries
                }
                is Resourses.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}