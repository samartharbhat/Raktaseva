package com.example.raktaseva

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
class EmergencyRequestActivity : AppCompatActivity() {

    private lateinit var bloodGroup: EditText
    private lateinit var hospital: EditText
    private lateinit var sendRequestBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_emergency_request)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    200
                )
            }
        }
        bloodGroup = findViewById(R.id.bloodGroup)
        hospital = findViewById(R.id.hospital)
        sendRequestBtn = findViewById(R.id.sendRequestBtn)

        createNotificationChannel()

        sendRequestBtn.setOnClickListener {

            val blood =
                bloodGroup.text.toString().trim()

            val location =
                hospital.text.toString().trim()

            val database =
                FirebaseDatabase.getInstance()

            val reference =
                database.getReference("EmergencyRequests")

            val request =
                HashMap<String, String>()

            request["blood"] = blood
            request["hospital"] = location

            reference.push().setValue(request)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Emergency Request Sent",
                        Toast.LENGTH_SHORT
                    ).show()

                    showNotification(
                        blood,
                        location
                    )

                    bloodGroup.text.clear()
                    hospital.text.clear()
                }
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "blood_request_channel",
                "Blood Requests",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(
        blood: String,
        hospital: String
    ) {

        val builder =
            NotificationCompat.Builder(
                this,
                "blood_request_channel"
            )
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("🚨 Emergency Blood Request")
                .setContentText(
                    "$blood blood needed at $hospital"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )

        with(NotificationManagerCompat.from(this)) {

            notify(1, builder.build())
        }
    }
}