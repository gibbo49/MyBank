package com.chrisgibson.mybank.Utilities

import com.google.firebase.auth.FirebaseAuth

object FunctionRecycler {

    fun getcurrentUser():String{
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        return currentUser
    }
}