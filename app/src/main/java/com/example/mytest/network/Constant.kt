package com.example.mytest.network

import okhttp3.HttpUrl

object Constant {

    const val SCHEME = "https"
    const val HOST = "translate.argosopentech.com"
    const val LANGUAGE = "languages"
    const val TRANSLATE = "translate"
    const val q: String = "q"
    const val sourceText: String = "source"
    const val targetText: String = "target"

    var urlTranslated = HttpUrl.Builder()
        .scheme(Constant.SCHEME)
        .host(Constant.HOST)
        .addPathSegment(Constant.TRANSLATE)


    var urlLanguage = HttpUrl.Builder()
        .scheme(Constant.SCHEME)
        .host(Constant.HOST)
        .addPathSegment(Constant.LANGUAGE)
        .build().toString()

}