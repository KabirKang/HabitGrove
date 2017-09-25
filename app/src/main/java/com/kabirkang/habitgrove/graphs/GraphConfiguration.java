package com.kabirkang.habitgrove.graphs;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kabirkang.habitgrove.graphs.formatters.BaseAxisValueFormatter;
import com.kabirkang.habitgrove.graphs.formatters.GraphValueFormatter;
import com.kabirkang.habitgrove.graphs.formatters.MonthAxisValueFormatter;
import com.kabirkang.habitgrove.graphs.formatters.WeekDayAxisValueFormatter;
import com.kabirkang.habitgrove.graphs.formatters.YearAxisValueFormatter;
import com.kabirkang.habitgrove.models.Habit;
import com.kabirkang.habitgrove.view.GraphView;
import com.kabirkang.habitgrove.view.XYMarkerView;

import java.util.ArrayList;
import java.util.List;

public final class GraphConfiguration {

    private static final int MAX_VISIBLE_VALUE_COUNT = 60;

    private BarChart mBarChart;
    private BaseAxisValueFormatter mXAxisFormatter;

    private GraphDataSource mDataSource;
    private GraphView mView;

    public GraphConfiguration(BarChart barChart) {
        this.mBarChart = barChart;
    }

    public void setup(@NonNull final GraphDataSource dataSource,
                      @NonNull final GraphView view) {
        this.mDataSource = dataSource;
        this.mView = view;
        configureBarChart();
        setData();
    }

    private void configureBarChart() {
        mBarChart.setDrawBarShadow(true);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setDrawGridBackground(false);

        mBarChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);
        mBarChart.setPinchZoom(false);

        configureXAxis();
        configureLeftAxis();
        configureRightAxis();
        configureLegend();
        configureMarkerView();
    }

    private void configureXAxis() {
        mXAxisFormatter = mView.getXAxisFormatter();

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(mDataSource.getNumberOfEntries());
        xAxis.setValueFormatter(mXAxisFormatter);
    }

    private void configureLeftAxis() {
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
    }

    private void configureRightAxis() {
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configureLegend() {
        Legend legend = mBarChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);
    }

    private void configureMarkerView() {
        XYMarkerView markerView = new XYMarkerView(mBarChart.getContext(), mXAxisFormatter);
        markerView.setChartView(mBarChart);
        mBarChart.setMarker(markerView);
    }

    private void setData() {
        List<BarEntry> yValues = mDataSource.getData();
        final int labelCount = ((int) Math.floor(mDataSource.getMaxValue() / 2)) + 1;

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(labelCount, false);

        final String setName = mView.getBarDataSetName(mBarChart.getContext());

        BarDataSet barDataSet;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(yValues);
            barDataSet.setLabel(setName);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();
        } else {
            barDataSet = new BarDataSet(yValues, setName);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueFormatter(new GraphValueFormatter());

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.7f);

            mBarChart.setData(data);
        }
    }

}