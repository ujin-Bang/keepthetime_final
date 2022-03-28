package com.example.keepthetime_final.datas

class DataResponse (
    val user: UserData,
    val token: String,

    val friends: List<UserData>,

    val users: List<UserData>,

    val appointments: List<AppointmentData>, //약속목록 데이타

    val places : List<PlacesData>


        ){
}