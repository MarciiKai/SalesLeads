@file:Suppress("DEPRECATION")

package com.example.salesleads.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.salesleads.R
import lecho.lib.hellocharts.animation.ChartAnimationListener
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.ValueShape
import lecho.lib.hellocharts.model.Viewport
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView

class AnalysisFragment : Fragment() {
    private var chart: LineChartView? = null
    private var data: LineChartData? = null
    private var numberOfLines = 1
    private val maxNumberOfLines = 4
    private val numberOfPoints = 12
    private var randomNumbersTab = Array(maxNumberOfLines) {
        FloatArray(
            numberOfPoints
        )
    }
    private var hasAxes = true
    private var hasAxesNames = true
    private var hasLines = true
    private var hasPoints = true
    private var shape = ValueShape.CIRCLE
    private var isFilled = false
    private var hasLabels = false
    private var isCubic = false
    private var hasLabelForSelected = false
    private var pointsHaveDifferentColor = false
    private var hasGradientToTransparent = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_analysis, container, false)
        chart = rootView.findViewById<View>(R.id.linechart) as LineChartView
        chart!!.onValueTouchListener = ValueTouchListener()

        // Generate some random values.
        generateValues()
        generateData()

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart!!.isViewportCalculationEnabled = false
        resetViewport()

        setHasOptionsMenu(true)

        return rootView
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "inflater.inflate(R.menu.line_chart, menu)",
        "com.example.salesleads.R"
    )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_chart, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_reset) {
            reset()
            generateData()
            return true
        }
        if (id == R.id.action_add_line) {
            addLineToData()
            return true
        }
        if (id == R.id.action_toggle_lines) {
            toggleLines()
            return true
        }
        if (id == R.id.action_toggle_points) {
            togglePoints()
            return true
        }
        if (id == R.id.action_toggle_gradient) {
            toggleGradient()
            return true
        }
        if (id == R.id.action_toggle_cubic) {
            toggleCubic()
            return true
        }
        if (id == R.id.action_toggle_area) {
            toggleFilled()
            return true
        }
        if (id == R.id.action_point_color) {
            togglePointColor()
            return true
        }
        if (id == R.id.action_shape_circles) {
            setCircles()
            return true
        }
        if (id == R.id.action_shape_square) {
            setSquares()
            return true
        }
        if (id == R.id.action_shape_diamond) {
            setDiamonds()
            return true
        }
        if (id == R.id.action_toggle_labels) {
            toggleLabels()
            return true
        }
        if (id == R.id.action_toggle_axes) {
            toggleAxes()
            return true
        }
        if (id == R.id.action_toggle_axes_names) {
            toggleAxesNames()
            return true
        }
        if (id == R.id.action_animate) {
            prepareDataAnimation()
            chart!!.startDataAnimation()
            return true
        }
        if (id == R.id.action_toggle_selection_mode) {
            toggleLabelForSelected()
            Toast.makeText(
                activity,
                "Selection mode set to " + chart!!.isValueSelectionEnabled + " select any point.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (id == R.id.action_toggle_touch_zoom) {
            chart!!.isZoomEnabled = !chart!!.isZoomEnabled
            Toast.makeText(
                activity,
                "IsZoomEnabled " + chart!!.isZoomEnabled,
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (id == R.id.action_zoom_both) {
            chart!!.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
            return true
        }
        if (id == R.id.action_zoom_horizontal) {
            chart!!.zoomType = ZoomType.HORIZONTAL
            return true
        }
        if (id == R.id.action_zoom_vertical) {
            chart!!.zoomType = ZoomType.VERTICAL
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateValues() {
        for (i in 0 until maxNumberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = Math.random().toFloat() * 100f
            }
        }
    }

    private fun reset() {
        numberOfLines = 1
        hasAxes = true
        hasAxesNames = true
        hasLines = true
        hasPoints = true
        shape = ValueShape.CIRCLE
        isFilled = false
        hasLabels = false
        isCubic = false
        hasLabelForSelected = false
        pointsHaveDifferentColor = false
        chart!!.isValueSelectionEnabled = hasLabelForSelected
        resetViewport()
    }

    private fun resetViewport() {
        // Reset viewport height range to (0,100)
        val v = Viewport(chart!!.maximumViewport)
        v.bottom = 0f
        v.top = 100f
        v.left = 0f
        v.right = (numberOfPoints - 1).toFloat()
        chart!!.maximumViewport = v
        chart!!.currentViewport = v
    }

    private fun generateData() {
        val lines: MutableList<Line> = ArrayList()
        for (i in 0 until numberOfLines) {
            val values: MutableList<PointValue> = ArrayList()
            for (j in 0 until numberOfPoints) {
                values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
            }
            val line = Line(values)
            line.color = ChartUtils.COLORS[i]
            line.shape = shape
            line.isCubic = isCubic
            line.isFilled = isFilled
            line.setHasLabels(hasLabels)
            line.setHasLabelsOnlyForSelected(hasLabelForSelected)
            line.setHasLines(hasLines)
            line.setHasPoints(hasPoints)
            if (pointsHaveDifferentColor) {
                line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
            }
            lines.add(line)
        }
        data = LineChartData(lines)
        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Axis X"
                axisY.name = "Axis Y"
            }
            data!!.axisXBottom = axisX
            data!!.axisYLeft = axisY
        } else {
            data!!.axisXBottom = null
            data!!.axisYLeft = null
        }
        data!!.baseValue = Float.NEGATIVE_INFINITY
        chart!!.lineChartData = data
    }

    private fun addLineToData() {
        if (data!!.lines.size >= maxNumberOfLines) {
            Toast.makeText(activity, "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show()
            return
        } else {
            ++numberOfLines
        }
        generateData()
    }

    private fun toggleLines() {
        hasLines = !hasLines
        generateData()
    }

    private fun togglePoints() {
        hasPoints = !hasPoints
        generateData()
    }

    private fun toggleGradient() {
        hasGradientToTransparent = !hasGradientToTransparent
        generateData()
    }

    private fun toggleCubic() {
        isCubic = !isCubic
        generateData()
        if (isCubic) {
            val v = Viewport(
                chart!!.maximumViewport
            )
            v.bottom = -5f
            v.top = 105f
            chart!!.maximumViewport = v
            chart!!.setCurrentViewportWithAnimation(v)
        } else {
            val v = Viewport(
                chart!!.maximumViewport
            )
            v.bottom = 0f
            v.top = 100f

            chart!!.setViewportAnimationListener(object : ChartAnimationListener {
                override fun onAnimationStarted() {}

                override fun onAnimationFinished() {
                    chart!!.maximumViewport = v
                    chart!!.setViewportAnimationListener(null)
                }
            })

            chart!!.setCurrentViewportWithAnimation(v)
        }
    }

    private fun toggleFilled() {
        isFilled = !isFilled
        generateData()
    }

    private fun togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor
        generateData()
    }

    private fun setCircles() {
        shape = ValueShape.CIRCLE
        generateData()
    }

    private fun setSquares() {
        shape = ValueShape.SQUARE
        generateData()
    }

    private fun setDiamonds() {
        shape = ValueShape.DIAMOND
        generateData()
    }

    private fun toggleLabels() {
        hasLabels = !hasLabels
        if (hasLabels) {
            hasLabelForSelected = false
            chart!!.isValueSelectionEnabled = hasLabelForSelected
        }
        generateData()
    }

    private fun toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected
        chart!!.isValueSelectionEnabled = hasLabelForSelected
        if (hasLabelForSelected) {
            hasLabels = false
        }
        generateData()
    }

    private fun toggleAxes() {
        hasAxes = !hasAxes
        generateData()
    }

    private fun toggleAxesNames() {
        hasAxesNames = !hasAxesNames
        generateData()
    }

    private fun prepareDataAnimation() {
        for (line in data!!.lines) {
            for (value in line.values) {
                value.setTarget(value.x, Math.random().toFloat() * 100)
            }
        }
    }

    private inner class ValueTouchListener : LineChartOnValueSelectListener {
        override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {
            Toast.makeText(activity, "Selected: $value", Toast.LENGTH_SHORT).show()
        }

        override fun onValueDeselected() {}
    }
}
