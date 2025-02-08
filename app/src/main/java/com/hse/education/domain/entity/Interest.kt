package com.hse.education.domain.entity

import com.hse.education.R

enum class Interest(val value: Int, val translation: String,val imageId:Int) {
    GAME(1, "Игры", R.drawable.outline_videogame_asset_24),
    VOLLEYBALL(2,"Волейбол",R.drawable.outline_sports_volleyball_24),
    FOOD(3,"Еда",R.drawable.outline_fastfood_24);



    companion object{
        fun fromValue(value:Int):Interest?{
            return entries.find { it.value==value }
        }
        fun fromTranslation(translation: String):Interest?{
            return entries.find { it.translation==translation }
        }
    }

}