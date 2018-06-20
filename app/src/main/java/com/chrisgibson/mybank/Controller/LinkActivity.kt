package com.chrisgibson.mybank.Controller

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
        val getuserdoc = FirebaseFirestore.getInstance().collection(USER_REF).document(FunctionRecycler.getcurrentUser()).get().addOnCompleteListener {
            val linkedId = it.result.get(LINKED_ID).toString()
            link_account_text.setText(linkedId)
        currentUserId_text.text = FunctionRecycler.getcurrentUser()
        }
    }

    fun onSavedClicked(view:View) {
        val saveuserdoc = FirebaseFirestore.getInstance().collection(USER_REF).document(FunctionRecycler.getcurrentUser()).update(LINKED_ID, link_account_text.text.toString()).addOnCompleteListener {
            finish()
        }.addOnFailureListener {exception ->
            Log.e("exception","Could not update document exception:$exception")
            Toast.makeText(this,"Could Not Save Link. Please Try Again",Toast.LENGTH_LONG).show()
                }
        if (link_account_text.text.toString() == ""){

        }
    }

}
