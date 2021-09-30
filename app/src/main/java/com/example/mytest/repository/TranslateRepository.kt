


import com.example.mytest.Status
import com.example.mytest.network.Constant
import com.example.mytest.response.LanguagesText

import com.example.mytest.response.TranslatedText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


object TranslateRepository {


    fun getInfoTranslate(q:String, sourceText: String,targetText:String)=
        flow<Status<TranslatedText>>{
            emit(Status.Loading)
            emit(Client.initRequest(Constant.TRANSLATE, q , sourceText,targetText))
        }.flowOn(Dispatchers.IO)



    fun getInfoLanguages() = flow<Status<LanguagesText>>{
            emit(Status.Loading)
            emit(Client.initRequest(Constant.LANGUAGE,null,null,null))
        }.flowOn(Dispatchers.IO)

}




