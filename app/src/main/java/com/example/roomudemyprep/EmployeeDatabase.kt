package com.example.roomudemyprep

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [EmployeEntity::class], version = 1)
abstract class EmployeeDatabase :RoomDatabase() {



    abstract fun employeeDao():EmplyeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getInstence(context: Context): EmployeeDatabase {


            synchronized(this) {
                var instence = INSTANCE
                if (instence == null) {
                    instence = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDatabase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instence
                }
                return instence
            }
        }
    }
}




