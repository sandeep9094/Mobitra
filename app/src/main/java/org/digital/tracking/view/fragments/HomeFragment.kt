package org.digital.tracking.view.fragments

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.mobitra.tracking.VehiclesAverageRunningQuery
import com.mobitra.tracking.VehiclesWeeklyAverageRunningQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.FragmentHomeBinding
import org.digital.tracking.enum.AvgRunningKmTimeFrame
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.DeviceListItem
import org.digital.tracking.model.DumVehicleAvgRunningResponse
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.AddDeviceActivity
import org.digital.tracking.view.activity.MainActivity
import org.digital.tracking.viewModel.HomeViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(), View.OnClickListener {

    private var deviceListSize = 0
    private var selectedVehicleImeiForLineCart = ""
    private var avgRunningKmTimeFrame = AvgRunningKmTimeFrame.WEEK
    private var avgRunningKmsList: List<VehiclesAverageRunningQuery.VehiclesAverageRunning?> = ArrayList()

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeToolbar(binding.root)

        setupObservers()

        if (UserCacheManager.getDeviceList().isEmpty()) {
            viewModel.getDeviceList()
        } else {
            prepareDashboard()
        }

        avgRunningKmTimeFrame = AvgRunningKmTimeFrame.WEEK

        binding.weekButton.setOnClickListener(this)
        binding.monthButton.setOnClickListener(this)
        binding.yearButton.setOnClickListener(this)
        binding.addDeviceTextView.setOnClickListener(this)

        binding.vehicleLayout.runningVehicles.setOnClickListener(this)
        binding.vehicleLayout.stopVehicles.setOnClickListener(this)
        binding.vehicleLayout.idleVehicles.setOnClickListener(this)
        binding.vehicleLayout.noDataVehicles.setOnClickListener(this)
        binding.vehicleLayout.allVehicles.setOnClickListener(this)
    }

    private fun initSelectVehicleSpinner() {
        val vehicleList = ArrayList<String>()
        val vehicleMap = VehicleRepository.getVehicleNumberHashMap()
        val vehicleImeiList = ArrayList(vehicleMap.keys)
        vehicleMap.forEach {
            vehicleList.add(it.value)
        }
        context?.let {
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, vehicleList)
            binding.spinner.adapter = arrayAdapter
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Timber.d("onItemSelected at ${position} is ${vehicleList[position]}")
                    val selectedVehicleImei = vehicleImeiList[position]
                    selectedVehicleImeiForLineCart = selectedVehicleImei
                    viewModel.getVehicleWeeklyKms(selectedVehicleImeiForLineCart)
                    viewModel.fetchAverageKms(selectedVehicleImeiForLineCart)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.d("onNothing Selected")
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.deviceListResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.progress.makeVisible()
                    binding.error.makeGone()
                    binding.dashboardRootLayout.makeGone()
                }
                is ApiResult.Error -> {
                    binding.progress.makeGone()
                    binding.error.makeVisible()
                    binding.dashboardRootLayout.makeGone()
                }
                is ApiResult.Success -> {
                    val deviceList = it.data
                    if (deviceList.isEmpty()) {
                        return@observe
                    }
                    UserCacheManager.setDeviceList(deviceList)
                    deviceListSize = deviceList.size
                    viewModel.fetchAllVehicles()
                    preparePieChartData(deviceList)
                    binding.progress.makeGone()
                    binding.error.makeGone()
                    binding.dashboardRootLayout.makeVisible()
                }
            }
        }
        viewModel.vehicleList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Error -> {
                }
                is ApiResult.Success -> {
                    val list = it.data
                    if (list.isEmpty()) {
                        return@observe
                    }
                    val vehicleList = list.map { it?.vehicleNum }.toList()
                    if (vehicleList.isEmpty()) {
                        return@observe
                    }
                    VehicleRepository.setVehicleList(list)
                    initSelectVehicleSpinner()
                    initVehicleStatusCountOnUi()
                }
            }
        }
        viewModel.vehicleWeeklyKms.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Error -> {
                    binding.runningKmLayout.makeGone()
                }
                is ApiResult.Success -> {
                    val lineChartVehicleData = it.data.first()
                    prepareLineChartData(lineChartVehicleData)
                }
            }
        }

        viewModel.averageRunningKmResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Error -> {
                    binding.barChartLayout.makeGone()
                }
                is ApiResult.Success -> {
                    prepareChartData(it.data)
                }
            }
        }
    }

    private fun prepareDashboard() {
        val deviceList = UserCacheManager.getDeviceList()
        deviceListSize = deviceList.size
        preparePieChartData(deviceList)

        if (VehicleRepository.getVehicleList().isNotEmpty()) {
            initSelectVehicleSpinner()
            initVehicleStatusCountOnUi()
        } else {
            viewModel.fetchAllVehicles()
        }
    }

    private fun prepareChartData(newAvgRunningKmsList: List<VehiclesAverageRunningQuery.VehiclesAverageRunning?>) {
        avgRunningKmsList = newAvgRunningKmsList
        prepareBarChartData(avgRunningKmsList)
    }

    private fun prepareLineChartData(vehiclesAverageRunning: VehiclesWeeklyAverageRunningQuery.VehiclesWeeklyAverageRunning?) {
        vehiclesAverageRunning?.let {
            if (it.vehicleNum.isNullOrEmpty()) {
                //Do nothing
                binding.runningKmLayout.makeGone()
            } else {
                prepareLineChart(it.weeklyKms)
            }
        }
    }


    private fun prepareLineChart(data: List<Double?>?) {
        if (data.isNullOrEmpty()) {
            binding.runningKmLayout.makeGone()
            return
        }
        setDefaultLineChartProperties(binding.lineChart)
        setData(binding.lineChart, data)
    }

    private fun setDefaultLineChartProperties(chart: LineChart) {
        // background color
        chart.setBackgroundColor(Color.WHITE)

        // disable description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)
        chart.axisLeft.axisMinimum = 0f

        // set listeners
//        chart.setOnChartValueSelectedListener(this)
//        chart.setDrawGridBackground(true)

        // create marker to display box when values are selected
//        val mv = MyMarkerView(this, R.layout.custom_marker_view)

        // Set the marker to the chart
//        mv.setChartView(chart)
//        chart.marker = mv

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true)
    }

    private fun setData(chart: LineChart, valuesData: List<Double?>?) {
        val values = ArrayList<Entry>()
        valuesData?.forEachIndexed { index, value ->
            values.add(Entry(index.toFloat(), value?.toFloat() ?: 0F))
        }

        Timber.d("Line DateSet : ${values}")

        val set1: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "KM")
            set1.setDrawIcons(false)
            set1.color = Color.DKGRAY
//            set1.setCircleColor(Color.DKGRAY)
//            set1.lineWidth = 1f
            set1.circleRadius = 0f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(false)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.fillColor = Color.WHITE
//            if (Utils.getSDKInt() >= 18) {
//                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
//                set1.fillDrawable = drawable
//            } else {
//                set1.fillColor = Color.DKGRAY
//            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1)
            val data = LineData(dataSets)
            chart.data = data
        }
        chart.invalidate()
    }


    private fun prepareBarChartData(avgRunningKmsList: List<VehiclesAverageRunningQuery.VehiclesAverageRunning?>) {
        val avgRunningKms = avgRunningKmsList
        val dataMap = HashMap<String, Float>()
        avgRunningKms.forEach {
            val avgData = when (avgRunningKmTimeFrame) {
                AvgRunningKmTimeFrame.WEEK -> {
                    val weekList: ArrayList<Int> = ArrayList()
                    it?.weeklyKms?.forEach {
                        it?.let {
                            weekList.add(it.toInt())
//                            weekList.add(VehicleRepository.getDistance().random().toInt())
                        }
                    }
                    val weeklyAvg: Float = weekList.average().toFloat()
                    weeklyAvg
                }
                AvgRunningKmTimeFrame.MONTH -> {
                    val monthlyList: ArrayList<Int> = ArrayList()
                    it?.monthlyKms?.forEach {
                        it?.let {
                            monthlyList.add(it.toInt())
//                            monthlyList.add(VehicleRepository.getDistance().random().toInt())
                        }
                    }
                    val monthlyAvg: Float = monthlyList.average().toFloat()
                    monthlyAvg
                }
                AvgRunningKmTimeFrame.YEAR -> {
                    val yearlyList: ArrayList<Int> = ArrayList()
                    it?.yearlyKms?.forEach {
                        it?.let {
                            yearlyList.add(it.toInt())
//                            yearlyList.add(VehicleRepository.getDistance().random().toInt())
                        }
                    }
                    val yearlyAvg: Float = yearlyList.average().toFloat()
                    yearlyAvg
                }
            }
            val vehicleNum = VehicleRepository.getVehicleNumber(it?.IMEINumber ?: "")
            dataMap[vehicleNum] = avgData
        }
        if (dataMap.isEmpty()) {
            return
        }
        prepareBarChart(dataMap)
    }


    private fun prepareBarChart(dataMap: HashMap<String, Float>) {
        val distanceLabels = ArrayList(dataMap.keys)
        setDefaultBarChartProperties(binding.barChart, distanceLabels)
        setBarChartData(binding.barChart, dataMap)
    }

    private fun setDefaultBarChartProperties(chart: BarChart, monthsLabels: ArrayList<String>) {
        chart.description.isEnabled = false
        // if more than 60 entries are displayed in the chart, no values will be drawn
        chart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)
        val xAxis = chart.xAxis
        val xAxisMonthLabels: ArrayList<String> = ArrayList<String>()
        xAxisMonthLabels.addAll(monthsLabels)
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisMonthLabels)
        chart.axisLeft.axisMinimum = 0f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(false)
        xAxis.isGranularityEnabled = true
        chart.axisLeft.setDrawGridLines(false)
        // add a nice and smooth animation
        chart.animateY(1500)
        chart.legend.isEnabled = false
    }

    private fun setBarChartData(chart: BarChart, distanceMap: HashMap<String, Float>) {
        val xAxisData: ArrayList<String> = ArrayList<String>(distanceMap.keys)
        val values = ArrayList<BarEntry>()
        Log.d(TAG, "Distance Data Key Set : " + distanceMap.keys)
        for (i in xAxisData.indices) {
            val monthKey = xAxisData[i]
            val monthOrderValue: Float = distanceMap[monthKey] ?: 0f
            values.add(BarEntry(i.toFloat(), monthOrderValue))
        }
        val barDataSet: BarDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            barDataSet = chart.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            barDataSet = BarDataSet(values, "Data Set")
            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
            barDataSet.setDrawValues(false)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)
            val data = BarData(dataSets)
            chart.data = data
            chart.setFitBars(true)
        }
        chart.invalidate()
    }


    private fun preparePieChartData(deviceList: List<DeviceListItem>) {
        val deviceTypesMap = deviceList.groupingBy { it.vehicleType }.eachCount().filter { it.value > 0 }
        preparePieChart(deviceTypesMap)
    }


    private fun preparePieChart(dataMap: Map<String, Int>) {
        binding.pieChart.invalidate()
        binding.pieChart.setUsePercentValues(false)
        binding.pieChart.description?.isEnabled = false
//                binding.pieChart.setExtraOffsets(0f, 8f, 0f, 8f)
        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.holeRadius = 90f
        binding.pieChart.setHoleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(225)

        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.centerText = "All\n${deviceListSize}"
        binding.pieChart.setCenterTextSize(24f)
        binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        binding.pieChart.rotationAngle = 0f
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true

        setPieChartData(dataMap)

        activity?.runOnUiThread {
            binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
            binding.pieChart.spin(2000, 0F, 360F, Easing.EaseInOutQuad)
        }

        // binding.pieChart?.spin(2000, 0, 360);
        val legend = binding.pieChart.legend
        legend?.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend?.orientation = Legend.LegendOrientation.HORIZONTAL
        legend?.setDrawInside(false)
        legend?.xEntrySpace = 16f
        legend?.yEntrySpace = 0f
        legend?.yOffset = 0f
        legend?.isEnabled = true
        legend?.textColor = Color.BLACK


        activity?.runOnUiThread {
            // entry label styling
//            binding.pieChart.setDrawEntryLabels(false)
            binding.pieChart.setEntryLabelColor(Color.WHITE)
            binding.pieChart.setEntryLabelTextSize(12f)

            binding.pieChart.setDrawEntryLabels(false) // To remove slice text
//        binding.pieChart.setDrawMarkers(false); // To remove markers when click
//        binding.pieChart.setDrawEntryLabels(false); // To remove labels from piece of pie
//        binding.pieChart.getDescription().setEnabled(false);

        }
    }

    private fun setPieChartData(data: Map<String, Int>) {
        val entries = ArrayList<PieEntry>()
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        val dataKeys: ArrayList<String> = ArrayList(data.keys)
        try {
            for (i in 0 until data.size) {
                val analyticName = dataKeys[i]
                val hits = data[analyticName]
                entries.add(PieEntry(hits?.toFloat() ?: 0f, analyticName))
            }
        } catch (exception: Exception) {
            Log.w("FragmentHome", exception.toString())
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)
        dataSet.sliceSpace = 1f
        dataSet.iconsOffset = MPPointF(0f, 20f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)
        binding.pieChart.data = pieData

        // undo all highlights
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.weekButton.id -> {
                avgRunningKmTimeFrame = AvgRunningKmTimeFrame.WEEK
                prepareBarChartData(avgRunningKmsList)
                binding.weekButton.setPrimaryStyle()
                binding.monthButton.setSecondaryStyle()
                binding.yearButton.setSecondaryStyle()
            }
            binding.monthButton.id -> {
                avgRunningKmTimeFrame = AvgRunningKmTimeFrame.MONTH
                prepareBarChartData(avgRunningKmsList)
                binding.weekButton.setSecondaryStyle()
                binding.monthButton.setPrimaryStyle()
                binding.yearButton.setSecondaryStyle()
            }
            binding.yearButton.id -> {
                avgRunningKmTimeFrame = AvgRunningKmTimeFrame.YEAR
                prepareBarChartData(avgRunningKmsList)
                binding.weekButton.setSecondaryStyle()
                binding.monthButton.setSecondaryStyle()
                binding.yearButton.setPrimaryStyle()
            }
            binding.addDeviceTextView.id -> {
                activity?.navigateToActivity(AddDeviceActivity::class.java)
            }
            binding.vehicleLayout.runningVehicles.id,
            binding.vehicleLayout.stopVehicles.id,
            binding.vehicleLayout.idleVehicles.id,
            binding.vehicleLayout.noDataVehicles.id,
            binding.vehicleLayout.allVehicles.id -> {
                MainActivity.bottomNavigationView.selectedItemId = R.id.navigation_list
            }
        }
    }

    private fun initVehicleStatusCountOnUi() {
        val vehicleStatusMapWithCount: HashMap<String, Int> = HashMap()
        VehicleRepository.getVehicleStatusMap().forEach {
            vehicleStatusMapWithCount[it.key] = it.value.size
        }

        vehicleStatusMapWithCount.forEach {
            when (it.key) {
                Constants.VEHICLE_STATUS_KEY_RUNNING -> {
                    binding.vehicleLayout.runningTextView.text = "${getString(R.string.running)}\n(${it.value})"
                }
                Constants.VEHICLE_STATUS_KEY_STOP -> {
                    binding.vehicleLayout.stopTextView.text = "${getString(R.string.stop)}\n(${it.value})"
                }
                Constants.VEHICLE_STATUS_KEY_IDLE -> {
                    binding.vehicleLayout.idleTextView.text = "${getString(R.string.idle)}\n(${it.value})"
                }
                Constants.VEHICLE_STATUS_KEY_UNKNOWN -> {
                    binding.vehicleLayout.unknownTextView.text = "${getString(R.string.no_data)}\n(${it.value})"
                }
            }
        }

        binding.vehicleLayout.allTextView.text = "${getString(R.string.all)}\n(${VehicleRepository.getVehicleList().size})"
    }

}