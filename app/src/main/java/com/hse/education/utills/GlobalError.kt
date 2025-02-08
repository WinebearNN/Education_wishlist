package com.hse.education.utills

data class GlobalError(val code: Int, val description: String)

class ErrorException(val errors: List<GlobalError>) : Exception("Errors occurred")
