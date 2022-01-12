package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import java.util.ArrayList

class Setting : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val attention_target = arrayListOf<String>("Sender","Revice")
        val spinnerAdapterAttention = context?.let {
            ArrayAdapter(it,android.R.layout.simple_spinner_item,attention_target)
        }

        val spinner_attention = view.findViewById<Spinner>(R.id.spinner_attention)
        spinner_attention.adapter = spinnerAdapterAttention

        val gender_target = arrayListOf<String>("Male","Female")
        val spinnerAdapterGender = context?.let {
            ArrayAdapter(it,android.R.layout.simple_spinner_item,gender_target)
        }

        val spinner_gender = view.findViewById<Spinner>(R.id.spinner_gender)
        spinner_gender.adapter = spinnerAdapterGender
        return view
    }
}