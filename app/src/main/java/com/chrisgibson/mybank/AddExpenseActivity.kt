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
                Toast.makeText(applicationContext,addItemCategory,Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun loadSpinner(){
        val options = arrayOf("Rent/Mortgage","Automotive/Transport","Bills & Utilities","Home Goods","Entertainment","Health / Medical","Groceries","Eating Out/Fast Food","Work","Educational")
        add_expense_category_select.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
    }

    fun addItemClicked(view: View){
        val userDocumentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val data = HashMap<String, Any>()
        data.put(ITEMNAME, add_expense_name.text.toString())
        data.put(ITEMPRICE, add_expense_price_text.text.toString())
        data.put(CATEGORY, addItemCategory)
        data.put(USER_REF, FirebaseAuth.getInstance().currentUser?.uid.toString())

        FirebaseFirestore.getInstance().collection(USER_REF).document(userDocumentId).collection(ITEMS)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {exception ->
                    Log.e("EXC", "Could not add post: $exception")
                }
    }

    fun onCancelClicked(view: View){
        finish()
    }
}
