package model

data class ProfileAttendance(
    val id: String,
    val scheduleID: String,
    val userID: String,
    val visited: Boolean
)