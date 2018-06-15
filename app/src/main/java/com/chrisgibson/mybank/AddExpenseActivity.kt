package com.chrisgibson.mybank

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_add_expense.*




class AddExpenseActivity : AppCompatActivity() {

    lateinit var addItemCategory:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        setSupportActionBar(my_toolbar)
        loadSpinner()
        add_expense_category_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               addItemCategory = add_expense_category_select.selectedItem.toString()
            }

        }

    }

    private fun loadSpinner(){
        val options = arrayOf("Rent/Mortgage","Automotive/Transport","Bills & Utilities","Home Goods","Entertainment","Health / Medical","Groceries","Eating Out/Fast Food","Work","Educational","Clothing","Other")
        add_expense_category_select.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
    }

    fun addItemClicked(view: View){
        val userDocumentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val rawPrice = add_expense_price_text.text.toString().toDouble()
        val data = HashMap<String, Any>()
        data.put(ITEMNAME, add_expense_name.text.toString())
        data.put(ITEMPRICE, rawPrice.decformat(2))
        data.put(CATEGORY, addItemCategory)
        data.put(USER_REF, FirebaseAuth.getInstance().currentUser?.uid.toString())
        data.put(CATEGORY_ICON, getIcon(addItemCategory))

        FirebaseFirestore.getInstance().collection(USER_REF).document(userDocumentId).collection(ITEMS)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {exception ->
                    Log.e("EXC", "Could not add post: $exception")
                }
    }

    fun getIcon(addItemCategory:String):Int{
        return when (addItemCategory){
            "Rent/Mortgage" -> R.drawable.mortgage
            "Automotive/Transport" -> R.drawable.auto
            "Bills & Utilities" -> R.drawable.bills
            "Home Goods" -> R.drawable.homegoods
            "Entertainment" -> R.drawable.entertainment
            "Health / Medical" -> R.drawable.health
            "Groceries" -> R.drawable.groceries
            "Eating Out/Fast Food" -> R.drawable.eatingout
            "Work" -> R.drawable.work
            "Educational" -> R.drawable.educational
            "Clothing" -> R.drawable.clothing
            else -> R.drawable.other
        }
    }

    fun onCancelClicked(view: View){
        finish()
    }

    fun Double.decformat(digits: Int):String{
        val string = java.lang.String.format("%.${digits}f", this)
        return string
    }
}
