package com.example.maashi;



import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Shift extends AppCompatActivity {
    private long totalTime,hourlyPay,id;
    private String date,startingTime,finishingTime,total;

    public Shift(){}

    public Shift(long totalTime,String date,String startingTime,String finishingTime,String total){
        this.totalTime=totalTime;
        this.date=date;
        this.startingTime=startingTime;
        this.finishingTime=finishingTime;
        this.total=total;
    }



//    public static ArrayList<Shift> createShiftsList(int numShifts){
//        ArrayList<Shift> shifts = new ArrayList<Shift>();
//        for(int i=1;i<=numShifts;i++){
//            shifts.add(new )
//        }
//    }
    public String getFinishingTime() { return finishingTime; }

    public void setFinishingTime(String finishingTime) {
        this.finishingTime = finishingTime;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public String getTotal(){return total;}

    public void setTotal(String total){this.total=total;}

    public long getHourlyPay() { return hourlyPay; }

    public void setHourlyPay(long hourlyPay) { this.hourlyPay = hourlyPay; }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
}
