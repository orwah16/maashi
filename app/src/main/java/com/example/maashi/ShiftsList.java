package com.example.maashi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ShiftsList extends AppCompatActivity {
    DatabaseHelper myDb;

    private ArrayList<Shift> shifts;
    RecyclerView rv;
    ShiftAdapter adapter;
    Shift shift;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        myDb = new DatabaseHelper(this);

        Log.v("Shifts list","start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        rv = findViewById(R.id.recycler);
        //Bundle bundle = getIntent().getExtras();
        viewAll();

        ImageView mainActivityBtn = findViewById(R.id.set_Time);
        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ShiftsList.this, MainActivity.class);
                ShiftsList.this.startActivity(myIntent);
            }
        });
        ImageView sittingsBtn = findViewById(R.id.settings);
        sittingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ShiftsList.this, Settings.class);
                ShiftsList.this.startActivity(myIntent);
            }
        });

    }
    public void viewAll(){
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            Toast.makeText(this, "لم تسجل اي ورديات", Toast.LENGTH_SHORT).show();
        }
        shifts = new ArrayList<Shift>();
        while (res.moveToNext()){
            shift = new Shift();
            shift.setId(res.getLong(0));
            shift.setTotal(res.getString(1));
            shift.setTotalTime(res.getLong(2));
            shift.setFinishingTime(res.getString(3));
            shift.setStartingTime(res.getString(4));
            shift.setDate(res.getString(5));
            shifts.add(shift);

        }
        adapter = new ShiftAdapter(getApplicationContext(), shifts, ShiftsList.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }


}
