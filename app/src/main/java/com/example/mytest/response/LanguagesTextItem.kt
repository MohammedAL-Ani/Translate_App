package com.example.mytest.response


import com.google.gson.annotations.SerializedName

data class LanguagesTextItem(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?
)