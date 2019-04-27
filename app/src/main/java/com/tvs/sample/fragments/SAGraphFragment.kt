package com.tvs.sample.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.tvs.sample.R
import com.tvs.sample.utilis.SASession
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SAGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SAGraphFragment : Fragment() {
    private var mySession: SASession? = null
    private var myBubbleChart: BubbleChart? = null
    private var myBarChart: BarChart? = null
    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySession = SASession(activity!!)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val aView = inflater.inflate(R.layout.fragment_graph, container, false)

        myBubbleChart = aView.findViewById<View>(R.id.bubble_chart) as BubbleChart

        myBarChart = aView.findViewById<View>(R.id.bar_chart) as BarChart

        val aType = arguments!!.getString("type")

        if (aType == "1") {
            setupBarChart(aView)
        } else {
            setUpBubble(aView)
        }
        return aView
    }


    fun setupBarChart(aView: View) {

        myBubbleChart!!.visibility = View.GONE

        myBarChart!!.visibility = View.VISIBLE

        val aData = ArrayList<BarEntry>()
        val aYnames = ArrayList<String>()

        val yvalues = ArrayList<Entry>()
        val xVals = ArrayList<String>()
        val aUSerData = mySession!!.getListData()

        for (i in 0..19) {
            val aString = aUSerData[i].salary!!.replace("$", "")
            val aReplace = aString.replace(",".toRegex(), "")
            aData.add(BarEntry(Integer.parseInt(aReplace).toFloat(), i))
            yvalues.add(Entry(Integer.parseInt(aReplace).toFloat(), i))
            aYnames.add(aUSerData[i].name!!)
            xVals.add(aUSerData[i].name!!)
        }
        val xAxis = myBarChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        myBarChart!!.axisRight.isEnabled = false
        xAxis.spaceBetweenLabels = 0
        val bardataset = BarDataSet(aData, "Salary")
        xAxis.setLabelsToSkip(0)
        xAxis.textSize = 2f

        val data = BarData(aYnames, bardataset)
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)

        myBarChart!!.data = data

    }

    fun setUpBubble(aView: View) {

        myBubbleChart!!.visibility = View.VISIBLE

        myBarChart!!.visibility = View.GONE

        val aData = ArrayList<BubbleEntry>()

        val aYnames = ArrayList<String>()

        val aYvalues = ArrayList<Entry>()
        val xVals = ArrayList<String>()

        val aUSerData = mySession!!.getListData()

        for (i in 0..19) {
            val aString = aUSerData[i].salary?.replace("$", "")
            val aReplace = aString?.replace(",".toRegex(), "")
            aData.add(BubbleEntry(i, Integer.parseInt(aReplace).toFloat(), Integer.parseInt(aReplace).toFloat()))
            aYvalues.add(Entry(Integer.parseInt(aReplace).toFloat(), i))
            aYnames.add(aUSerData[i].name!!)
            xVals.add(aUSerData[i].name!!)
        }
        val xAxis = myBubbleChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        myBubbleChart!!.axisRight.isEnabled = false
        xAxis.spaceBetweenLabels = 0
        val bardataset = BubbleDataSet(aData, "Salary")
        xAxis.setLabelsToSkip(0)
        xAxis.textSize = 2f

        val data = BubbleData(aYnames, bardataset)
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        myBubbleChart!!.data = data

    }

    companion object {

        private val ARG_PARAM1 = "type"

    }

}
