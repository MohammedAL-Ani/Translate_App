package com.example.mytest.response


import com.google.gson.annotations.SerializedName

data class TranslatedText(
    @SerializedName("translatedText")
    val translatedText: String?
)