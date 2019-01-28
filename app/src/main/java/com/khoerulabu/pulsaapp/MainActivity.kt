package com.khoerulabu.pulsaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build.VERSION.SDK_INT
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.khoerulabu.pulsaapp.presenter.Presenter
import com.khoerulabu.pulsaapp.utils.invisible
import com.khoerulabu.pulsaapp.utils.visible
import com.khoerulabu.pulsaapp.view.BaseView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.os.StrictMode
import android.text.TextUtils
import android.widget.Toast


class MainActivity : AppCompatActivity(), BaseView{

    private lateinit var mPresenter : Presenter

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        // set actionbar title dengan nama username
        val actionBar = supportActionBar
        actionBar?.title = "Welcome to Pulsa App"

        button_login.setOnClickListener {
            loginProses()
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    private fun loginProses() {
        when {
            TextUtils.isEmpty(field_username.text) -> field_username.error = "Username is required !"
            TextUtils.isEmpty(field_password.text) -> field_password.error = "Password is required !"
            else -> {
                //initiate service presennter
                mPresenter = Presenter(this)
                val userServices = mPresenter.createUsersPresenter()
                GlobalScope.launch(Dispatchers.Main) {
                    progressBar.visible()
                    val request = userServices.login(field_username.text.toString(), field_password.text.toString())
                    try {
                        val response = request.await()
                        val data = response.body()
                        if (response.isSuccessful) {
                            if (data != null) {
                                Toast.makeText(applicationContext, "Login Success !", Toast.LENGTH_LONG).show()
                                //kirim data ke dahsboard
                                val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                                val bundle = Bundle()
                                bundle.putParcelable("userData", data)
                                intent.putExtra("myBundle", bundle)
                                startActivity(intent)
                            } else {
                                Toast.makeText(applicationContext, "Login gagal !", Toast.LENGTH_LONG).show()
                            }
                            progressBar.invisible()

                        } else {
                            Toast.makeText(applicationContext, "Connection Failed !", Toast.LENGTH_LONG).show()
                            Log.e("tag", "post submitted to API.s" + response.body().toString())
                        }
                    } catch (e: HttpException) {
                        Toast.makeText(applicationContext, "Connection Failed  !" + e.code(), Toast.LENGTH_LONG).show()
                    } catch (e: Throwable) {
                        Toast.makeText(applicationContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
