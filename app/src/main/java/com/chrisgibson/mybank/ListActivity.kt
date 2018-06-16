package com.chrisgibson.mybank

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlin.concurrent.thread


class ListActivity : AppCompatActivity(), ItemOptionsClickListener {

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

        itemsAdapter = ItemsAdapter(items,this)
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

    override fun itemOptionsMenuClicked(item: Item) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.options_menu, null)
        val deleteBtn = dialogView.findViewById<Button>(R.id.option_delete_btn)
        val editBtn = dialogView.findViewById<Button>(R.id.option_edit_btn)

        builder.setView(dialogView).setNegativeButton("Cancel") { _, _ -> }
        val ad = builder.show()

        deleteBtn.setOnClickListener {
            val itemRef = FirebaseFirestore.getInstance().collection(USER_REF).document(getcurrentUser()).collection(ITEMS).document(item.documentId)
            val collectionRef = FirebaseFirestore.getInstance().collection(USER_REF).document(getcurrentUser()).collection(ITEMS)

            deleteCollection(collectionRef,item){success->
                if (success){
                    itemRef.delete()
                            .addOnSuccessListener {
                                ad.dismiss()
                            }
                            .addOnFailureListener{exception ->
                                Log.e("Exception","Could no delete item:$exception")
                            }
                }
            }
        }
        editBtn.setOnClickListener {

        }


/*

        editBtn.setOnClickListener {
            val updateIntent = Intent(this,UpdateCommentActivity::class.java)
            updateIntent.putExtra(THOUGHT_DOC_ID_EXTRA, thought.documentId)
            updateIntent.putExtra(THOUGHT_TXT_EXTRA,thought.thoughtTxt)
            updateIntent.putExtra(COMMENT_DOC_ID_EXTRA,"0")
            updateIntent.putExtra(COMMENT_TXT_EXTRA,"0")
            ad.dismiss()
            startActivity(updateIntent)

        }

    }*/
    }


    fun deleteCollection(collection: CollectionReference, item: Item, complete: (Boolean) -> Unit) {
            collection.get().addOnSuccessListener { snapshot ->
                thread {
                    val batch = FirebaseFirestore.getInstance().batch()
                    for (document in snapshot) {
                        val docRef = FirebaseFirestore.getInstance().collection(USER_REF).document(getcurrentUser()).collection(ITEMS).document(item.documentId)
                        batch.delete(docRef)
                    }
                    batch.commit()
                            .addOnSuccessListener {
                                complete(true)
                            }.addOnFailureListener { exception ->
                                Log.e("Exception", "Could not delete subcollection: ${exception.localizedMessage}")
                            }
                }
            }.addOnFailureListener { exception ->
                Log.e("Exception", "Could not retreive documents: ${exception.localizedMessage}")
            }
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
                val user = data[USER_REF] as String
                val documentId = document.id

                val newItem = Item(itemname,itemprice,category,categoryIcon,user,documentId)
                items.add(newItem)
            }
        }
        itemsAdapter.notifyDataSetChanged()
        val total = calculateTotal()
        main_total_text.text = getString(R.string.total,total)
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
}
