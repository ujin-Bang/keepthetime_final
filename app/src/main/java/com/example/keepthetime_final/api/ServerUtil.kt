package com.example.keepthetime_final.api

import retrofit2.Retrofit


class ServerUtil {

    companion object {

        private  var retrofit : Retrofit? = null

        fun getRetrofit() : Retrofit {
            if(retrofit == null) {
                retrofit = Retrofit.Builder()
                    .build()
            }
            return retrofit!!
        }

    }
}