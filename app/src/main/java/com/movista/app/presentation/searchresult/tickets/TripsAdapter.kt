package com.movista.app.presentation.searchresult.tickets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movista.app.R

class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TripsAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_trip, parent, false)
        )
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}