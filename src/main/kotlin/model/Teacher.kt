package model

import java.util.*

data class Teacher(
    val id        : UUID,
    val name      : String,
    val surname   : String,
    val patronymic: String,
    val login     : String,
    val password  : String,
)