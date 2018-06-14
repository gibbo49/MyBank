package com.chrisgibson.mybank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
            menuItem.setIcon(R.drawable.login)
        }else{
            menuItem.title = "Logout"
            menuItem.setIcon(R.drawable.logout)
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
            }
            return true
        }
        return false
    }

}
