package com.khoerulabu.pulsaapp.utils

import android.content.Context
import android.widget.ArrayAdapter
import com.khoerulabu.pulsaapp.model.Operator
import com.khoerulabu.pulsaapp.model.Pulsa
import com.khoerulabu.pulsaapp.model.SpinnerOperator
import com.khoerulabu.pulsaapp.model.SpinnerPulsa

class SpinnerCustom {

    companion object {
        fun adapterSpinnerOperator(ctx : Context, operators  : List<Operator>) : ArrayAdapter<SpinnerOperator>{
            val listItem : MutableList<SpinnerOperator> = mutableListOf()
            operators.forEach { listItem.add(SpinnerOperator(it.operatorName, it.id)) }

            return ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, listItem)
        }

        fun adapterSpinnerPulsa(ctx : Context, pulsa  : List<Pulsa>) : ArrayAdapter<SpinnerPulsa>{
            val listItem : MutableList<SpinnerPulsa> = mutableListOf()
            pulsa.forEach { listItem.add(SpinnerPulsa(it.pulsa, it.id)) }

            return ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, listItem)
        }
    }
}