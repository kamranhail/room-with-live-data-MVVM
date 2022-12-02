package com.example.roomudemyprep

import android.app.Application

class EmployeeApp: Application() {

    val db by lazy {
        EmployeeDatabase.getInstence(this)
    }
}