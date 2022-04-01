package com.promise.keepthetime_final.api

import android.content.Context
import com.promise.keepthetime_final.utils.ContextUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ServerAPI {

    companion object {

        private  var retrofit : Retrofit? = null
        private  val BASE_URL = "https://keepthetime.xyz"

        fun getRetrofit(context: Context) : Retrofit {
            if(retrofit == null) {

                val interceptor = Interceptor {
                    with(it) {


                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getLoginUerToken(context))
                            .build()

                        proceed(newRequest)
                    }

                }

                val myClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

//                Date 자료형으로 파싱 => String을 yyyy-MM-dd HH:mm:ss으로 파싱해서 저장해야 함.

                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss") //서버가 이런 양식으로 보내주는 String을
                    .registerTypeAdapter(
                        Date::class.java,
                        DateDeserializer()
                    ) //어떤 형태의 자료형을 적용시킬지?
                    .create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) //gson라이브러리와 결합 + Date 파싱 요령 첨부
                    .client(myClient)
                    .build()
            }
            return retrofit!!
        }

    }
}