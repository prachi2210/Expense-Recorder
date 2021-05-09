package com.prachi.expenserecorder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TransactionsDao {


    @Insert
    suspend fun insertTransaction(transactionsData: TransactionData)

    @Update
    suspend fun updateTransaction(transactionsData: TransactionData)

    @Delete
    suspend fun deleteTransaction(transactionsData: TransactionData)


    @Query("SELECT * FROM TRANSACTION_DATA ORDER BY date ASC")
    fun getAllData(): LiveData<List<TransactionData>>


    @Query("SELECT * FROM transaction_data WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC ")
    fun getCurrentMonthData(startDate: Long, endDate: Long): LiveData<List<TransactionData>>


//    @Query("SELECT * FROM TRANSACTION_DATA WHERE  strftime('%Y',date) == strftime('%Y',date('now')) AND  strftime('%m',date) == strftime('%m',date('now')) ")
//
//    fun getCurrentMonthData(): LiveData<List<TransactionData>>


}