package com.example.android.firstproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    ImageButton datePicker,timePicker;
    EditText dateInput,timeInput;
    Button submit,setAlarm,cancelAlarm,snoozeAlarm;
    String alarmTime=null;
    long  milliseconds=0; /* alarmTime in milliseconds to set alarmManager */
    private int mYear,mMonth,mDay,mHour,mMinute; /* To store current time from calendar */
    private int TmYear,TmMonth,TmDay,TmHour,TmMinute; /* To store time input */

    private AlarmManager alarmManager = null;
    private PendingIntent pendingIntent = null;
    Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Bind buttons in this.Activity.xml file using Resource class  */
        datePicker = (ImageButton)findViewById(R.id.btn_date);
        timePicker = (ImageButton)findViewById(R.id.btn_time);
        dateInput = (EditText) findViewById(R.id.txt_date);
        dateInput.setEnabled(false);
        timeInput = (EditText)findViewById(R.id.txt_time);
        timeInput.setEnabled(false);
        submit = (Button)findViewById(R.id.submit);
        submit.setEnabled(false);
        setAlarm = (Button)findViewById(R.id.setAlarm);
        cancelAlarm = (Button)findViewById(R.id.cancelAlarm);
        cancelAlarm.setEnabled(false);
        snoozeAlarm = (Button)findViewById(R.id.snoozeAlarm);
        snoozeAlarm.setEnabled(false);


        /* Add listener to the click events*/
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        submit.setOnClickListener(this);

        /* Fetch Date from the Calendar in the First Activity*/
        String FirstActivity_Date= getIntent().getStringExtra(CalendarActivity.EXTRA_FirstActivity_Date);
           try {
               TmDay = Integer.parseInt(FirstActivity_Date.split("-")[0]);
           TmMonth = Integer.parseInt(FirstActivity_Date.split("-")[1]);
           TmYear = Integer.parseInt(FirstActivity_Date.split("-")[2]);
       }catch (NumberFormatException exp){Log.i("Activity_First","ParseException: "+exp);}
        catch (NullPointerException exp) {Log.i("Activity_First","ParseException: "+exp);}
        dateInput.setText(FirstActivity_Date);

        /* Setting static Alarm*/
        setAlarm.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                intent = new Intent(getApplicationContext(),AlarmReciever.class);
                 pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),234324243,intent,0);
                if((TmHour!=0 || TmMinute!=0) && TmYear!=0 && TmMonth!=0 && TmDay!=0 ) {
                    alarmTime=TmDay+"-"+TmMonth+"-"+TmYear+" "+TmHour+":"+TmMinute;
                     milliseconds = getMillisecondsFromDateTime(alarmTime,"dd-MM-yyyy HH:mm",null);
                    if(milliseconds>System.currentTimeMillis())
                    {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent);
                        displayToast(MainActivity.this,"Alarm set for "+alarmTime,Toast.LENGTH_LONG);
                        setAlarm.setEnabled(false);
                        cancelAlarm.setEnabled(true);
                    }
                    else{
                        displayToast(MainActivity.this,"Choose time should not be past",Toast.LENGTH_LONG);
                    }
                }
                else{
                    displayToast(MainActivity.this,"Choose time should not be past",Toast.LENGTH_LONG);
                }
                Log.i("AlarmStatus:","Alarm Trigger Success: "+milliseconds+":"+System.currentTimeMillis());
            }
        });

        /* To cancel the Alarm */
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmManager!=null && pendingIntent!=null){
                   // pendingIntent = PendingIntent.getActivity(MainActivity.this,
                    //        234324243, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                  //  pendingIntent.cancel();
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    cancelAlarm.setEnabled(false);
                    setAlarm.setEnabled(true);
                    displayToast(MainActivity.this,"Alarm during: "+alarmTime+" is cancelled",Toast.LENGTH_LONG);
                }
            }
        });

    }


    @Override
    public void onClick(View v){
        Log.d("day",mDay+"0");
        Log.d("time",mMinute+"0");

        /* To show Date dialog to take input from user and to display selected date
         from the dialog to text field dateInput/@R.id.txt_date */
        if(v==datePicker)
        {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                           dateInput.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            TmDay=dayOfMonth; TmMonth=monthOfYear+1; TmYear=year;
                        }
                    },mYear,mMonth,mDay);
        dpd.show();
        }

        /* To show Time dialog to take input from user and to display selected time
         from the dialog to text field timeInput/@R.id.txt_time*/
        if(v==timePicker)
        {
            final Calendar c= Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                   timeInput.setText(hourOfDay+":"+minute);
                    TmHour=hourOfDay; TmMinute=minute;
                }
            },mHour,mMinute,true);
            Log.i("time.show","before time picker");
            tpd.show();
        }

        /* On click submit button display user date and time inputs through Toast msg */
        if(v==submit)
        {
            //Toast.makeText(this, TmDay+"/"+TmMonth+"/"+TmYear+" "+TmHour+":"+TmMinute, Toast.LENGTH_LONG).show();
            displayToast(this,TmDay+"/"+TmMonth+"/"+TmYear+" "+TmHour+":"+TmMinute,Toast.LENGTH_LONG);
        }


    }

    /* This method is called for displaying Toast messages, this is a reusable code*/
  public void displayToast(Context cntxt,CharSequence msg,int period){
       Toast.makeText(cntxt, msg,period).show();
    }
     /* This method is called for converting the time/@string to milliseconds */
    public long getMillisecondsFromDateTime(String alarmString,String timeFormat,String timeZone){
        long alarmTimeInMilliSeconds=0;
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        try {
            Date alarmTime = sdf.parse(alarmString);
            alarmTimeInMilliSeconds=alarmTime.getTime();

        }catch (ParseException e){
            Log.i("AlarmParse: ","failed");
            e.printStackTrace();
        }
        return alarmTimeInMilliSeconds;
    }

}
