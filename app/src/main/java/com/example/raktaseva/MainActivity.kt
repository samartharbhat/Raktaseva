package com.example.raktaseva

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var emergencyBtn: Button
    private lateinit var mapsBtn: Button
    private lateinit var profileBtn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        name = findViewById(R.id.name)

        email = findViewById(R.id.email)

        password = findViewById(R.id.password)

        loginBtn = findViewById(R.id.loginBtn)

        registerBtn = findViewById(R.id.registerBtn)

        emergencyBtn = findViewById(R.id.emergencyBtn)

        mapsBtn = findViewById(R.id.mapsBtn)


        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {

            val userEmail =
                email.text.toString().trim()

            val userPassword =
                password.text.toString().trim()

            auth.signInWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    startActivity(
                        Intent(
                            this,
                            DonorListActivity::class.java
                        )
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        registerBtn.setOnClickListener {

            val userName =
                name.text.toString().trim()

            val userEmail =
                email.text.toString().trim()

            val userPassword =
                password.text.toString().trim()

            auth.createUserWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val userId =
                        auth.currentUser?.uid

                    val database =
                        FirebaseDatabase
                            .getInstance()
                            .getReference("users")

                    val userData =
                        hashMapOf<String, String>(

                            "name" to userName,

                            "email" to userEmail
                        )

                    database.child(userId!!)
                        .setValue(userData)

                    Toast.makeText(
                        this,
                        "Registration Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        emergencyBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EmergencyRequestActivity::class.java
                )
            )
        }

        mapsBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MapsActivity::class.java
                )
            )
        }

    }
}