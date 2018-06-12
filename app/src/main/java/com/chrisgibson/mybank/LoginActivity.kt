package com.chrisgibson.mybank

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(my_toolbar)

        callbackManager = CallbackManager.Factory.create()
        facebooklogin_button.setReadPermissions("email","public_profile")
        facebooklogin_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null){
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    //firebaseLogin(credential)
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


}
