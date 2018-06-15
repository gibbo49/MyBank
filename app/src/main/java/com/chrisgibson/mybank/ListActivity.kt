package com.chrisgibson.mybank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_list.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.content_list.*


class ListActivity : AppCompatActivity() {

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

        itemsAdapter = ItemsAdapter(items)
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

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val deletemenuitem = menu.getItem(0)
        val loginmenuItem = menu.getItem(1)
        if (auth.currentUser == null){
            deletemenuitem.isVisible = false
            loginmenuItem.title = "Login"
            loginmenuItem.setIcon(R.drawable.login)
        }else{
            deletemenuitem.isVisible = true
            loginmenuItem.title = "Logout"
            loginmenuItem.setIcon(R.drawable.logout)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout){
            if (auth.currentUser==null){
                val loginIntent = Intent(this,LoginActivity::class.java)
                startActivity(loginIntent)
            }else{

                val user = auth.currentUser
                if (user != null) {
                    for (info in user.providerData) {
                        when (info.providerId) {
                            FacebookAuthProvider.PROVIDER_ID -> LoginManager.getInstance().logOut()
                        }
                    }
                }
                auth.signOut()
                updateUI()
            }
            return true
        }
        return false
    }

    fun setListener(){
        itemsListener = FirebaseFirestore.getInstance().collection(USER_REF).document(getcurrentUser()).collection(ITEMS)
                .orderBy(ITEMNAME, Query.Direction.ASCENDING)
                .addSnapshotListener(this){snapshot, exception ->
                    if (exception != null){
                        Log.e("Exception","Could Not Retreive Documents: $exception")
                    }
                    if (snapshot != null){
                        parseData(snapshot)
                    }
                }
    }

    fun parseData(snapshot: QuerySnapshot){
        items.clear()
        for (document in snapshot.documents){
            val data = document.data
            if (data != null){
                val itemname = data[ITEMNAME] as String
                val itemprice = data[ITEMPRICE] as String
                val category = data[CATEGORY] as String
                val categoryIcon = data[CATEGORY_ICON] as Long

                val newItem = Item(itemname,itemprice,category,categoryIcon)
                items.add(newItem)
            }
        }
        itemsAdapter.notifyDataSetChanged()
        val total = calculateTotal()
        main_total_text.text = "THIS WEEK:\n$ "+ total.decformat(2)
    }

    fun updateUI(){
        if (auth.currentUser == null){
            fab.isEnabled = false
            items.clear()
            itemsAdapter.notifyDataSetChanged()
            main_total_text.setText(R.string.welcome_login)
        }else{
            getcurrentUser()
            fab.isEnabled = true
            setListener()


        }
    }

    fun getcurrentUser():String{
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        return currentUser
    }

    fun calculateTotal():Double{
        var runningTotal = 0.00
        for (i in items){
            val price =  i.itemPrice
            runningTotal = runningTotal + price.toDouble()
        }
        return runningTotal
    }

    fun Double.decformat(digits: Int):String{
        val string = java.lang.String.format("%.${digits}f", this)
        return string
    }
}
