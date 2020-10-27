package com.example.maashi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static java.lang.Double.parseDouble;

public class DatabaseHelper extends SQLiteOpenHelper {
    long ret;

    public static final String DATABASE_NAME = "Shift.db";
    public static final String TABLE_NAME = "shift_table";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "TOTAL";
    public static final String COL_2 = "HOURS";
    public static final String COL_3 = "FINISH";
    public static final String COL_4 = "STARTT";
    public static final String COL_5 = "DATEE";
    public static final String COL_6 = "PAY";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TOTAL TEXT,HOURS TEXT,FINISH TEXT,STARTT TEXT,DATEE TEXT,PAY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public long insertData(Shift shift) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, shift.getTotal());
        contentValues.put(COL_2, shift.getTotalTime());
        contentValues.put(COL_3, shift.getFinishingTime());
        contentValues.put(COL_4, shift.getStartingTime());
        contentValues.put(COL_5, shift.getDate());
        contentValues.put(COL_6, shift.getHourlyPay());

        try {
            ret=db.insert(TABLE_NAME, null, contentValues);
        } catch (RuntimeException e) {
            Log.v("database", "updateData: updating values error");
            return ret;
        }
        Log.v("database", "updateData: updating values success");
        return ret;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


    public boolean updateData(Shift shift) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, shift.getTotal());
        contentValues.put(COL_2, shift.getTotalTime());
        contentValues.put(COL_3, shift.getFinishingTime());
        contentValues.put(COL_4, shift.getStartingTime());
        contentValues.put(COL_5, shift.getDate());
        contentValues.put(COL_6, shift.getHourlyPay());

        try {
            db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{Long.toString(shift.getId())});
        } catch (RuntimeException e) {
            Log.v("database", "insertData: inserting values error");
            return false;
        }
        Log.v("database", "insertData: inserting values success");
        return true;
    }

    public Cursor getData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where ID = "+id+"",null);
        return res;
    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public void history(String startdate,String enddate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME +
                " WHERE STARTT BETWEEN ?  AND ?", new String[]{startdate, enddate});
    }

    public double getPay(String startdate,String enddate,boolean all){
        double amount=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        c=db.rawQuery("SELECT PAY FROM " +TABLE_NAME+" WHERE STARTT BETWEEN ?  AND ?", new String[]{startdate,enddate});
        if(c.moveToFirst())
            amount += parseDouble(c.getString(0));
        else
            amount=-1;
        c.close();
        return amount;
    }
}
