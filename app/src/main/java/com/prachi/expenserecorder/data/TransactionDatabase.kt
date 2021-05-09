package com.prachi.expenserecorder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prachi.expenserecorder.utils.DateTypeConverter


@Database(entities = [TransactionData::class], version = 1)

abstract class TransactionDatabase : RoomDatabase() {


    abstract fun getTransactionsDao(): TransactionsDao

    companion object {
        private var dbInstance: TransactionDatabase? = null

        fun getDatabaseInstance(context: Context): TransactionDatabase {
            val tempInstance = dbInstance

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this)
            {
                val instance =
                    Room.databaseBuilder(
                        context,
                        TransactionDatabase::class.java,
                        "transaction_data"
                    )
                        .build()

                dbInstance = instance

                return instance
            }


        }
    }

}