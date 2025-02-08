package com.hse.education.utills

enum class ErrorCode(val description: String){
    CODE_100("Invalid email format"),
    CODE_101("Password should be at least 6 characters long"),
    CODE_102("Phone number should be at least 10 numbers long"),
    CODE_103("Incorrect password or email")
}