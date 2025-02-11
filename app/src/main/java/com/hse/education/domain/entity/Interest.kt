package com.hse.education.domain.entity

import com.hse.education.R

enum class Interest(val value: Int, val translation: String,val imageId:Int) {
    GAME(1, "Игры", R.drawable.outline_videogame_asset_24),
    VOLLEYBALL(2,"Волейбол",R.drawable.outline_sports_volleyball_24),
    FOOD(3,"Еда",R.drawable.outline_fastfood_24),
    MOVIES(4, "Фильмы", R.drawable.outline_movie_24),
    MUSIC(5, "Музыка", R.drawable.outline_music_note_24),
    READING(6, "Чтение", R.drawable.outline_menu_book_24),
    TRAVEL(7, "Путешествия", R.drawable.outline_flight_24),
    FITNESS(8, "Фитнес", R.drawable.outline_fitness_center_24),
    SWIMMING(9, "Плавание", R.drawable.outline_pool_24),
    PHOTOGRAPHY(10, "Фотография", R.drawable.outline_camera_alt_24),
    PAINTING(11, "Рисование", R.drawable.outline_palette_24),
    COOKING(12, "Готовка", R.drawable.outline_restaurant_24),
    CARS(13, "Автомобили", R.drawable.outline_directions_car_24),
    CYCLING(14, "Велоспорт", R.drawable.outline_directions_bike_24),
    RUNNING(15, "Бег", R.drawable.outline_directions_run_24),
    BOARD_GAMES(16, "Настольные игры", R.drawable.outline_casino_24),
    CHESS(17, "Шахматы", R.drawable.outline_smart_toy_24),
    YOGA(18, "Йога", R.drawable.outline_self_improvement_24),
    GARDENING(19, "Садоводство", R.drawable.outline_nature_24),
    ANIME(20, "Аниме", R.drawable.outline_movie_filter_24),
    ASTRONOMY(21, "Астрономия", R.drawable.outline_nightlight_24),
    TECHNOLOGY(22, "Технологии", R.drawable.outline_computer_24),
    PROGRAMMING(23, "Программирование", R.drawable.outline_code_24);




    companion object{
        fun fromValue(value:Int):Interest?{
            return entries.find { it.value==value }
        }
        fun fromTranslation(translation: String):Interest?{
            return entries.find { it.translation==translation }
        }
    }

}