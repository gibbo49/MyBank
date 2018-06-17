package com.chrisgibson.mybank.Controller

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chrisgibson.mybank.R
import com.chrisgibson.mybank.Utilities.FunctionRecycler
import com.chrisgibson.mybank.Utilities.LINKED_ID
import com.chrisgibson.mybank.Utilities.USER_REF
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_link.*

class LinkActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link)
        val prefs = applicationContext.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
        currentUserId_text.text = FunctionRecycler.getcurrentUser()
        val savedlink = prefs.getString(LINKED_ID,null)
        if (savedlink != null){
            link_account_text.setText(savedlink.toString())
        }
    }

    fun onSavedClicked(view:View){
        val prefs = applicationContext.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
        val linkAccount = link_account_text.text.toString()
        if (linkAccount != ""){
            val editor = prefs.edit()
            editor.putString(LINKED_ID,linkAccount)
            editor.apply()
            val userDocumentId = FunctionRecycler.getcurrentUser()
            FirebaseFirestore.getInstance().collection(USER_REF).document(userDocumentId)
                    .update(LINKED_ID,linkAccount)
                    .addOnSuccessListener {
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Exception","Could not add linked account:$exception")
                    }
        }else{
            finish()
        }
    }
}
