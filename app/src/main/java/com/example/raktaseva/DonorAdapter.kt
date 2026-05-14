package com.example.raktaseva

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class DonorAdapter(
    context: Context,
    private val donorList: ArrayList<HashMap<String, String>>
) : ArrayAdapter<HashMap<String, String>>(
    context,
    0,
    donorList
) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = if (convertView == null) {

            LayoutInflater.from(context).inflate(
                R.layout.donor_card,
                parent,
                false
            )

        } else {

            convertView
        }

        val donor = donorList[position]

        val nameText =
            view.findViewById<TextView>(R.id.nameText)

        val bloodText =
            view.findViewById<TextView>(R.id.bloodText)

        val phoneText =
            view.findViewById<TextView>(R.id.phoneText)

        val distanceText =
            view.findViewById<TextView>(R.id.distanceText)

        val bloodBadge =
            view.findViewById<TextView>(R.id.bloodBadge)

        val callBtn =
            view.findViewById<Button>(R.id.callBtn)

        val name = donor["name"] ?: ""
        val blood = donor["blood"] ?: ""
        val phone = donor["phone"] ?: ""
        val distance =
            donor["distance"] ?: ""

        nameText.text = name
        bloodText.text = "Blood Group: $blood"
        phoneText.text = "Phone: $phone"
        distanceText.text = distance
        bloodBadge.text = blood

        callBtn.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:$phone")
            )

            context.startActivity(intent)
        }

        return view
    }
}