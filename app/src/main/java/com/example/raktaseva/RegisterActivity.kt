package com.example.raktaseva

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
class RegisterActivity : AppCompatActivity() {
    var name: EditText? = null
    var blood: EditText? = null
    var location: EditText? = null
    var phone: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var register: Button? = null

    var auth: FirebaseAuth? = null

    var database: FirebaseDatabase? = null
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById<EditText>(R.id.name)
        blood = findViewById<EditText>(R.id.blood)
        location = findViewById<EditText>(R.id.location)
        phone = findViewById<EditText>(R.id.phone)
        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password)

        register = findViewById<Button>(R.id.register)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        reference = database!!.getReference("Donors")

        register!!.setOnClickListener(View.OnClickListener { view: View? ->
            val userName =
                name!!.getText().toString().trim { it <= ' ' }
            val userBlood =
                blood!!.getText().toString().trim { it <= ' ' }

            val userLocation =
                location!!.getText().toString().trim { it <= ' ' }

            val userPhone =
                phone!!.getText().toString().trim { it <= ' ' }

            val userEmail =
                email!!.getText().toString().trim { it <= ' ' }

            val userPassword =
                password!!.getText().toString().trim { it <= ' ' }
            auth!!.createUserWithEmailAndPassword(
                userEmail,
                userPassword
            ).addOnCompleteListener(OnCompleteListener { task: Task<AuthResult?>? ->
                if (task!!.isSuccessful()) {
                    val donor =
                        HashMap<String?, String?>()

                    donor.put("name", userName)
                    donor.put("blood", userBlood)
                    donor.put("location", userLocation)
                    donor.put("phone", userPhone)
                    donor.put("email", userEmail)

                    reference!!.push().setValue(donor)

                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this@RegisterActivity,
                            MainActivity::class.java
                        )
                    )

                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        task.getException()!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        })
    }
}