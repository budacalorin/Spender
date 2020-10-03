package com.example.dayplanner.chart;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.edmodo.rangebar.RangeBar;
import com.example.dayplanner.utils.DatePickerFragment;
import com.example.dayplanner.R;
import com.example.dayplanner.db.spender.SpenderItemsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.dayplanner.chart.SliceInfoAlertDialogKt.getSliceInfoAlertDialog;
import static com.example.dayplanner.utils.DataBaseUtilsKt.dataToHashMap;
import static com.example.dayplanner.utils.DataBaseUtilsKt.getCursorForDatabase;
import static com.example.dayplanner.utils.DataBaseUtilsKt.hashMapToList;
import static com.example.dayplanner.utils.DateUtilsKt.dateToString;
import static com.example.dayplanner.utils.DateUtilsKt.dayDiff;
import static com.example.dayplanner.utils.DateUtilsKt.stringToDate;

public class AllChartActivity extends AppCompatActivity {
    ArrayList<String> namesList;
    AnyChartView chartView;
    Pie pie;
    Date begin;
    Date end;
    Date chosenLeft;
    Date chosenRight;
    RangeBar rangeBar;
    TextView beginTextView;
    TextView endTextView;
    TextView totalTextView;
    Context CONTEXT = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chart);

        Intent intent = getIntent();

        namesList = intent.getStringArrayListExtra("names");

        chartView = (AnyChartView) findViewById(R.id.any_chart_view_all);
        pie = AnyChart.pie();

        initializeDates();

        initializeSlider();

        initializeTextViews();

        initializePie();
    }

    private void initializePie() {
        updatePie(begin, end);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Color color = Color.valueOf(getColor(R.color.colorPrimaryDark));
            String hexColor = String.format("#%06X", (0xFFFFFF & color.toArgb()));

            pie.background().fill(hexColor, 0);

            chartView.setBackgroundColor(hexColor);
        }

        pie.title("All expenses");
        pie.fill("aquastyle");
        pie.sort("desc");
//        pie.labels().position("outside");
        pie.labels().format("{%value}");
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                pie.unselect();
                String category = event.getData().get("x");
                AlertDialog alertDialog = getSliceInfoAlertDialog(CONTEXT, namesList, category, chosenLeft, chosenRight);
                if (alertDialog != null)
                    alertDialog.show();
                else
                    Toast.makeText(CONTEXT, "Error", Toast.LENGTH_LONG).show();
            }
        });

        chartView.setChart(pie);
    }

    private void initializeDates() {
        begin= new Date( 2099, 12, 31);
        end= new Date(2020,01,01);

        for (String name: namesList){
            Cursor cursor = getCursorForDatabase(name, this);

            while (cursor.moveToNext()){
                String date = cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_DATE);
                Date parsedDate = stringToDate(date);
                if (parsedDate.before(begin))
                    begin = parsedDate;
                if (parsedDate.after(end))
                    end = parsedDate;
            }
        }
        chosenLeft = new Date(begin.getTime());
        chosenRight = new Date(end.getTime());
    }


    private void initializeTextViews() {
        totalTextView = ((TextView) findViewById(R.id.allChartTotalTextView));

        beginTextView = ((TextView) findViewById(R.id.allChartBeginTextBox));
        endTextView = ((TextView) findViewById(R.id.allChartEndTextBox));

        beginTextView.setText(dateToString(begin));
        endTextView.setText(dateToString(end));

        beginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date(year,month,dayOfMonth);
                        if (begin.after(date) || end.before(date)){
                            Toast.makeText(CONTEXT, "Date should be between " + dateToString(begin) + " and " + dateToString(end) +"!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        chosenLeft = date;
                        updateRange(date);
                    }
                });
                newFragment.setDate(chosenLeft);
                newFragment.show(getSupportFragmentManager(),"Choose date");
            }
        });

        endTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date(year,month,dayOfMonth);
                        if (begin.after(date) || end.before(date)){
                            Toast.makeText(CONTEXT, "Date should be between " + dateToString(begin) + " and " + dateToString(end) +"!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        chosenRight = date;
                        updateRange(date);
                    }
                });
                newFragment.setDate(chosenRight);
                newFragment.show(getSupportFragmentManager(),"Choose date");
            }
        });
    }

    private void initializeSlider(){
        rangeBar = (RangeBar) findViewById(R.id.allChartRangeBar) ;

        Integer dayDiff = dayDiff(begin, end);

        rangeBar.setTickCount(dayDiff + 1);
        rangeBar.setTickHeight(0);
        rangeBar.setThumbRadius(7);
        rangeBar.setBarColor(R.color.colorPrimary);
        rangeBar.setConnectingLineColor(R.color.colorAccent);
        rangeBar.setThumbColorNormal(R.color.colorPrimary);
        rangeBar.setThumbColorPressed(R.color.colorAccent);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                chosenLeft.setTime(begin.getTime() + ((long) i) * 86400000L);
                chosenRight.setTime(begin.getTime() + ((long) i1) * 86400000L);

                updatePie(chosenLeft, chosenRight);
            }
        });
    }

    private void updatePie(Date begin, Date end){
        ArrayList<DataEntry> allData;

        HashMap<String, Double> fullHashMap = new HashMap<>();

        for (String name: namesList){
            HashMap<String, Double> hashMap = dataToHashMap(name, begin, end, this);

            for (String key: hashMap.keySet()){
                if (fullHashMap.containsKey(key)){
                    fullHashMap.put(key, fullHashMap.get(key) + hashMap.get(key));
                } else {
                    fullHashMap.put(key, hashMap.get(key));
                }
            }
        }

        allData = hashMapToList(fullHashMap, false);

        pie.data(allData);

        updateTotal(allData);

        ((TextView) findViewById(R.id.allChartBeginTextBox)).setText(dateToString(chosenLeft));
        ((TextView) findViewById(R.id.allChartEndTextBox)).setText(dateToString(chosenRight));


    }

    private void updateRange(Date date){
        if (chosenLeft.after(chosenRight)){
            long time = chosenLeft.getTime();
            chosenLeft.setTime(chosenRight.getTime());
            chosenRight.setTime(time);
        }

        beginTextView.setText(dateToString(chosenLeft));
        endTextView.setText(dateToString(chosenRight));

        rangeBar.setThumbIndices(dayDiff(begin, chosenLeft), dayDiff(begin, chosenRight));
    }

    private void updateTotal(ArrayList<DataEntry> list){
        try{
            Double total = 0.0;
            for (DataEntry item: list){
                Object obj = item.getValue("value");
                total += Double.parseDouble(obj.toString());
            }
            totalTextView.setText("Total: " +total.toString());
        } catch (Exception e) {
            totalTextView.setText("Total: Error");
        }
    }
}
