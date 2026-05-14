package com.example.raktaseva

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity :
    AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        val nameText =
            findViewById<TextView>(R.id.nameText)

        val emailText =
            findViewById<TextView>(R.id.emailText)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)

        val user =
            FirebaseAuth
                .getInstance()
                .currentUser

        val database =
            FirebaseDatabase
                .getInstance()
                .getReference("users")

        database.child(user!!.uid)
            .get()
            .addOnSuccessListener {

                nameText.text =
                    it.child("name")
                        .value.toString()

                emailText.text =
                    it.child("email")
                        .value.toString()
            }

        logoutBtn.setOnClickListener {

            FirebaseAuth
                .getInstance()
                .signOut()

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }
}