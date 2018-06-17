package com.chrisgibson.mybank.Controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.chrisgibson.mybank.Utilities.DATE_CREATED
import com.chrisgibson.mybank.R
import com.chrisgibson.mybank.Utilities.USERNAME
import com.chrisgibson.mybank.Utilities.USER_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(my_toolbar)
        auth = FirebaseAuth.getInstance()
    }

    fun onCancelClicked(view: View){
        finish()
    }

    fun onRegisterClicked(view: View){
        progressBar.visibility = View.VISIBLE
        val email = register_email_text.text.toString()
        val password = register_password_text.text.toString()
        val username = register_username_text.text.toString()

        auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener { result->
                    val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build()
                    result.user.updateProfile(changeRequest)
                            .addOnFailureListener { exception ->
                                Log.e("Exception","Could not update User: ${exception.localizedMessage}")
                            }
                    val data = HashMap<String,Any>()
                    data.put(USERNAME, username)
                    data.put(DATE_CREATED, FieldValue.serverTimestamp())

                    FirebaseFirestore.getInstance().collection(USER_REF).document(result.user.uid)
                            .set(data)
                            .addOnSuccessListener {
                                finish()
                                progressBar.visibility = View.INVISIBLE
                            }
                            .addOnFailureListener {
                                Log.e("Exception", "Could Not Create User document: $it")
                            }
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception","Could not Create User:${exception.localizedMessage}")
                    Snackbar.make(view,"Something went wrong, Please try again later",Snackbar.LENGTH_LONG).show()
                }
    }
}
