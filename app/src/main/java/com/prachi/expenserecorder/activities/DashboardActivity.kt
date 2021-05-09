package com.prachi.expenserecorder.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.prachi.expenserecorder.R
import com.prachi.expenserecorder.adapters.TransactionsAdapter
import com.prachi.expenserecorder.data.TransactionData
import com.prachi.expenserecorder.utils.AppConstants
import com.prachi.expenserecorder.viewmodels.TransactionsViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_filter_transactions.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.DecimalFormat
import java.util.*


class DashboardActivity : BaseAppCompatActivity(), TransactionsAdapter.ItemClickListener {


    private val transactionList: MutableList<TransactionData> = mutableListOf()
    private lateinit var transactionsViewModel: TransactionsViewModel

    private val transactionsAdapter: TransactionsAdapter by lazy {
        TransactionsAdapter(this@DashboardActivity, transactionList, this)
    }

    private var startDate: Long? = null
    private var endDate: Long? = null

    private var isFabMenuOpen = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setDashboardToolbar()


        rvTransactions.adapter = transactionsAdapter

        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)


        val firstTimeStamp = getFirstTimeStampOfMonth(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        )

        val lastTimeStamp = getLastTimeStampOfMonth(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        )

        transactionsViewModel.getTransactionsByMonth(
            firstTimeStamp, lastTimeStamp
        )
        observeViewModel()


        rvTransactions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && baseFloatingActionButton.isShown) {
                    baseFloatingActionButton.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    baseFloatingActionButton.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    private fun observeViewModel() {
        transactionsViewModel.currentMonthData.observe(this, {
            transactionList.clear()
            transactionList.addAll(it)
            transactionsAdapter.notifyDataSetChanged()

            if (transactionList.isEmpty()) {
                noTvDataDashboard.visibility = View.VISIBLE
            } else {
                noTvDataDashboard.visibility = View.GONE
            }


            var income: Double = 0.0
            var expese: Double = 0.0

            for (i in transactionList) {

                if (i.transaction_type == AppConstants.INCOME.transactionType) {
                    income += i.amount
                }
                else{
                    expese += i.amount
                }
            }
            val format = DecimalFormat("#.##")

            tvIncomeValue.text = "₹".plus(format.format(income))
            tvExpenseValue.text = "₹".plus(format.format(expese))
        })
    }

    private fun setDashboardToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitle.text = getString(R.string.dashboard)

    }

    private fun expandFabMenu() {
        ViewCompat.animate(baseFloatingActionButton).rotation(45.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        addIncomeLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.open_fab_animation
            )
        )
        addExpenseLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.open_fab_animation
            )
        )
        addIncomeFab.isClickable = true
        addExpenseFab.isClickable = true
        isFabMenuOpen = true
    }

    private fun collapseFabMenu() {
        ViewCompat.animate(baseFloatingActionButton).rotation(0.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        addIncomeLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.close_fab_animation
            )
        )
        addExpenseLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.close_fab_animation
            )
        )
        addIncomeFab.isClickable = false
        addExpenseFab.isClickable = false
        isFabMenuOpen = false
    }

    fun onBaseFabClick(view: View?) {
        if (isFabMenuOpen) collapseFabMenu() else expandFabMenu()
    }

    fun onAddIncomeFabClick(view: View?) {

        startActivity(Intent(this, IncomeActivity::class.java))
        collapseFabMenu()
    }

    fun onAddExpenseFabClick(view: View?) {
        startActivity(Intent(this, ExpenseActivity::class.java))
        collapseFabMenu()
    }

    fun viewTransactionsClick(view: View?) {
        startActivity(Intent(this, FilterTransactionsActivity::class.java))

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


}