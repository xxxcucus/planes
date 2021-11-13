package com.planes_multiplayer.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

class CustomVideosAdapter(private var c: Context, private var quotes: Array<String>) : BaseAdapter()  {
    override fun getCount(): Int {
        return quotes.size
    }
    override fun getItem(i: Int): Any {  return quotes[i] }

    override fun getItemId(i: Int): Long { return i.toLong()}

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        if (view == null) {
            //inflate layout resource if its null
            view = LayoutInflater.from(c).inflate(R.layout.spinner_item, viewGroup, false)
        }

        //reference textviews and imageviews from our layout
        val nameTxt = view?.findViewById<TextView>(R.id.track_name) as TextView

        //BIND data to TextView and ImageVoew
        nameTxt.text = quotes[i]

        //handle itemclicks for the ListView
        //view?.setOnClickListener { Toast.makeText(c, "Track title clicked!", Toast.LENGTH_SHORT).show() }

        return view!!
    }
}