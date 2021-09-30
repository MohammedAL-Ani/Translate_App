import com.example.mytest.Status
import com.example.mytest.network.Constant
import com.example.mytest.response.LanguagesText

import com.example.mytest.response.TranslatedText
import com.google.gson.Gson
import okhttp3.*


object Client {

    //make first request

    val okHttpClient = OkHttpClient()

    val gson = Gson()


    fun generalUrl(pathSegment: String, q: String?, sourceText: String?, targetText: String?) =
        HttpUrl.Builder().scheme(Constant.SCHEME).host(Constant.HOST).addPathSegment(pathSegment)
            .apply {
                if (pathSegment == Constant.TRANSLATE) {
                    addQueryParameter(Constant.q, q)
                    addQueryParameter(Constant.sourceText, sourceText)
                    addQueryParameter(Constant.targetText, targetText)
                }
            }.toString()


    inline fun <reified T> initRequest(urlRequest: String,  q: String?,  sourceText: String?,  targetText: String?): Status<T> {

        val request =
            Request.Builder().url(generalUrl(urlRequest, q, sourceText, targetText)).apply {
                if (urlRequest == Constant.TRANSLATE) {
                    post(FormBody.Builder().build())
                }
            }.build()
        val response = okHttpClient.newCall(request).execute()

        return if (response.isSuccessful) {
            val parserResponse = gson.fromJson(
                response.body?.string(),
                T::class.java
            )
            Status.Success(parserResponse)
        } else {
            Status.Fail(response.message)
        }
    }


}


























    
