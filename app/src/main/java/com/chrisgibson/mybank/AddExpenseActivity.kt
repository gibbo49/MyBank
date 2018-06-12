package com.chrisgibson.mybank

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_add_expense.*




class AddExpenseActivity : AppCompatActivity() {

    lateinit var addItemCategory:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        setSupportActionBar(my_toolbar)
        loadSpinner()
        add_expense_category_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               addItemCategory = add_expense_category_select.selectedItem.toString()
                Toast.makeText(applicationContext,addItemCategory,Toast.LENGTH_LONG).show()
            }

        }

    }

    fun loadSpinner(){
        val options = arrayOf("Rent/Mortgage","Automotive/Transport","Bills & Utilities","Home Goods","Entertainment","Health / Medical","Groceries","Eating Out/Fast Food","Work","Educational")
        add_expense_category_select.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
    }
}
