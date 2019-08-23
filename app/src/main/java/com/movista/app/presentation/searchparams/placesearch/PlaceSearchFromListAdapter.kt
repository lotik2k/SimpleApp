package com.movista.app.presentation.searchparams.placesearch

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movista.app.R
import com.movista.app.presentation.viewmodel.PlaceViewModel
import com.movista.app.utils.EMPTY
import com.movista.app.utils.setGone
import kotlinx.android.synthetic.main.place_search_header_view_hint.view.*
import kotlinx.android.synthetic.main.place_search_list_item.view.*

class PlaceSearchFromListAdapter(
        private var listener: OnRecentPlacesItemListener?,
        private val placeSearchListType: PlaceSearchFromListType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HEADER_VIEW = 0
    }

    private val items = arrayListOf<PlaceViewModel>()

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_VIEW
            else -> position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            HEADER_VIEW -> {
                HeaderViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.place_search_header_view_hint, parent, false)
                )
            }
            else -> {
                ContentViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.place_search_list_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ContentViewHolder -> {

                val placeViewModel = items[position - 1]

                holder.itemView.item_city_name.text = placeViewModel.cityName
                holder.itemView.item_state_name.text =
                        if (placeViewModel.stateName != String.EMPTY) {
                            "${placeViewModel.stateName}, ${placeViewModel.countryName}"
                        } else {
                            placeViewModel.countryName
                        }

                if (position == items.size) {
                    holder.itemView.place_search_item_separator.setGone()
                }
            }
            is HeaderViewHolder -> {

                if (items.size < 1) holder.itemView.place_search_header_title.setGone()

                holder.itemView.place_search_header_title.text =
                        when (placeSearchListType) {
                            PlaceSearchFromListType.POPULAR ->
                                holder.itemView.context.getString(R.string.place_search_recent_header)
                            PlaceSearchFromListType.RECENT ->
                                holder.itemView.context.getString(R.string.place_search_popular_header)
                        }
            }
        }
    }

    //increasing getItemcount to 1. This will be the row of header.
    override fun getItemCount(): Int {
        return items.size + 1
    }

    fun setItems(placeViewModel: List<PlaceViewModel>) {
        items.clear()
        items.addAll(placeViewModel)
    }


    fun removeListener() {
        listener = null
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                it.parent_layout.setBackgroundColor(
                        ContextCompat.getColor(it.context, R.color.place_search_button_clicked)
                )
                listener?.onRecentItemClicked(items[adapterPosition - 1])
            }
        }
    }

    interface OnRecentPlacesItemListener {
        fun onRecentItemClicked(placeViewModel: PlaceViewModel)
    }
}

enum class PlaceSearchFromListType {
    POPULAR, RECENT
}

