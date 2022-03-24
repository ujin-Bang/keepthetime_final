package com.example.keepthetime_final.datas

import java.io.Serializable

class UserData(
    val id: Int,
    val email: String,
    val nick_name: String,
    val created_at: String,
    val updated_at: String,

    val provider: String,

    val profile_img: String,
):Serializable {
}