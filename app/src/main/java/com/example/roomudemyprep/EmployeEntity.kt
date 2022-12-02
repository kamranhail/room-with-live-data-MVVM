package com.example.roomudemyprep

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "emplyee-table")
data class EmployeEntity (
                           @PrimaryKey(autoGenerate = true)
                           val id: Int=0,
                           val name: String="",
                           @ColumnInfo(name = "email-id")
                           val email: String="")
