package model

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("LastName")   val lastName  : String,
    @SerializedName("FirstName")  val firstName : String,
    @SerializedName("FatherName") val fatherName: String,
    @SerializedName("Login")      val login     : String,
    @SerializedName("Password")   val password  : String,
)
