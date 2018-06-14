package com.intelligencefactory.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

public class CalendarActivity extends AppCompatActivity
{
    CalendarView cv;
    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cv = (CalendarView)findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth)
            {
                // 使用Toast显示用户选择的日期
                Toast.makeText(CalendarActivity.this,
                        "你选择的是" + year + "年" + month + "月"
                                + dayOfMonth + "日",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dp = (DatePicker)findViewById(R.id.dateAndTimePicker_datePicker);

        Log.d("CalendarActivity", "11111111111111111 ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
