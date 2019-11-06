package bitcoin.crypto.booster.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import bitcoin.crypto.booster.ada.AdaNet;
import bitcoin.crypto.booster.R;
import bitcoin.crypto.booster.mo.CapEntry;
import bitcoin.crypto.booster.mo.CapResponse;
import bitcoin.crypto.booster.mo.Ticker;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTicker extends Fragment implements View.OnClickListener {
    Ticker ticker;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.div_day)
    View divDay;
    @BindView(R.id.btn_day)
    LinearLayout btnDay;
    @BindView(R.id.tv_hour)
    TextView tvWeek;
    @BindView(R.id.div_hour)
    View divWeek;
    @BindView(R.id.btn_hour)
    LinearLayout btnWeek;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.div_month)
    View divMonth;
    @BindView(R.id.btn_month)
    LinearLayout btnMonth;
    @BindView(R.id.tv_quarter)
    TextView tvQuarter;
    @BindView(R.id.div_quarter)
    View divQuarter;
    @BindView(R.id.btn_quarter)
    LinearLayout btnQuarter;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.div_year)
    View divYear;
    @BindView(R.id.btn_year)
    LinearLayout btnYear;
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.percent)
    TextView percent;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.cap)
    TextView cap;
    @BindView(R.id.volume)
    TextView volume;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.progress_bar)
    SpinKitView progressBar;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.max)
    TextView max;
    @BindView(R.id.ticker3)
    TextView ticker3;
    @BindView(R.id.ticker4)
    TextView ticker4;

    MyValueFormatter formatter;


    public static Fragment newInstance(Ticker ticker) {
        FragmentTicker fragment = new FragmentTicker();
        Bundle args = new Bundle();
        args.putParcelable("ticker", ticker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ticker", ticker);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ti, container, false);

        if (savedInstanceState != null)
            ticker = savedInstanceState.getParcelable("ticker");
        else if (getArguments() != null)
            ticker = getArguments().getParcelable("ticker");

        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    private void getChart(String interval, Long start, Long end) {
        chart.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        AdaNet.history(ticker.slug, interval,String.valueOf(start),String.valueOf(end)).enqueue(new Callback<CapResponse>() {
            @Override
            public void onResponse(Call<CapResponse> call, Response<CapResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data!=null && response.body().data.size()>0) {
                    Log.i("DEV", "" + response.body().data.size());
                    refreshChart(response.body().data);
                } else {
                    error.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    chart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CapResponse> call, Throwable t) {
                error.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                chart.setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {
        name.setText(ticker.name + " (" + ticker.symbol + ") ");
        String per = String.valueOf(ticker.quotes.usd.percentChange24h) + " %";
        percent.setText(per);
        if (per.startsWith("-")) {
            percent.setTextColor(Color.parseColor("#cc0000"));
            price.setTextColor(Color.parseColor("#cc0000"));
        } else {
            percent.setTextColor(Color.parseColor("#19ff19"));
            price.setTextColor(Color.parseColor("#19ff19"));
        }
        price.setText(getMoney(ticker.quotes.usd.price));

        cap.setText(getMoney(ticker.quotes.usd.marketCap));
        volume.setText(getMoney(ticker.quotes.usd.volume24h));
        if (ticker.totalSupply != null) {
            total.setText(getMoney(ticker.totalSupply));
        } else {
            ticker3.setVisibility(View.INVISIBLE);
            total.setVisibility(View.INVISIBLE);
        }
        if (ticker.maxSupply != null) {
            max.setText(getMoney(ticker.maxSupply));
        } else {
            ticker4.setVisibility(View.INVISIBLE);
            max.setVisibility(View.INVISIBLE);
        }
        btnDay.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
        btnQuarter.setOnClickListener(this);
        btnYear.setOnClickListener(this);
        linechartModification();
        onClick(btnYear);

    }

    public String getMoney(double money) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        String moneyString = formatter.format(money);
        if (moneyString.endsWith(".00")) {
            int centsIndex = moneyString.lastIndexOf(".00");
            if (centsIndex != -1) {
                moneyString = moneyString.substring(1, centsIndex);
            }
        }
        return moneyString;
    }

    public void refreshChart(List<CapEntry> items) {

        chart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        chart.fitScreen();
        ArrayList<Entry> candleEntries = new ArrayList<Entry>();
        for (CapEntry item : items) {
            candleEntries.add(new Entry(item.time, Float.parseFloat(item.priceUsd)));
        }
        LineDataSet set = new LineDataSet(candleEntries, "ывм");

        set.setColor(Color.parseColor("#AFD755"));
//        set.setDrawValues(false);
        set.setLineWidth(1.4f);

        set.setDrawCircles(false);
        set.setCubicIntensity(0.2f);
        set.setDrawHighlightIndicators(false);
        set.setDrawFilled(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        LineData candleData = new LineData(set);
        candleData.setHighlightEnabled(true);
        candleData.setDrawValues(false);
        chart.setData(candleData);
        chart.animateX(500);
//        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.invalidate();

    }

    public void linechartModification() {
        formatter = new MyValueFormatter();
        chart.setBackgroundColor(Color.BLACK);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisLeft().setValueFormatter(new MyXAxisValueFormatter());
        chart.getXAxis().setTextColor(Color.parseColor("#AFD755"));
        chart.getAxisLeft().setTextColor(Color.parseColor("#AFD755"));
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setValueFormatter(formatter);
    }

    @Override
    public void onClick(View view) {
        String tek = "#BEE86E";
        Calendar current = Calendar.getInstance();
        Date now = new Date();
        switch (view.getId()) {
            case R.id.btn_hour:
                formatter.pattern = "HH:mm";
                current.add(Calendar.HOUR,-1);
                getChart("m1",current.getTime().getTime(),now.getTime());
                divDay.setBackgroundColor(Color.TRANSPARENT);
                divWeek.setBackgroundColor(Color.parseColor(tek));
                divMonth.setBackgroundColor(Color.TRANSPARENT);
                divQuarter.setBackgroundColor(Color.TRANSPARENT);
                divYear.setBackgroundColor(Color.TRANSPARENT);

                tvDay.setTextColor(Color.parseColor("#b2b2b2"));
                tvWeek.setTextColor(Color.parseColor(tek));
                tvMonth.setTextColor(Color.parseColor("#b2b2b2"));
                tvQuarter.setTextColor(Color.parseColor("#b2b2b2"));
                tvYear.setTextColor(Color.parseColor("#b2b2b2"));
                break;
            case R.id.btn_day:
                formatter.pattern = "HH";
                current.add(Calendar.DAY_OF_MONTH,-1);
                getChart("h1",current.getTime().getTime(),now.getTime());
                divDay.setBackgroundColor(Color.parseColor(tek));
                divWeek.setBackgroundColor(Color.TRANSPARENT);
                divMonth.setBackgroundColor(Color.TRANSPARENT);
                divQuarter.setBackgroundColor(Color.TRANSPARENT);
                divYear.setBackgroundColor(Color.TRANSPARENT);

                tvDay.setTextColor(Color.parseColor(tek));
                tvWeek.setTextColor(Color.parseColor("#b2b2b2"));
                tvMonth.setTextColor(Color.parseColor("#b2b2b2"));
                tvQuarter.setTextColor(Color.parseColor("#b2b2b2"));
                tvYear.setTextColor(Color.parseColor("#b2b2b2"));
                break;
            case R.id.btn_month:
                formatter.pattern = "dd MMMM";
                current.add(Calendar.MONTH,-1);
                getChart("d1",current.getTime().getTime(),now.getTime());
                divDay.setBackgroundColor(Color.TRANSPARENT);
                divWeek.setBackgroundColor(Color.TRANSPARENT);
                divMonth.setBackgroundColor(Color.parseColor(tek));
                divQuarter.setBackgroundColor(Color.TRANSPARENT);
                divYear.setBackgroundColor(Color.TRANSPARENT);

                tvDay.setTextColor(Color.parseColor("#b2b2b2"));
                tvWeek.setTextColor(Color.parseColor("#b2b2b2"));
                tvMonth.setTextColor(Color.parseColor(tek));
                tvQuarter.setTextColor(Color.parseColor("#b2b2b2"));
                tvYear.setTextColor(Color.parseColor("#b2b2b2"));
                break;
            case R.id.btn_quarter:
                formatter.pattern = "dd MMMM";
                current.add(Calendar.MONTH,-3);
                getChart("d1",current.getTime().getTime(),now.getTime());
                divDay.setBackgroundColor(Color.TRANSPARENT);
                divWeek.setBackgroundColor(Color.TRANSPARENT);
                divMonth.setBackgroundColor(Color.TRANSPARENT);
                divQuarter.setBackgroundColor(Color.parseColor(tek));
                divYear.setBackgroundColor(Color.TRANSPARENT);

                tvDay.setTextColor(Color.parseColor("#b2b2b2"));
                tvWeek.setTextColor(Color.parseColor("#b2b2b2"));
                tvMonth.setTextColor(Color.parseColor("#b2b2b2"));
                tvQuarter.setTextColor(Color.parseColor(tek));
                tvYear.setTextColor(Color.parseColor("#b2b2b2"));
                break;
            default:
                formatter.pattern = "dd MMMM";
                current.add(Calendar.YEAR,-1);
                getChart("d1",current.getTime().getTime(),now.getTime());
                divDay.setBackgroundColor(Color.TRANSPARENT);
                divWeek.setBackgroundColor(Color.TRANSPARENT);
                divMonth.setBackgroundColor(Color.TRANSPARENT);
                divQuarter.setBackgroundColor(Color.TRANSPARENT);
                divYear.setBackgroundColor(Color.parseColor(tek));

                tvDay.setTextColor(Color.parseColor("#b2b2b2"));
                tvWeek.setTextColor(Color.parseColor("#b2b2b2"));
                tvMonth.setTextColor(Color.parseColor("#b2b2b2"));
                tvQuarter.setTextColor(Color.parseColor("#b2b2b2"));
                tvYear.setTextColor(Color.parseColor(tek));
                break;
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return "$ "+String.format("%.0f",value);
        }
    }
    public class MyValueFormatter implements IAxisValueFormatter {
        public String pattern = "dd MMMM";

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Date d = new Date((long)value);
            DateFormat f = new SimpleDateFormat(pattern);
            return f.format(d);
        }
    }
}
