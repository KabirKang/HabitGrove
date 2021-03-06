package com.kabirkang.habitgrove.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.kabirkang.habitgrove.R;
import com.kabirkang.habitgrove.graphs.formatters.BaseAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kabirkang on 9/24/17.
 */

@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

    private TextView mContentTextView;
    private BaseAxisValueFormatter mXAxisValueFormatter;

    public XYMarkerView(Context context, BaseAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.marker_view);

        this.mXAxisValueFormatter = xAxisValueFormatter;
        this.mContentTextView = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long date = mXAxisValueFormatter.getDateForValue(e.getX());
        mContentTextView.setText(FORMATTER.format(date) + ". " + String.valueOf((int) e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}