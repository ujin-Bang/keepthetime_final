package com.example.keepthetime_final.api

import com.example.keepthetime_final.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/user")
    fun getRequestMyInfo(
        @Header("X-Http-Token") header: String,
    ):Call<BasicResponse>

    @GET("/user/check")
    fun getRequestDupleCheck(
        @Query("type") type: String,
        @Query("value") value: String,
    ):Call<BasicResponse>

    @GET("/user/friend")
    fun getRequestMyFriendList(
        @Header("X-Http-Token") token: String,
        @Query("type") type: String,
    ): Call<BasicResponse>

    @GET("/user/user")
    fun getRequestSearchUserList(
        @Header("X-Http-Token") token: String,
        @Query("nickname") type: String,
    ): Call<BasicResponse>
}