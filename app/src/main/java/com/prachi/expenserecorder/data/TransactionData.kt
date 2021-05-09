package com.prachi.expenserecorder.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prachi.expenserecorder.utils.DateTypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "transaction_data")

@Parcelize
data class TransactionData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Double,
    val date: Long,
    val type: String,
    val comment: String,
    val transaction_type: String
) : Parcelable
