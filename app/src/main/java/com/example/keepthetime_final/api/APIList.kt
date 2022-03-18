package com.example.keepthetime_final.api

import com.example.keepthetime_final.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface APIList {

    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email: String,
        @Field("password")pw: String,
    ): Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignup(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nick_name") nickname: String,
    ) : Call<BasicResponse>

}