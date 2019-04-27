package com.tvs.sample.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.tvs.sample.R
import com.tvs.sample.utilis.SASession

/**
 *
 * Selvak
 */
class SASplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private var mySession: SASession? = null

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            var intent: Intent? = null
            if (!mySession?.loginStatus!!) {
                intent = Intent(applicationContext, SALoginActivity::class.java)
            } else {
                intent = Intent(applicationContext, SAHomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mySession = SASession(this)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}