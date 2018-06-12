package com.chrisgibson.mybank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_list.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.content_list.*


class ListActivity : AppCompatActivity() {

    val userCollectionRef = FirebaseFirestore.getInstance().collection(USER_REF)
    lateinit var itemsAdapter: ItemsAdapter
    val items = arrayListOf<Item>()
    lateinit var itemsListener: ListenerRegistration
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(my_toolbar)

        fab.setOnClickListener { view ->
            val addExpenseIntent = Intent(this, AddExpenseActivity::class.java)
            startActivity(addExpenseIntent)
        }

        itemsAdapter = ItemsAdapter(this,items)
        item_list_view.adapter = itemsAdapter
        val layoutManager = LinearLayoutManager(this)
        item_list_view.layoutManager = layoutManager
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(1)
        if (auth.currentUser == null){
            menuItem.title = "Login"
        }else{
            menuItem.title = "Logout"
        }
        return super.onPrepareOptionsMenu(menu)
    }

}
