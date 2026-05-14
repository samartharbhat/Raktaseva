package com.example.raktaseva

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.*

import com.google.firebase.database.*

import java.util.Locale

class DonorListActivity :
    AppCompatActivity() {

    private lateinit var listView:
            ListView

    private lateinit var searchBlood:
            EditText

    private lateinit var searchBtn:
            Button

    private lateinit var profileBtn:
            Button

    private lateinit var locationText:
            TextView

    private lateinit var donorList:
            ArrayList<HashMap<String, String>>

    private lateinit var adapter:
            DonorAdapter

    private lateinit var database:
            FirebaseDatabase

    private lateinit var reference:
            DatabaseReference

    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_donor_list
        )

        listView =
            findViewById(R.id.listView)

        searchBlood =
            findViewById(R.id.searchBlood)

        searchBtn =
            findViewById(R.id.searchBtn)

        profileBtn =
            findViewById(R.id.profileBtn)

        locationText =
            findViewById(R.id.locationText)

        donorList = ArrayList()

        adapter =
            DonorAdapter(
                this,
                donorList
            )

        listView.adapter = adapter

        fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        getCurrentLocation()

        database =
            FirebaseDatabase.getInstance()

        reference =
            database.getReference("Donors")

        profileBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        searchBtn.setOnClickListener {

            val searchText =
                searchBlood.text
                    .toString()
                    .trim()

            reference.addValueEventListener(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        donorList.clear()

                        for (
                        dataSnapshot
                        in snapshot.children
                        ) {

                            val name =
                                dataSnapshot
                                    .child("name")
                                    .getValue(
                                        String::class.java
                                    )

                            val blood =
                                dataSnapshot
                                    .child("blood")
                                    .getValue(
                                        String::class.java
                                    )

                            val phone =
                                dataSnapshot
                                    .child("phone")
                                    .getValue(
                                        String::class.java
                                    )

                            if (
                                blood != null &&
                                blood.lowercase()
                                    .contains(
                                        searchText
                                            .lowercase()
                                    )
                            ) {

                                val donorMap =
                                    HashMap<String, String>()

                                if (name != null) {

                                    donorMap["name"] =
                                        name
                                }

                                donorMap["blood"] =
                                    blood

                                if (phone != null) {

                                    donorMap["phone"] =
                                        phone
                                }

                                donorMap["distance"] =
                                    (1..10).random()
                                        .toString() +
                                            " km away"

                                donorList.add(
                                    donorMap
                                )
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                    }
                })
        }
    }

    private fun getCurrentLocation() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                ),
                101
            )

            return
        }

        val locationRequest =
            LocationRequest.Builder(
                Priority
                    .PRIORITY_HIGH_ACCURACY,
                1000
            ).build()

        fusedLocationClient
            .requestLocationUpdates(

                locationRequest,

                object : LocationCallback() {

                    override fun onLocationResult(
                        result: LocationResult
                    ) {

                        val location:
                                Location =
                            result.lastLocation
                                ?: return

                        val geocoder =
                            Geocoder(
                                this@DonorListActivity,
                                Locale.getDefault()
                            )

                        val addresses =
                            geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )

                        if (
                            !addresses.isNullOrEmpty()
                        ) {

                            val city =
                                addresses[0]
                                    .locality

                            val state =
                                addresses[0]
                                    .adminArea

                            locationText.text =
                                "$city, $state"
                        }

                        fusedLocationClient
                            .removeLocationUpdates(
                                this
                            )
                    }
                },

                Looper.getMainLooper()
            )
    }
}