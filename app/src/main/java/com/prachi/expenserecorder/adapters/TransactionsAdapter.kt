package com.prachi.expenserecorder.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prachi.expenserecorder.R
import com.prachi.expenserecorder.data.TransactionData
import com.prachi.expenserecorder.utils.AppConstants
import kotlinx.android.synthetic.main.rv_transactions.view.*
import java.text.DecimalFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class TransactionsAdapter(
    private val context: Context,
    private var transactionList: MutableList<TransactionData>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TransactionsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_transactions, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(transactionList[position])


    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(transactionData: TransactionData) {


            val format = DecimalFormat("#.##")
            val amount = format.format(transactionData.amount)


            itemView.tvItemPrice.text = "₹".plus(amount)
            itemView.tvItemAbout.text = transactionData.comment

              Log.e("PRACHI", transactionData.date.toString())


            itemView.tvTransactionDate.text = convertTime(transactionData.date)


            if (transactionData.transaction_type == AppConstants.EXPENSE.transactionType) {
                itemView.ivType.setBackgroundResource(R.drawable.ic_down_arrow)
            } else {
                itemView.ivType.setBackgroundResource(R.drawable.ic_up_arrow)

            }

            itemView.setOnClickListener {
                itemClickListener.onItemClick(transactionData)
            }

        }

    }


    interface ItemClickListener {

        fun onItemClick(data: TransactionData)
    }


    fun convertTime(time: Long): String? {
        val date = Date(time)
        val format: Format = SimpleDateFormat("dd-MMMM-yyyy")
        return format.format(date)
    }

    fun updateList(dataList: MutableList<TransactionData>) {
        transactionList = dataList
        notifyDataSetChanged()
    }
}