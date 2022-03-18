package com.example.keepthetime_final.api

import com.example.keepthetime_final.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIList {

    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email: String,
        @Field("password")pw: String,
    ): Call<BasicResponse>

}