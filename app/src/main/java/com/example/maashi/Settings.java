package com.example.maashi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {
    EditText hourPay,extraHour,breaks,travel;
    String pay,extraPay,breaksPay,travelPay;
    ImageButton button;
    SharedPreferences pref;
    public static final String PREFS_NAME = "com.example.maashi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sittings);
        hourPay=findViewById(R.id.oneHourPayInput);
        extraHour=findViewById(R.id.extraHourPayInput);
        breaks=findViewById(R.id.paidBreakInput);
        travel=findViewById(R.id.travelInput);
        button=findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay=hourPay.getText().toString();
                extraPay=extraHour.getText().toString();
                breaksPay=breaks.getText().toString();
                travelPay=travel.getText().toString();
                calcExtra();
                saveData();
            }
        });
        ImageView shiftsListBtn = findViewById(R.id.profile);
        shiftsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Settings.this, ShiftsList.class);
                Settings.this.startActivity(myIntent);
            }
        });
        ImageView mainActivityBtn = findViewById(R.id.set_Time);
        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(myIntent);
            }
        });

    }
    private void calcExtra(){
        if(extraPay=="0"){
            extraPay=pay;
        }
    }

    public void saveData(){
        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        pref.edit().putString("pay", pay).apply();
        pref.edit().putString("extraPay", extraPay).apply();
        pref.edit().putString("breaksPay", breaksPay).apply();
        pref.edit().putString("travelPay", travelPay).apply();

    }





}
