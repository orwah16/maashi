package com.example.maashi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    Context context;
    Integer hours, minutes;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset, totalTime,shiftId;
    private double pay=0,extraPay=0,breaksPay=0,travelPay=0;
    ImageView start, pause, stop;
    String currentDate, startingTime, finishingTime;
    Shift shift;
    SharedPreferences pref;
    public static final String PREFS_NAME = "com.example.maashi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new DatabaseHelper(this);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        start = findViewById(R.id.start_button);
        pause = findViewById(R.id.pause2);
        stop = findViewById(R.id.stop2);
        start.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        //time = Calendar.getInstance().getTime();
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.v("mainActivity", "current date: " + currentDate);
        ImageButton shiftsListBtn = findViewById(R.id.profile);
        shiftsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ShiftsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        ImageButton sittingsBtn = findViewById(R.id.settings);
        sittingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    public void startChronometer(View view) {
        if (!running) {
            startingTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            Log.v("mainActivity", "starting time: " + startingTime);
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            start.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            running = true;
        }
    }

    public void pauseChronometer(View view) {
        if (running) {
            chronometer.stop();
            start.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void stopChronometer(View view) {
        shift = new Shift();
        chronometer.setBase((SystemClock.elapsedRealtime()));
        start.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        totalTime = pauseOffset;
        chronometer.stop();
        Toast.makeText(getApplicationContext(), "" + totalTime, Toast.LENGTH_SHORT).show();
        pauseOffset = 0;
        finishingTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        Log.v("mainActivity", "finishing time: " + finishingTime);
        Intent intent = new Intent(this, EditShift.class);
        Log.v("mainActivity", "end main");
        totalTime(totalTime);
        loadData();
        if(shift != null) {
            shift.setTotalTime(totalTime);
            shift.setDate(currentDate);
            shift.setStartingTime(startingTime);
            shift.setFinishingTime(finishingTime);
            shift.setTotal(String.valueOf(totalPay()));
            shiftId=myDb.insertData(shift);
        }
        else{
            Log.v("database","shift is null");
        }
        intent.putExtra("shiftId", shiftId);
        startActivity(intent);
    }

    private String totalTime(long time) {
        hours = (int) time / 3600000;
        time -= (long) (hours * 3600000);
        minutes = (int) time / 60000;
        return hours.toString() + ":" + minutes.toString();
    }
    private double totalPay(){
        double sum;
        sum=(hours+(((double)minutes)/60))*pay;
        sum+=travelPay;
        sum+=(breaksPay/60)*(pay);
        return sum;
    }


    private void loadData(){
        String payReturn,extraPayReturn,breaksReturn,travelPayReturn;
        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        payReturn=pref.getString("pay", "");
        extraPayReturn=pref.getString("extraPay", "");
        breaksReturn = pref.getString("breaksPay", "");
        travelPayReturn = pref.getString("travelPay", "");
        if(payReturn==""){
            payReturn = "0";
        }
        if(extraPayReturn==""){
            extraPayReturn = "0";
        }
        if(breaksReturn==""){
            breaksReturn = "0";
        }
        if(travelPayReturn==""){
            travelPayReturn = "0";
        }
        Log.v("database","extra pay"+extraPayReturn);
        pay=Double.parseDouble(payReturn);
        extraPay=Double.parseDouble(extraPayReturn);
        breaksPay=Double.parseDouble(breaksReturn);
        travelPay=Double.parseDouble(travelPayReturn);
    }

}