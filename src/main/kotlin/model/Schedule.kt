package model

import java.util.*

data class Schedule(
    val id          : UUID,
    var date        : String,
    var timeStart   : String,
    var timeEnd     : String,
    var roomNum     : Int,
    var type        : String,
    var teacherNotes: String,
    var subjectID   : UUID,
    var teacherID   : UUID
)