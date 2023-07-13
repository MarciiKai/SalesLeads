package com.example.salesleads.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.AnalysisAdapter
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue

class HomeCompanyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_company, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.analysisRecycleView)

        val data: List<Any> = lineChartData()

        val adapter = AnalysisAdapter(data)

        recyclerView.adapter = adapter

        return rootView
    }

    private fun lineChartData(): List<Any> {
        val dataList: MutableList<Any> = mutableListOf()

        // Generate data for the line chart
        val numberOfLines = 4
        val numberOfPoints = 12
        val randomNumbersTab = Array(numberOfLines) {
            FloatArray(numberOfPoints)
        }

        // Generate random values for the line chart data
        for (i in 0 until numberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = (Math.random() * 100f).toFloat()
            }
        }

        // Create LineChartData objects for each line
        for (i in 0 until numberOfLines) {
            val values: MutableList<PointValue> = ArrayList()
            for (j in 0 until numberOfPoints) {
                values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
            }
            val line = Line(values)
            val lines: MutableList<Line> = ArrayList()
            lines.add(line)
            val lineChartData = LineChartData(lines)
            dataList.add(lineChartData)
        }

        return dataList
    }

}
