package model

import java.util.*

data class Student(
    val id        : UUID,
    var name      : String,
    var surname   : String,
    var patronymic: String,
    val login     : String,
    var password  : String,
    var groupName : String,
    var course    : String,
    var semester  : String
)