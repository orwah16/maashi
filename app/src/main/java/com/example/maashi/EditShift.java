package com.example.maashi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class EditShift extends AppCompatActivity {
    Long id;
    int sameDay=0;
    DatabaseHelper myDb;
    Shift shift;
    Cursor res;
    EditText payV,extraHourPayV,extraHoursNumberV,extraPayV;
    ImageButton saveBtn,deleteBtn;
    TimePicker startingTimeP,finishingTimeP;
    private double pay,extraHourPay,extraHoursNumber,extraPay,breaksPay,travelPay;
    Switch manualTimeS,sameDayS;
    SharedPreferences pref;
    boolean manualTime=false;
    public static final String PREFS_NAME = "com.example.maashi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);

        setContentView(R.layout.activity_manual_shift);
        Bundle bundle = getIntent().getExtras();
        id=bundle.getLong("shiftId");
        payV=findViewById(R.id.hourPayInput);
        extraHourPayV=findViewById(R.id.extraHoursPayInput);
        extraHoursNumberV=findViewById(R.id.extraHourNumberInput);
        extraPayV=findViewById(R.id.extraPayInput);
        startingTimeP=findViewById(R.id.timePicker1);
        finishingTimeP=findViewById(R.id.timePicker2);
        manualTimeS=findViewById(R.id.timeSwitch);
        manualTimeS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    manualTime=true;
                    startingTimeP.setVisibility(View.VISIBLE);
                    finishingTimeP.setVisibility(View.VISIBLE);
                    sameDayS.setVisibility(View.VISIBLE);
                }else{
                    manualTime=false;
                    startingTimeP.setVisibility(View.GONE);
                    finishingTimeP.setVisibility(View.GONE);
                    sameDayS.setVisibility(View.GONE);
                }
            }
        });
        sameDayS=findViewById(R.id.sameDay);
        sameDayS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sameDay=24;
                }else{
                    sameDay=0;
                }
            }
        });
        startingTimeP.setVisibility(View.GONE);
        finishingTimeP.setVisibility(View.GONE);
        sameDayS.setVisibility(View.GONE);
        deleteBtn=findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int isDelete=myDb.deleteData(Long.toString(shift.getId()));
                //desplay error message
            }
        });
        saveBtn=findViewById(R.id.saveButton);
        saveBtn.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                shift.setTotal(Double.toString(calcPay()));
                shift.setTotalTime(res.getLong(2));
                shift.setFinishingTime(getTime(finishingTimeP));
                shift.setStartingTime(getTime(startingTimeP));
                boolean isUpdate = myDb.updateData(shift);
                //display error message
                Intent intent = new Intent(EditShift.this, ShiftsList.class);
                EditShift.this.startActivity(intent);
            }
        });
        loadData();   //shared preferences
        getShiftFromDb();
        setFields();

    }

    private void getShiftFromDb(){
        res = myDb.getData(id);
        res.moveToNext();
        shift = new Shift();

        Log.v("database","id = "+res.getLong(0));
        shift.setId(res.getLong(0));
        shift.setTotal(res.getString(1));
        shift.setTotalTime(res.getLong(2));
        shift.setFinishingTime(res.getString(3));
        shift.setStartingTime(res.getString(4));
        shift.setDate(res.getString(5));
    }

    private void setFields(){
        payV.setText(Double.toString(pay));
        extraHourPayV.setText(Double.toString(extraPay));
        extraHoursNumberV.setText("0");
        extraPayV.setText("0");

        //shift.setTotal(payV.getText().toString());
        startingTimeP.setCurrentHour(Integer.valueOf(shift.getStartingTime().split(":")[0]));
        startingTimeP.setCurrentMinute(Integer.valueOf(shift.getStartingTime().split(":")[1]));
        finishingTimeP.setCurrentHour(Integer.valueOf(shift.getFinishingTime().split(":")[0]));
        finishingTimeP.setCurrentMinute(Integer.valueOf(shift.getFinishingTime().split(":")[1]));
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

    private double calcPay(){
        double total=0,time;
        if(manualTime){
            time=calcTime(startingTimeP,finishingTimeP);
            total=(time-extraHoursNumber)*pay+(extraHourPay*extraHoursNumber)+extraPay;
        }else{
            time=shift.getTotalTime();
            double prevPay = Double.valueOf(shift.getTotal());
            total = prevPay-(extraHoursNumber*pay)+(extraHoursNumber*extraHourPay)+extraPay;
        }
        return total;
    }

    private String getTime(TimePicker p){
        int hour = p.getCurrentHour();
        int minute = p.getCurrentMinute();
        String res = String.format("%02d"+":"+"%02d",hour,minute);
        return res;
    }

    private double calcTime(TimePicker p1,TimePicker p2){
        int hour1,hour2,minute1,minute2;
        hour1 = p1.getCurrentHour();
        minute1 = p1.getCurrentMinute();
        hour2 = p2.getCurrentHour();
        minute2 = p2.getCurrentMinute();
        hour1 = hour2-hour1;
        minute1 = minute2-minute1;
        return hour1+(minute1/60);
    }


}
