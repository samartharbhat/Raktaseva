package com.example.raktaseva

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
class MapsActivity :
    FragmentActivity(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        val mapFragment =
            supportFragmentManager
                .findFragmentById(R.id.map)
                    as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(
        googleMap: GoogleMap
    ) {

        mMap = googleMap

        val manipal =
            LatLng(13.3520, 74.7928)

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                manipal,
                12f
            )
        )

        addHospitals()

        loadDonors()


    }

    private fun loadDonors() {

        val database =
            FirebaseDatabase.getInstance()

        val reference =
            database.getReference("Donors")

        reference.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {


                    var latitude = 13.3520
                    var longitude = 74.7928

                    for (dataSnapshot in snapshot.children) {

                        val name =
                            dataSnapshot.child("name")
                                .getValue(String::class.java)

                        val blood =
                            dataSnapshot.child("blood")
                                .getValue(String::class.java)

                        val donorLocation =
                            LatLng(latitude, longitude)

                        mMap.addMarker(
                            MarkerOptions()
                                .position(donorLocation)
                                .title(name)
                                .snippet("Blood Group: $blood")
                        )

                        latitude += 0.01
                        longitude += 0.01
                    }
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {

                }
            })
    }
    private fun addHospitals() {

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(13.3400, 74.7850)
                )
                .title("KMC Hospital")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE
                        )
                )
        )

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(13.3650, 74.8100)
                )
                .title("Manipal Clinic")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                )
        )

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(13.3250, 74.7700)
                )
                .title("Blood Bank")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_VIOLET
                        )
                )
        )
    }
}