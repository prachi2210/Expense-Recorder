package com.prachi.expenserecorder.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.prachi.expenserecorder.R
import com.prachi.expenserecorder.adapters.TransactionsAdapter
import com.prachi.expenserecorder.data.TransactionData
import com.prachi.expenserecorder.utils.AppConstants
import com.prachi.expenserecorder.viewmodels.TransactionsViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_filter_transactions.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class FilterTransactionsActivity : BaseAppCompatActivity(), View.OnClickListener,
    TransactionsAdapter.ItemClickListener {


    private var transactionList: MutableList<TransactionData> = mutableListOf()
    val filteredTextList: MutableList<TransactionData> = mutableListOf()
    var spinnerText: String = ""

    var mSearchView: SearchView? = null

    var searchTextValue: String = ""


    private val transactionsAdapter: TransactionsAdapter by lazy {
        TransactionsAdapter(this@FilterTransactionsActivity, transactionList, this)
    }

    private lateinit var transactionsViewModel: TransactionsViewModel

    private val calendar: Calendar by lazy {
        Calendar.getInstance(Locale.getDefault())
    }

    private var month = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_transactions)


        setToolbar()
        setSpinner()
        toolbarTitle.text = getString(R.string.my_transactions)

        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)

        month = calendar.get(Calendar.MONTH)

        rvFilterTransactions.adapter = transactionsAdapter


        transactionsViewModel.getTransactionsByMonth(
            getFirstTimeStampOfMonth(month, Calendar.getInstance().get(Calendar.YEAR)),
            getLastTimeStampOfMonth(month, Calendar.getInstance().get(Calendar.YEAR))
        )


        tvMonth.text =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())


    }

    private fun observeViewModel() {
        transactionsViewModel.currentMonthData.observe(this, { list ->

            transactionList.clear()

            transactionList.addAll(list)

            if (transactionList.isEmpty()) {
                noTvData.visibility = View.VISIBLE
            } else {
                noTvData.visibility = View.GONE
            }


            if (searchTextValue.isNotEmpty()) {
                searchFilterOnList()
            } else {
                transactionsAdapter.notifyDataSetChanged()

            }

        })

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivPreviousMonth -> {
                if (month > 0) {
                    month--
                    calendar.add(Calendar.MONTH, -1)

                }

                tvMonth.text =
                    calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                disableButton()

                transactionsViewModel.getTransactionsByMonth(
                    getFirstTimeStampOfMonth(month, spinnerText.toInt()),
                    getLastTimeStampOfMonth(month, spinnerText.toInt())
                )
                observeViewModel()

            }

            R.id.ivNextMonth -> {
                if (month < 11) {

                    month++
                    calendar.add(Calendar.MONTH, 1)
                }

                tvMonth.text =
                    calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                transactionsViewModel.getTransactionsByMonth(
                    getFirstTimeStampOfMonth(month, spinnerText.toInt()),
                    getLastTimeStampOfMonth(month, spinnerText.toInt())
                )
                observeViewModel()

                disableButton()
            }


        }
    }


    private fun disableButton() {

        when (month) {

            0 -> {
                ivPreviousMonth.setColorFilter(
                    ContextCompat.getColor(this, R.color.grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

            }

            11 -> {
                ivNextMonth.setColorFilter(
                    ContextCompat.getColor(this, R.color.grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                );

            }
            else -> {
                ivPreviousMonth.setColorFilter(
                    ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivNextMonth.setColorFilter(
                    ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val mSearch: MenuItem = menu!!.findItem(R.id.appSearchBar)
        mSearchView = mSearch.actionView as SearchView
        mSearchView?.queryHint = "Search"

        mSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                searchTextValue = newText
                searchFilterOnList()



                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onItemClick(data: TransactionData) {
        if (data.transaction_type == AppConstants.EXPENSE.transactionType) {
            val intent = Intent(this, ExpenseActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        } else {

            val intent = Intent(this, IncomeActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }


    private fun setSpinner() {
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance()[Calendar.YEAR]
        for (i in 2000..thisYear) {
            years.add(i.toString())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)

        spinner.adapter = adapter
        spinner.setSelection(adapter.count - 1);



        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerText = parent?.getItemAtPosition(position).toString()
                transactionsViewModel.getTransactionsByMonth(
                    getFirstTimeStampOfMonth(month, spinnerText.toInt()),
                    getLastTimeStampOfMonth(month, spinnerText.toInt())
                )

                observeViewModel()
            }
        }


    }

    private fun searchFilterOnList() {
        filteredTextList.clear()

        if (searchTextValue.isNotEmpty()) {
            for (data in transactionList) {
                if (data.transaction_type.toLowerCase(Locale.US)
                        .contains(searchTextValue.toLowerCase(Locale.US)) || data.comment.toLowerCase(
                        Locale.US
                    )
                        .contains(searchTextValue.toLowerCase(Locale.US)) ||
                    data.type.toLowerCase(Locale.US)
                        .contains(searchTextValue.toLowerCase(Locale.US))
                ) {

                    filteredTextList.add(data)
                }

            }

            if (filteredTextList.isEmpty()) {
                noTvData.visibility = View.VISIBLE
            } else {
                noTvData.visibility = View.GONE
            }

            transactionsAdapter.updateList(filteredTextList)

        } else {

            transactionsAdapter.updateList(transactionList)

            if (transactionList.isEmpty()) {
                noTvData.visibility = View.VISIBLE
            } else {
                noTvData.visibility = View.GONE


            }
        }
    }


}