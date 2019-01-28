package com.khoerulabu.pulsaapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.khoerulabu.pulsaapp.adpater.DashBoardAdapter
import com.khoerulabu.pulsaapp.model.Transaction
import com.khoerulabu.pulsaapp.model.Users
import com.khoerulabu.pulsaapp.presenter.Presenter
import com.khoerulabu.pulsaapp.utils.invisible
import com.khoerulabu.pulsaapp.utils.visible
import com.khoerulabu.pulsaapp.view.BaseView
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import retrofit2.HttpException

class DashboardActivity : AppCompatActivity(), BaseView{

    private lateinit var dashboardAdapter       : DashBoardAdapter
    private lateinit var mPresenter             : Presenter
    private          var listTransaction        : MutableList<Transaction> = mutableListOf()
    private lateinit var users                  : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //terima data dari intent
        val intent = intent
        val bundle = intent.getBundleExtra("myBundle")
        users  = bundle?.getParcelable("userData") as Users

        // set actionbar title dengan nama username
        val actionBar = supportActionBar
        actionBar?.title = users.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set all info into textView
        lbl_userId_value.text   = users.id.toString()
        lbl_username_value.text = users.username

        button_transaction.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TransactionActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("user",users)
            intent.putExtra("myBundle",bundle)
            startActivity(intent)
        }
        //get all transaction by user id
        lisTransactionByUserId(users)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        logoutProcess()
    }

    private fun logoutProcess() {
        val builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage("Keluar ?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                this.finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }

        val alert = builder.create()
        alert.show()
    }

    override fun showLoading() {
        progressBar_dashboard.visible()
    }

    override fun hideLoading() {
        progressBar_dashboard.invisible()
    }

    private fun lisTransactionByUserId(users : Users){
        //get list transaction by user id
        mPresenter = Presenter(this)
        val postTransactionService = mPresenter.createTransactionPresenter()

        GlobalScope.launch(Dispatchers.Main) {
            progressBar_dashboard.visible()
            val request = postTransactionService.getListTransactionByUserId(users.id)
            try {
                val response = request.await()
                val data = response.body()
                if (data != null) {
                    listTransaction.clear()
                    listTransaction.addAll(data)
                    System.out.println("Test Data List $listTransaction")
                }
                //set resyclerview for list transaction
                dashboardAdapter = DashBoardAdapter(applicationContext, listTransaction) {
                    startActivity<DetailTransactionActivity>("transactionData" to it)
                }
                rv_transaction.layoutManager = LinearLayoutManager(applicationContext)
                rv_transaction.adapter = dashboardAdapter
                dashboardAdapter.notifyDataSetChanged()

            } catch (e: HttpException) {
                Toast.makeText(applicationContext,e.code(), Toast.LENGTH_LONG).show()
            } catch (e: Throwable) {
                Toast.makeText(applicationContext,"Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
            }
            progressBar_dashboard.invisible()
        }
    }

    override fun onRestart() {
        super.onRestart()
        lisTransactionByUserId(users)
    }
}
