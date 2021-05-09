package com.prachi.expenserecorder.activities

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.prachi.expenserecorder.R
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import android.view.MotionEvent as MotionEvent1


abstract class BaseAppCompatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
           window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.grey);

        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent1?): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(ev)

        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = ev!!.rawX + w.left - scrcoords[0]
            val y = ev.rawY + w.top - scrcoords[1]
            if (ev.action == MotionEvent1.ACTION_UP
                && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
            ) {
                disappearKeyboard()
                view.clearFocus()
            }
        }
        return ret
    }

    private fun disappearKeyboard() {
        val imm =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) imm.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun setMessage(string: String) {

        val snackBar: Snackbar =
            Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.parseColor("#23afe2"))

        snackBar.show()
        Snackbar.make(
            findViewById(android.R.id.content),
            string,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun checkEmpty(editText: EditText): Boolean {
        return TextUtils.isEmpty(editText.text.toString().trim())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun getFirstTimeStampOfMonth(month: Int, year: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal[Calendar.HOUR_OF_DAY] = cal.getActualMinimum(Calendar.HOUR_OF_DAY)
        cal[Calendar.MINUTE] = cal.getActualMinimum(Calendar.MINUTE)
        cal[Calendar.SECOND] = cal.getActualMinimum(Calendar.SECOND)
        cal[Calendar.MILLISECOND] = cal.getActualMinimum(Calendar.MILLISECOND)

        return cal.timeInMillis
    }

    fun getLastTimeStampOfMonth(month: Int, year: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        cal[Calendar.HOUR_OF_DAY] = cal.getActualMaximum(Calendar.HOUR_OF_DAY)
        cal[Calendar.MINUTE] = cal.getActualMaximum(Calendar.MINUTE)
        cal[Calendar.SECOND] = cal.getActualMaximum(Calendar.SECOND)
        cal[Calendar.MILLISECOND] = cal.getActualMaximum(Calendar.MILLISECOND)

        return cal.timeInMillis
    }



}
