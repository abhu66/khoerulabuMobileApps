package com.khoerulabu.pulsaapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.khoerulabu.pulsaapp.model.Transaction
import com.khoerulabu.pulsaapp.utils.formatDateLocaleId
import kotlinx.android.synthetic.main.detail_transaction.*
import java.text.DecimalFormat
import android.util.Log
import com.itextpdf.text.pdf.PdfWriter.*
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import java.io.FileNotFoundException
import android.net.Uri
import android.os.StrictMode
import android.os.Build
import com.itextpdf.text.*
import com.khoerulabu.pulsaapp.utils.TableBuilder


class DetailTransactionActivity : AppCompatActivity(){

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_transaction)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // set actionbar title
        val actionBar = supportActionBar
        actionBar?.title = "Detail Transaction"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        /**
         * Used by lame internal apps that haven't done the hard work to get
         * themselves off file:// Uris yet.
         */
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        //terima data dari intent
        val intent = intent
        val transactionDetail  = intent.getParcelableExtra<Transaction>("transactionData")
        val decimal = DecimalFormat("#,###.00")
        detail_lbl_userid.text               = transactionDetail.users?.id.toString()
        detail_lbl_phonenumber.text          = transactionDetail.phoneNumber
        detail_lbl_operator.text             = transactionDetail.operator?.operatorName
        detail_lbl_pulsa.text                = transactionDetail.voucher?.pulsaId?.pulsa.toString()
        detail_lbl_price.text                = "Rp "+decimal.format(transactionDetail.voucher?.hargaVoucher)
        System.out.println("Transaction Date : "+transactionDetail.createdDate)
        detail_lbl_transactiondate.text      = transactionDetail.createdDate?.formatDateLocaleId()

        button_print.setOnClickListener {
            generatePdf(transactionDetail)
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

    private fun generatePdf(transaction : Transaction) {
        val fileName = "TR_"+transaction.id+".pdf"


        val dir =  getPrivateAlbumStorageDir(applicationContext,"PDF")
        val files = File(dir,fileName)
        val pos    = FileOutputStream(files)

        //Step 1
        val document = Document()

        //Step 2
        getInstance(document, pos)

        //Step 3
        document.open()
        document.add(TableBuilder.createTable(transaction))
      /*  //Step 4 Add content
        document.add(Paragraph(detail_lbl_userid.text.toString()))
        document.add(Paragraph(detail_lbl_phonenumber.text.toString()))
        document.add(Paragraph(detail_lbl_operator.text.toString()))
        document.add(Paragraph(detail_lbl_pulsa.text.toString()))
        document.add(Paragraph(detail_lbl_price.text.toString()))
        document.add(Paragraph(detail_lbl_transactiondate.text.toString()))*/

        //Step 5: Close the document
        document.close()

        try {
            val targetUri = Uri.fromFile(files)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(targetUri, "application/pdf")
            startActivity(intent)
        }
        catch(e: FileNotFoundException) {
            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
        } catch (e: DocumentException) {
            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
        }

    }

    private fun getPrivateAlbumStorageDir(context: Context, dirName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_DCIM), dirName)
        if (!file.mkdirs()) {
            Log.e("tag","Directory not created")
        }
        return file
    }

}

