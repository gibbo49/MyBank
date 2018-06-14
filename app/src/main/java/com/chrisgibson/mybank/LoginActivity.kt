package com.chrisgibson.mybank

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var callbackManager : CallbackManager
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(my_toolbar)
        auth = FirebaseAuth.getInstance()

        callbackManager = CallbackManager.Factory.create()
        facebooklogin_button.setReadPermissions("email","public_profile")
        facebooklogin_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null){
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this@LoginActivity,"Login Successfull",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@LoginActivity,"Something went wrong. Please try again later",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onCancel() {
                Log.d("Cancel","Facebook login cancelled")
                //updateUI(null)
            }

            override fun onError(error: FacebookException?) {
                Log.e("Error","Facebook login error")
                //updateUI(null)
            }
        })
    }

    fun loginClicked(view: View){
        val email = login_email_text.text.toString()
        val password = login_password_text.text.toString()

        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                    Log.e("Exception","Could not sign in user: ${it.localizedMessage}")
                    Snackbar.make(view,"Could not sign in user. Please Try Again.",Snackbar.LENGTH_LONG)
                            .show()
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show()
                }

    }

    fun loginRegisterClicked(view: View){
        val registerIntent = Intent(this,RegisterActivity::class.java)
        startActivity(registerIntent)
    }


}
