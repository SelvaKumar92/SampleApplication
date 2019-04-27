package com.tvs.sample.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.tvs.sample.R
import com.tvs.sample.fragments.SAUserListFragment
import com.tvs.sample.utilis.SAFragmentManager

class SAHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        SAFragmentManager(this).updateContent(SAUserListFragment()!!, "SAUserListFragment", null)
    }

    override fun onBackPressed() {
        if (SAFragmentManager(this)!!.backstackCount == 1) {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                    finishAffinity()
                })
                .setNegativeButton(android.R.string.no, null)
                .show()
        } else {
            super.onBackPressed()
        }
    }

}
