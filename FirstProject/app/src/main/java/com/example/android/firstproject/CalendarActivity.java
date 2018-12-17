package com.example.android.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.CalendarView;

import java.util.Calendar;


public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_nextActivity;
    CalendarView calendarView;
    private int sYear,sMonth,sDayOfMonth;
    private long sMilliSeconds;
    private String sDate;
    public final static String EXTRA_FirstActivity_Date = "com.example.android.firstproject.FirstActivity_Date";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        btn_nextActivity=(Button)findViewById(R.id.btn_nextActivity);
        btn_nextActivity.setOnClickListener(this);
        calendarView = (CalendarView)findViewById(R.id.calendarView);

       /* calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if(view!=null) {
                    sYear = year;
                    sMonth = month + 1;
                    sDayOfMonth = dayOfMonth;
                    sDate = sDayOfMonth + "-" + sMonth + "-" + sYear;
                }
            }
        });*/

    }


    @Override
    public void onClick(View v){
       if(v==btn_nextActivity){
           /* when Next Activity button is clicked, then fetch the date selected from the calendar */
           sMilliSeconds= calendarView.getDate();
           Calendar c = Calendar.getInstance();
           c.setTimeInMillis(sMilliSeconds);
           sDate = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.YEAR);

           /* Passing selected date of the calendar to the MainActivity */
        startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra(EXTRA_FirstActivity_Date,sDate));
           Log.i("Activity","FirstActivity_Date :"+sDate+" passed to Intent");
       }
    }
}
