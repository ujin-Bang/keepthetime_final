package com.promise.keepthetime_final.api

import com.promise.keepthetime_final.datas.BasicResponse
import okhttp3.MultipartBody
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
    fun getRequestMyInfo():Call<BasicResponse>

    @GET("/user/check")
    fun getRequestDupleCheck(
        @Query("type") type: String,
        @Query("value") value: String,
    ):Call<BasicResponse>

    @GET("/user/friend")
    fun getRequestMyFriendList(
        @Query("type") type: String,
    ): Call<BasicResponse>

    @GET("/search/user")
    fun getRequestSearchUserList(
        @Query("nickname") nickname: String,
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/friend")
    fun postRequestAddFriend(
        @Field("user_id") userid: Int,
    ): Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user/friend")
    fun putRequestAcceptOrDenyFriend(
        @Field("user_id") userId: Int,
        @Field("type") type: String,
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/appointment")
    fun postRequestAddAppointment(
        @Field("title") title: String,
        @Field("datetime") datetime: String,
        @Field("start_place") startPlaceName: String,
        @Field("start_latitude") startLat: Double,
        @Field("start_longitude") startLng: Double,
        @Field("place") place: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double,
        @Field("friend_list") friendList: String,
    ): Call<BasicResponse>

    @GET("/appointment")
    fun getRequestAppointmentList()
    :Call<BasicResponse>

    @GET("/user/place")
    fun getRequestStratPlacesList()
    :Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun postRequestAddMyPlace(
        @Field("name") name: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double,
        @Field("is_primary") isPrimary: Boolean,
    ): Call<BasicResponse>

//    ????????? ?????? ?????? => ??????????????? ????????? ?????? : Field ??????,Multipart ??????
    @Multipart
    @PUT("/user/image")
    fun putRequestProfileImg(
        @Part img: MultipartBody.Part
    ):Call<BasicResponse>

    //???????????? ??????
    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestEditPassword(
        @Field("current_password") currentPassword: String,
        @Field("new_password") new_password: String,
    ):Call<BasicResponse>

    @GET("/appointment/{appointment_id}")
    fun getRequestAppointmentDetail(
        @Path("appointment_id") appointmentId: Int,
    ): Call<BasicResponse>

    @DELETE("/appointment")
    fun deleteRequestAppointment(
        @Query("appointment_id") appointmentId: Int,
    ): Call<BasicResponse>
}