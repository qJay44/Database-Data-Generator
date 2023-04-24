package model

data class Notification(
    val id          : String,
    var date        : String,
    var title       : String,
    var text        : String,
    var studentGroup: String
)