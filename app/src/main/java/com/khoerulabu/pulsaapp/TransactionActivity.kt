package com.khoerulabu.pulsaapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast

import com.khoerulabu.pulsaapp.model.Operator
import com.khoerulabu.pulsaapp.model.Pulsa
import com.khoerulabu.pulsaapp.model.Users
import com.khoerulabu.pulsaapp.model.Voucher
import com.khoerulabu.pulsaapp.presenter.*
import com.khoerulabu.pulsaapp.utils.SpinnerCustom
import com.khoerulabu.pulsaapp.utils.invisible
import com.khoerulabu.pulsaapp.utils.visible
import com.khoerulabu.pulsaapp.view.BaseView
import kotlinx.android.synthetic.main.detail_transaction.*

import kotlinx.android.synthetic.main.transaction_form.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.DecimalFormat



class TransactionActivity : AppCompatActivity(), BaseView {

    private lateinit var mPresnter          : Presenter
    private var listOperator                : MutableList<Operator> = mutableListOf()
    private var listPulsa                   : MutableList<Pulsa> = mutableListOf()
    private var operatorId                  : Long? = 0
    private var pulsaId                     : Long? = 0
    private lateinit var operator           : Operator
    private lateinit var voucher            : Voucher
    private lateinit var userInfo           : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_form)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // set actionbar title dengan nama username
        val actionBar   = supportActionBar
        actionBar?.title           = "Transaction"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //terima data dari Dashboard
        val intent  = intent
        val bundle = intent.getBundleExtra("myBundle")
        userInfo            = bundle?.getParcelable("user") as Users

        //inisiasi presenter
        mPresnter = Presenter(this)

        val pulsaServices      = mPresnter.createPulsaPresenter()
        val voucherServices  = mPresnter.createVoucherPresenter()

        //get list all operator
         listAllOperator()
        //spinner operator
        spinner_operator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                operatorId = listOperator[position].id!!
                GlobalScope.launch(Dispatchers.Main){
                    progressBar_tr.visible()
                    val request = pulsaServices.getListPulsaByOpId(operatorId!!)
                    try{
                        val response = request.await()
                        val data = response.body()
                        listPulsa.clear()
                        if (data != null) {
                            listPulsa.addAll(data)
                        }
                        if (listPulsa.isEmpty()) {
                            Toast.makeText(applicationContext,"Pulsa Operator ${listOperator[position].operatorName} kosong !",
                                Toast.LENGTH_LONG).show()
                        }
                        //set value for spinner pulsa
                        spinner_pulsa.adapter = SpinnerCustom.adapterSpinnerPulsa(spinner_pulsa.context, listPulsa)
                        progressBar_tr.invisible()

                    } catch (e: HttpException) {
                        Toast.makeText(applicationContext,e.code(),Toast.LENGTH_LONG).show()
                    } catch (e: Throwable) {
                        Toast.makeText(applicationContext,"Ooops: Something else went wrong",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //spinner pulsa
        spinner_pulsa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                pulsaId = listPulsa[position].id!!
                operatorId = listPulsa[position].operator?.id
                operator = listPulsa[position].operator!!
                GlobalScope.launch(Dispatchers.Main) {
                    progressBar_tr.visible()
                    val request = voucherServices.findByOperatorIdAndPulsaId(
                        listPulsa[position].operator?.id,
                        listPulsa[position].id!!
                    )
                    try {
                        val response = request.await()
                        val data = response.body()
                        if (data != null) {
                            voucher = data
                            val decimal = DecimalFormat("#,###.00")
                            lbl_harga_voucher.text = "Rp." + decimal.format(data.hargaVoucher)
                            progressBar_tr.invisible()
                        } else {
                            progressBar_tr.invisible()
                            lbl_harga_voucher.text = "Rp.0"
                            Toast.makeText(applicationContext, "Voucher kosong !", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: HttpException) {
                        Toast.makeText(applicationContext, e.code(), Toast.LENGTH_LONG).show()
                    } catch (e: Throwable) {
                        Toast.makeText(applicationContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //button save click
        button_save.setOnClickListener {
            when {
                TextUtils.isEmpty(field_phoneNumber.text) -> field_phoneNumber.error = "Phone number is required"
                lbl_harga_voucher.text == "Rp.0" || lbl_harga_voucher.text == "Rp." -> Toast.makeText(applicationContext,"Voucher kosong !",Toast.LENGTH_LONG).show()
                else -> saveTransaction(userInfo)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
        progressBar_tr.visible()
    }

    override fun hideLoading() {
        progressBar_tr.invisible()

    }

    private fun saveTransaction(usersInfo : Users){
        val jsonParam = HashMap<String, Any?>()
        jsonParam["phoneNumber"] = field_phoneNumber.text.toString()
        jsonParam["users"] = usersInfo
        jsonParam["operator"] = operator
        jsonParam["voucher"] = voucher


        mPresnter = Presenter(this)
        val postTransactionServices = mPresnter.createTransactionPresenter()

        GlobalScope.launch(Dispatchers.Main) {
            progressBar_tr.visible()
            val request = postTransactionServices.saveTransaction(jsonParam)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Transaction Sukses !", Toast.LENGTH_LONG).show()
                    Log.e("tag", "post submitted to API." + response.body().toString())
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Transaction Failed !", Toast.LENGTH_LONG).show()
                    Log.e("tag", "post submitted to API.s" + response.body().toString())
                }
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, e.code(), Toast.LENGTH_LONG).show()
            } catch (e: Throwable) {
                Toast.makeText(applicationContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
            }
            progressBar_tr.invisible()
        }
    }

    private fun listAllOperator(){
        //assigment postService
        val operatorServices    = mPresnter.createOpearatorPresenter()

        GlobalScope.launch(Dispatchers.Main) {
            progressBar_tr.visible()
            val request = operatorServices.getAllOperator()
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val data = response.body()
                    listOperator.clear()
                    if (data != null) {
                        listOperator.addAll(data)
                    } else {
                        Toast.makeText(applicationContext,"Operator kosong !",Toast.LENGTH_LONG).show()
                    }
                }
                //set value for spinner operator
                spinner_operator.adapter = SpinnerCustom.adapterSpinnerOperator(spinner_operator.context, listOperator)
                progressBar_tr.invisible()

            } catch (e: HttpException) {
                Toast.makeText(applicationContext,e.code(),Toast.LENGTH_LONG).show()
            } catch (e: Throwable) {
                Toast.makeText(applicationContext,"Ooops: Something else went wrong",Toast.LENGTH_LONG).show()
            }
            progressBar_tr.invisible()
        }

    }
}