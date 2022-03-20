package com.example.keepthetime_final.datas

class DataResponse (
    val user: UserData,
    val token: String,

    val friends: List<UserData>
        ){
}