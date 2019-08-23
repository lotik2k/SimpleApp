package com.movista.app.presentation.searchparams.placesearch

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movista.app.R
import com.movista.app.presentation.viewmodel.PlaceViewModel
import com.movista.app.utils.EMPTY
import com.movista.app.utils.setGone
import kotlinx.android.synthetic.main.place_search_list_item.view.*

class PlaceSearchAdapter(
        var listener: OnPlaceSearchItemListener?
) : RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>() {

    private val items = arrayListOf<PlaceViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.place_search_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placeViewModel = items[position]

        holder.itemView.item_city_name.text = placeViewModel.cityName
        holder.itemView.item_state_name.text =
                if (placeViewModel.stateName != String.EMPTY) {
                    "${placeViewModel.stateName}, ${placeViewModel.countryName}"
                } else {
                    placeViewModel.countryName
                }

        if (position == items.size - 1) {
            holder.itemView.place_search_item_separator.setGone()
        }
    }

    fun setItems(items: List<PlaceViewModel>) {

        val diffResult = DiffUtil.calculateDiff(SearchDiffUtil(this.items, items))

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)
    }

    fun removeListener() {
        listener = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                it.parent_layout.setBackgroundColor(
                        ContextCompat.getColor(it.context, R.color.place_search_button_clicked)
                )
                listener?.onPlaceItemClicked(items[adapterPosition])
            }
        }
    }

    class SearchDiffUtil(
            private val oldList: List<PlaceViewModel>,
            private val newList: List<PlaceViewModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.size == newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].cityName == newList[newItemPosition].cityName
        }
    }

    interface OnPlaceSearchItemListener {
        fun onPlaceItemClicked(placeViewModel: PlaceViewModel)
    }

}