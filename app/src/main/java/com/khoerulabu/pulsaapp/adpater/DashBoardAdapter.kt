package com.khoerulabu.pulsaapp.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khoerulabu.pulsaapp.R
import com.khoerulabu.pulsaapp.model.Transaction
import kotlinx.android.synthetic.main.transaction_item.view.*
import java.text.DecimalFormat

class DashBoardAdapter (
    private val context: Context,
    private val transaction : List<Transaction>,
    private val listener: (Transaction) -> Unit)
        : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(v)
    }

    override fun getItemCount(): Int = transaction.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindItem(transaction[position],listener)
    }

}

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val decimal = DecimalFormat("#,###.00")

    @SuppressLint("SetTextI18n")
    fun bindItem(transaction :Transaction, listener: (Transaction) -> Unit) {
            itemView.item_operator_tr.text = transaction.operator?.operatorName
            itemView.item_pulsa_tr.text    = "Rp "+decimal.format(transaction.voucher?.hargaVoucher)

        itemView.setOnClickListener {
            listener(transaction)
        }
    }
}





