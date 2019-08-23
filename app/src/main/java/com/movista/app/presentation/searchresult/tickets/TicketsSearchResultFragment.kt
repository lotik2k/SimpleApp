package com.movista.app.presentation.searchresult.tickets

import android.support.v7.widget.LinearLayoutManager
import com.movista.app.R
import com.movista.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tickets_search_result.*

class TicketsSearchResultFragment : BaseFragment() {

    companion object {

        fun newInstance(): TicketsSearchResultFragment {
            return TicketsSearchResultFragment()
        }
    }

    override fun getLayoutRes() = R.layout.fragment_tickets_search_result

    override fun onFragmentInject() {}

    override fun initUI() {
        super.initUI()

        val adapter = TripsAdapter()
        trips_list.layoutManager = LinearLayoutManager(activity)
        trips_list.adapter = adapter

    }
}
