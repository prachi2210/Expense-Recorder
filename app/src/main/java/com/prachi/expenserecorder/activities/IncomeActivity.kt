package com.prachi.expenserecorder.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.prachi.expenserecorder.R
import com.prachi.expenserecorder.data.TransactionData
import com.prachi.expenserecorder.utils.AppConstants
import com.prachi.expenserecorder.viewmodels.TransactionsViewModel
import kotlinx.android.synthetic.main.activity_add_income.*
import kotlinx.android.synthetic.main.toolbar.*
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class IncomeActivity : BaseAppCompatActivity(), View.OnClickListener {

    private lateinit var transactionsViewModel: TransactionsViewModel

    private var previousTransactionData: TransactionData? = null

    private var previousId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)
        setToolbar()


        if (intent != null) {
            previousTransactionData = intent?.extras?.getParcelable("data")
        }

        if (previousTransactionData != null) {
            toolbarTitle.text = getString(R.string.edit_income)
            setData(previousTransactionData!!)

        } else {
            toolbarTitle.text = getString(R.string.add_income)

        }

        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)


    }

    private fun setData(data: TransactionData) {
        previousId = data.id

        val format = DecimalFormat("#.##")
        val amount = format.format(data.amount)

        incomeAmount.setText(amount)

        val formattedDate: String = SimpleDateFormat("dd-MM-yyyy").format(data.date)
        incomeDate.setText(formattedDate)

        incomeComment.setText(data.comment)

        incomeType.setText(data.type)
    }


    private fun insertToDatabase() {


        when {
            checkEmpty(incomeAmount) -> {

                setMessage(getString(R.string.amount_error))
            }

            checkEmpty(incomeDate) -> {
                setMessage(getString(R.string.date_error))

            }

            checkEmpty(incomeType) -> {
                setMessage(getString(R.string.income_type_error))

            }

            checkEmpty(incomeComment) -> {
                setMessage(getString(R.string.comment_error))

            }

            else -> {

                val amount = incomeAmount.text.toString().trim()
                val date = incomeDate.text.toString().trim()
                val type = incomeType.text.toString().trim()
                val comment = incomeComment.text.toString().trim()

                val convertedDate: Date = SimpleDateFormat("dd-MM-yyyy").parse(date)



                if (previousTransactionData != null) {
                    val transactionData: TransactionData =
                        TransactionData(
                            previousId,
                            amount.toDouble(),
                            convertedDate.time,
                            type,
                            comment,
                            AppConstants.INCOME.transactionType
                        )
                    transactionsViewModel.updateTransaction(transactionData)

                    setMessage("Income Updated Successfully!")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()

                    }, 1500)


                } else {
                    val transactionData: TransactionData =
                        TransactionData(
                            0,
                            amount.toDouble(),
                            Timestamp(convertedDate.time).time,
                            type,
                            comment,
                            AppConstants.INCOME.transactionType
                        )
                    transactionsViewModel.addTransaction(transactionData)
                    setMessage("Income Added Successfully!")
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()

                    }, 1500)
                }
            }
        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {


            R.id.btnSaveIncome -> {
                insertToDatabase()

            }

            R.id.incomeDate -> {
                setDatePicker(incomeDate)
            }


        }


    }


    private fun setDatePicker(editText: EditText?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox

                val selectedMonth = String.format("%02d", monthOfYear + 1)
                val selectedDate = String.format("%02d", dayOfMonth)

                editText?.setText("$selectedDate-$selectedMonth-$selectedYear")
            },
            year,
            month,
            day
        )
        dpd.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.delete_menu, menu)
        val delete: MenuItem = menu!!.findItem(R.id.delete)

        delete.isVisible = previousTransactionData != null


        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.delete -> {
                showDeleteDialog()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert!")
        builder.setMessage("Are you sure you want to delete this transaction?")
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->

            transactionsViewModel.deleteTransaction(previousTransactionData!!)

            setMessage("Transaction Deleted Successfully!")
            Handler(Looper.getMainLooper()).postDelayed({
                finish()

            }, 1500)


        }
        builder.setNegativeButton("Cancel")
        { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


}