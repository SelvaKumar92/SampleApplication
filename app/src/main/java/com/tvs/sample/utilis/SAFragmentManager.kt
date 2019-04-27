package com.tvs.sample.utilis

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.tvs.sample.R


/**
 * Created by selvak
 */

class SAFragmentManager {

    var myLastTag = ""
    private var myContext: FragmentActivity? = null

    val backstackCount: Int
        get() {
            val aFragmentManager = myContext!!.supportFragmentManager
            return aFragmentManager.backStackEntryCount
        }

    /**
     * Constructor to Initiate fragment manager
     *
     * @param aContext
     */
    constructor(aContext: FragmentActivity) {
        myContext = aContext

    }

    constructor(aContext: AppCompatActivity) {
        myContext = aContext

    }


    /**
     * Update the Current Fragment by passing the below parameters
     *
     * @param aFragment
     * @param tag
     * @param aBundle
     */
    fun updateContent(aFragment: Fragment, tag: String, aBundle: Bundle?) {
        try {

            Log.e("TAG Screen name", tag)

            // Initialise Fragment Manager
            val aFragmentManager = myContext!!.supportFragmentManager

            // Initialise Fragment Transaction
            val aTransaction = aFragmentManager.beginTransaction()

            if (aBundle != null) {
                aFragment.arguments = aBundle
            }

            // Add the selected fragment
            aTransaction.add(R.id.main_content_frame, aFragment, tag)

            // add the tag to the backstack
            aTransaction.addToBackStack(tag)

            // Commit the Fragment transaction
            aTransaction.commit()

            myLastTag = tag

            Log.i("LastTag", myLastTag)
        } catch (aError: Exception) {
            aError.printStackTrace()
        }

    }
}