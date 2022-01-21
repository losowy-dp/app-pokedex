package com.example.apppokedex.util

import androidx.compose.ui.graphics.Color
import com.example.apppokedex.ui.theme.*

sealed class ColorsForGradient(val colorType: String,val color: Color) {
    object ColorNormal: ColorsForGradient("normal", TypeNormal)
    object ColorFighting: ColorsForGradient("fighting", TypeFighting)
    object ColorFlying: ColorsForGradient("flying", TypeFlying)
    object ColorPoison: ColorsForGradient("poison", TypePoison)
    object ColorGround: ColorsForGradient("ground", TypeGround)
    object ColorRock: ColorsForGradient("rock", TypeRock)
    object ColorBug: ColorsForGradient("bug", TypeBug)
    object ColorGhost: ColorsForGradient("ghost", TypeGhost)
    object ColorSteel: ColorsForGradient("steel", TypeSteel)
    object ColorFire: ColorsForGradient("fire", TypeFire)
    object ColorWater: ColorsForGradient("water", TypeWater)
    object ColorGrass: ColorsForGradient("grass", TypeGrass)
    object ColorElectric: ColorsForGradient("electric", TypeElectric)
    object ColorPsychic: ColorsForGradient("psychic", TypePsychic)
    object ColorIce: ColorsForGradient("ice", TypeIce)
    object ColorDragon: ColorsForGradient("dragon", TypeDragon)
    object ColorDark: ColorsForGradient("dark", TypeDark)
    object ColorFairy: ColorsForGradient("fairy", TypeFairy)
}