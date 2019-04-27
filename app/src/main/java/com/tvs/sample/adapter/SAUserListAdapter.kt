package com.tvs.sample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.tvs.sample.R
import com.tvs.sample.entities.UserData
import com.tvs.sample.fragments.SAUserListFragment.OnListFragmentInteractionListener
import java.util.*

/**
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class SAUserListAdapter(private val myList: List<UserData>, private val mListener: OnListFragmentInteractionListener?) :
    RecyclerView.Adapter<SAUserListAdapter.ViewHolder>(), Filterable {
    private var myListFiltered: List<UserData>? = null

    init {
        myListFiltered = myList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_userlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = this!!.myListFiltered?.get(position)
        holder.mContentView.text = myListFiltered!![position].name

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    override fun getItemCount(): Int {
        return myListFiltered!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    myListFiltered = myList
                } else {
                    val filteredList = ArrayList<UserData>()
                    for (aUserData in myList) {
                        // here we are looking for name
                        if (aUserData.name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(aUserData)
                        }
                    }
                    myListFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = myListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                myListFiltered = filterResults.values as ArrayList<UserData>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView
        var mItem: UserData? = null

        init {
            mContentView = mView.findViewById<View>(R.id.name_txt) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
