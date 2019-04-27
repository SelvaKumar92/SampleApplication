package com.tvs.sample.utilis

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.tvs.sample.R
import kotlinx.android.synthetic.main.custom_dialog_box.*

class SAProgressDialog(private val myContext: Context) : Dialog(myContext) {

    init {
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.setContentView(R.layout.custom_dialog_box)
            this.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            custom_dialog_box_PB_loading!!.animate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Loading text
     *
     * @param aLoadingText
     */
    fun setMessage(aLoadingText: String) {
        custom_dialog_box_TXT_loading!!.text = aLoadingText
    }

    override fun show() {
        super.show()
    }

}
