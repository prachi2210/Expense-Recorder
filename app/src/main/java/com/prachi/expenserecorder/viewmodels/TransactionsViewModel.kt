package com.prachi.expenserecorder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.prachi.expenserecorder.data.Repository
import com.prachi.expenserecorder.data.TransactionData
import com.prachi.expenserecorder.data.TransactionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<TransactionData>>

    lateinit var currentMonthData: LiveData<List<TransactionData>>
    //val getString: LiveData<List<String>>
    private val repository: Repository

    init {
        val transactionsDao =
            TransactionDatabase.getDatabaseInstance(application).getTransactionsDao()
        repository = Repository(transactionsDao)

        readAllData = repository.readAllData
        // currentMonthData = repository.getCurrentMonthData
      //  getString = repository.getStringData
    }

    fun addTransaction(data: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransactionData(data)
        }
    }


    fun getTransactionsByMonth(startDate: Long, endDate: Long) {

            currentMonthData = repository.getAllTransactionByMonth(startDate, endDate)

    }


    fun updateTransaction(data: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransactionData(data)
        }
    }


    fun deleteTransaction(data: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransactionData(data)
        }
    }

}