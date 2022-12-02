package com.example.roomudemyprep

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface EmplyeeDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(empEntity:EmployeEntity)

    @Update
    suspend fun update(empEntity:EmployeEntity)

    @Delete
    suspend fun delete(empEntity:EmployeEntity)

    @Query("SELECT * FROM `emplyee-table`")
    fun fetchAllEmplye():Flow<List<EmployeEntity>>

    @Query("SELECT * FROM `emplyee-table` where id=:id")
    fun fetchEmplyeByID(id:Int):Flow<EmployeEntity>
}