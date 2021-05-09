package com.prachi.expenserecorder.data

import androidx.lifecycle.LiveData
import java.util.*

class Repository(private val transactionsDao: TransactionsDao) {

    val readAllData: LiveData<List<TransactionData>> = transactionsDao.getAllData()

   // val getStringData: LiveData<List<String>> = transactionsDao.getStringValue()


     fun getAllTransactionByMonth(
        startDate: Long,
        endDate: Long
    ): LiveData<List<TransactionData>> {
        return transactionsDao.getCurrentMonthData(startDate, endDate)

    }

    suspend fun addTransactionData(transactionData: TransactionData) {
        transactionsDao.insertTransaction(transactionData)
    }

    suspend fun updateTransactionData(transactionData: TransactionData) {
        transactionsDao.updateTransaction(transactionData)
    }

    suspend fun deleteTransactionData(transactionData: TransactionData) {
        transactionsDao.deleteTransaction(transactionData)
    }
}