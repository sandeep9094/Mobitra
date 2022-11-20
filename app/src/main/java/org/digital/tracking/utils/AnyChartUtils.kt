package org.digital.tracking.utils

import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import org.digital.tracking.view.fragments.HomeFragment
import java.util.ArrayList
import java.util.HashMap

object AnyChartUtils {

    fun prepareVehiclePieChartList() {

    }


//    private fun preparePieChart() {
//        binding.pieChart.invalidate()
//        binding.pieChart.setUsePercentValues(false)
//        binding.pieChart.description?.isEnabled = false
////                binding.pieChart.setExtraOffsets(0f, 8f, 0f, 8f)
//        binding.pieChart.dragDecelerationFrictionCoef = 0.95f
//
//        binding.pieChart.isDrawHoleEnabled = true
//        binding.pieChart.setHoleColor(Color.WHITE)
//        binding.pieChart.setTransparentCircleColor(Color.WHITE)
//        binding.pieChart.setTransparentCircleAlpha(225)
//
//        binding.pieChart.setDrawCenterText(false)
//
//        binding.pieChart.rotationAngle = 0f
//        binding.pieChart.isRotationEnabled = true
//        binding.pieChart.isHighlightPerTapEnabled = true
//        setPieChartData(VehicleRepository.vehicleTypeMapWithFrequency)
//
//        activity?.runOnUiThread {
//            binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
//            binding.pieChart.spin(2000, 0F, 360F, Easing.EaseInOutQuad)
//        }
//
//        // binding.pieChart?.spin(2000, 0, 360);
//        val legend = binding.pieChart.legend
//        legend?.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
//        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//        legend?.orientation = Legend.LegendOrientation.HORIZONTAL
//        legend?.setDrawInside(false)
//        legend?.xEntrySpace = 16f
//        legend?.yEntrySpace = 0f
//        legend?.yOffset = 0f
//        legend?.isEnabled = true
//        legend?.textColor = Color.BLACK
//
//
//        activity?.runOnUiThread {
//            // entry label styling
//            binding.pieChart.setEntryLabelColor(Color.WHITE)
//            binding.pieChart.setEntryLabelTextSize(12f)
//        }
//    }
//
//    private fun setPieChartData(data: Map<String, Int>) {
//        val entries = ArrayList<PieEntry>()
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        val dataKeys: ArrayList<String> = ArrayList(data.keys)
//        try {
//            for (i in 0 until data.size) {
//                val analyticName = dataKeys[i]
//                val hits = data[analyticName]
//                entries.add(PieEntry(hits?.toFloat() ?: 0f, analyticName))
//            }
//        } catch (exception: Exception) {
//            Log.w("FragmentHome", exception.toString())
//        }
//
//        val dataSet = PieDataSet(entries, "")
//        dataSet.setDrawIcons(false)
//        dataSet.sliceSpace = 3f
//        dataSet.iconsOffset = MPPointF(0f, 20f)
//        dataSet.selectionShift = 5f
//
//        // add a lot of colors
//        val colors = ArrayList<Int>()
////        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//        colors.add(ColorTemplate.getHoloBlue())
//        dataSet.colors = colors
//        val pieData = PieData(dataSet)
//        pieData.setValueFormatter(PercentFormatter())
//        pieData.setValueTextSize(12f)
//        pieData.setValueTextColor(Color.WHITE)
//        binding.pieChart.data = pieData
//
//        // undo all highlights
//        binding.pieChart.highlightValues(null)
//        binding.pieChart.invalidate()
//    }
//
//
//    private fun prepareBarChart() {
//        val distanceLabels = ArrayList(VehicleRepository.vehicleDistanceMap.keys)
//        setDefaultBarChartProperties(binding.barChart, distanceLabels)
//        setBarChartData(binding.barChart, VehicleRepository.vehicleDistanceMap)
//    }
//
//    private fun setDefaultBarChartProperties(chart: BarChart, monthsLabels: ArrayList<String>) {
//        chart.description.isEnabled = false
//        // if more than 60 entries are displayed in the chart, no values will be drawn
//        chart.setMaxVisibleValueCount(60)
//        // scaling can now only be done on x- and y-axis separately
//        chart.setPinchZoom(false)
//        chart.setDrawBarShadow(false)
//        chart.setDrawGridBackground(false)
//        val xAxis = chart.xAxis
//        val xAxisMonthLabels: ArrayList<String> = ArrayList<String>()
//        xAxisMonthLabels.addAll(monthsLabels)
//        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisMonthLabels)
//        chart.axisLeft.axisMinimum = 0f
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//        xAxis.setCenterAxisLabels(false)
//        xAxis.isGranularityEnabled = true
//        chart.axisLeft.setDrawGridLines(false)
//        // add a nice and smooth animation
//        chart.animateY(1500)
//        chart.legend.isEnabled = false
//    }
//
//    private fun setBarChartData(chart: BarChart, distanceMap: HashMap<String, Float>) {
//        val xAxisData: ArrayList<String> = ArrayList<String>(distanceMap.keys)
//        val values = ArrayList<BarEntry>()
//        Log.d(HomeFragment.TAG, "Distance Data Key Set : " + distanceMap.keys)
//        for (i in xAxisData.indices) {
//            val monthKey = xAxisData[i]
//            val monthOrderValue: Float = distanceMap[monthKey] ?: 0f
//            values.add(BarEntry(i.toFloat(), monthOrderValue))
//        }
//        val barDataSet: BarDataSet
//        if (chart.data != null && chart.data.dataSetCount > 0) {
//            barDataSet = chart.data.getDataSetByIndex(0) as BarDataSet
//            barDataSet.values = values
//            chart.data.notifyDataChanged()
//            chart.notifyDataSetChanged()
//        } else {
//            barDataSet = BarDataSet(values, "Data Set")
//            barDataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
//            barDataSet.setDrawValues(false)
//            val dataSets = ArrayList<IBarDataSet>()
//            dataSets.add(barDataSet)
//            val data = BarData(dataSets)
//            chart.data = data
//            chart.setFitBars(true)
//        }
//        chart.invalidate()
//    }
//
//    private fun prepareLineChart() {
//        setDefaultLineChartProperties(binding.lineChart)
//        setData(binding.lineChart)
//    }
//
//    private fun setDefaultLineChartProperties(chart: LineChart) {
//        // background color
//        chart.setBackgroundColor(Color.WHITE)
//
//        // disable description text
//        chart.description.isEnabled = false
//
//        // enable touch gestures
//        chart.setTouchEnabled(true)
//
//        // set listeners
////        chart.setOnChartValueSelectedListener(this)
////        chart.setDrawGridBackground(true)
//
//        // create marker to display box when values are selected
////        val mv = MyMarkerView(this, R.layout.custom_marker_view)
//
//        // Set the marker to the chart
////        mv.setChartView(chart)
////        chart.marker = mv
//
//        // enable scaling and dragging
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//        // chart.setScaleXEnabled(true);
//        // chart.setScaleYEnabled(true);
//
//        // force pinch zoom along both axis
//        chart.setPinchZoom(true)
//
//    }
//
//    private fun setData(chart: LineChart) {
//        val values = ArrayList<Entry>()
//        values.add(Entry(1f, 529f))
//        values.add(Entry(2f, 568f))
//        values.add(Entry(3f, 288f))
//        values.add(Entry(4f, 502f))
//        values.add(Entry(5f, 300f))
//        values.add(Entry(6f, 404f))
//
//        val set1: LineDataSet
//        if (chart.data != null && chart.data.dataSetCount > 0) {
//            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
//            set1.values = values
//            set1.notifyDataSetChanged()
//            chart.data.notifyDataChanged()
//            chart.notifyDataSetChanged()
//        } else {
//            set1 = LineDataSet(values, "KM")
//            set1.setDrawIcons(false)
//            set1.color = Color.DKGRAY
////            set1.setCircleColor(Color.DKGRAY)
////            set1.lineWidth = 1f
//            set1.circleRadius = 0f
//            set1.setDrawCircleHole(false)
//            set1.valueTextSize = 9f
//            set1.setDrawFilled(false)
//            set1.formLineWidth = 1f
//            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
//            set1.formSize = 15f
//            set1.fillColor = Color.WHITE
////            if (Utils.getSDKInt() >= 18) {
////                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
////                set1.fillDrawable = drawable
////            } else {
////                set1.fillColor = Color.DKGRAY
////            }
//            val dataSets: ArrayList<ILineDataSet> = ArrayList()
//            dataSets.add(set1)
//            val data = LineData(dataSets)
//            chart.data = data
//        }
//    }

}