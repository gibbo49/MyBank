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
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_list.*

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
            override fun onCancel() {
                Log.d("Cancel","Facebook login cancelled")
            }

            override fun onError(error: FacebookException?) {
                Log.e("Error","Facebook login error")
            }

            override fun onSuccess(result: LoginResult?) {
                if (result != null){
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    firebaseLogin(credential)
                    }
                }
        })
    }

    fun loginClicked(view: View){
        val email = login_email_text.text.toString()
        val password = login_password_text.text.toString()

        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Log.e("Exception","Could not sign in user: ${it.localizedMessage}")
                    Snackbar.make(view,"Could not sign in user. Please Try Again.",Snackbar.LENGTH_LONG)
                            .show()
                }

    }

    fun loginRegisterClicked(view: View){
        val registerIntent = Intent(this,RegisterActivity::class.java)
        startActivity(registerIntent)
        finish()
    }

    fun firebaseLogin(credential: AuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
            finish()
            }else{
                Log.e("Error","Firebase sign in failed", it.exception)
                Toast.makeText(this,"Authentication Failed",Toast.LENGTH_LONG).show()
            }
        }
    }


}
