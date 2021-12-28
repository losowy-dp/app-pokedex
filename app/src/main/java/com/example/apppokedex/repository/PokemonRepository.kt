package com.example.apppokedex.repository

import com.example.apppokedex.R
import com.example.apppokedex.data.remote.PokemonApi
import com.example.apppokedex.data.remote.responses.Pokemon
import com.example.apppokedex.data.remote.responses.PokemonList
import com.example.apppokedex.util.Resourses
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private  val api: PokemonApi
) {
    suspend fun  getPokemonList(limit: Int, offset: Int): Resourses<PokemonList>{
        val response = try{
            api.getPokemonList(limit, offset)
        }catch (e: Exception){
            return Resourses.Error(R.string.unknownError.toString())
        }
        return Resourses.Success(response)
    }

    suspend fun getPokemonInfo(name: String): Resourses<Pokemon>{
        val response = try {
            api.getPokemonInfo(name)
        }catch (e: Exception){
            return Resourses.Error(R.string.unknownError.toString())
        }
        return Resourses.Success(response)
    }
}