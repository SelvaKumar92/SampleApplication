package com.tvs.sample.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.clans.fab.FloatingActionButton
import com.google.gson.Gson
import com.tvs.sample.R
import com.tvs.sample.activity.SALoginActivity
import com.tvs.sample.adapter.SAUserListAdapter
import com.tvs.sample.entities.UserData
import com.tvs.sample.fragments.SAUserListFragment.OnListFragmentInteractionListener
import com.tvs.sample.utilis.SAFragmentManager
import com.tvs.sample.utilis.SASession

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class SAUserListFragment : Fragment(), View.OnClickListener {
    // TODO: Customize parameters


    private var mAdapter: SAUserListAdapter? = null
    private var myFragmentManager: SAFragmentManager? = null
    private var mySession: SASession? = null
    private var myBarFAB: FloatingActionButton? = null
    private var myBubbleFAB: FloatingActionButton? = null
    private var myLogoutFAB: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val aView = inflater.inflate(R.layout.fragment_userlist_list, container, false)

        intalize(aView)

        setUpRecyclerView(aView)

        setUpSearch(aView)

        clickListener()

        return aView
    }

    private fun clickListener() {
        myBarFAB!!.setOnClickListener(this)
        myBubbleFAB!!.setOnClickListener(this)
        myLogoutFAB!!.setOnClickListener(this)
    }

    private fun intalize(aView: View) {

        mySession = SASession(activity!!)
        myFragmentManager = SAFragmentManager(activity!!)
        myBarFAB = aView.findViewById(R.id.bar_chart_fb)
        myBubbleFAB = aView.findViewById(R.id.bubble_chart_fb)
        myLogoutFAB = aView.findViewById(R.id.logout_fb)

    }

    private fun setUpRecyclerView(aView: View) {

        val recyclerView = aView.findViewById<View>(R.id.list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        // Set the adapter
        mAdapter = SAUserListAdapter(mySession!!.getListData(), object : OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: UserData) {
                val aBundle = Bundle()
                aBundle.putString("data", Gson().toJson(item))
                myFragmentManager!!.updateContent(SADetailFragment(), "SADetailFragment", aBundle)
            }

        })
        recyclerView.adapter = mAdapter
    }

    private fun setUpSearch(aView: View) {

        // Associate searchable configuration with the SearchView
        val searchView = aView.findViewById<View>(R.id.search_view) as SearchView

        // listening to search query text change
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter!!.filter.filter(query)
                return false
            }
        })
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bar_chart_fb -> {
                val aBundle = Bundle()
                aBundle.putString("type", "1")
                myFragmentManager!!.updateContent(SAGraphFragment(), "SAGraphFragment", aBundle)
            }
            R.id.bubble_chart_fb -> {
                val aPieBundle = Bundle()
                aPieBundle.putString("type", "2")
                myFragmentManager!!.updateContent(SAGraphFragment(), "SAGraphFragment", aPieBundle)
            }
            R.id.logout_fb -> showLogoutAlert()
        }
    }

    private fun showLogoutAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("TVS")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("OK") { dialog, which ->
                mySession?.clear()
                startActivity(Intent(activity, SALoginActivity::class.java))
                activity!!.finish()
            }
            .setNegativeButton("CANCEL", null).show()
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: UserData)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"
    }
}
