package com.example.maashi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    public ArrayList<Shift> shifts;
    Context context;
    LayoutInflater sInflater;
    RecyclerView rv;
    ShiftsList parent;


    public ShiftAdapter(Context context, ArrayList<Shift> shifts,ShiftsList parent) {
        this.shifts=shifts;
        sInflater=LayoutInflater.from(context);
        this.parent=parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context); // put layout of main activity in layout inflater
        View shiftView = inflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(shiftView,this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shift shift=shifts.get(position);
        holder.date.setText(shift.getDate());
        holder.finish.setText(shift.getFinishingTime());
        holder.start.setText(shift.getStartingTime());
        holder.date.setText(shift.getDate());
        holder.total.setText("0");
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView total,start,finish,date,hours;
        public ShiftAdapter adapter;
        public ViewHolder(@NonNull View itemView , ShiftAdapter adapter) {
            super(itemView);
            total=itemView.findViewById(R.id.total);
            start=itemView.findViewById(R.id.start);
            finish=itemView.findViewById(R.id.finish);
            hours=itemView.findViewById(R.id.hours);
            date=itemView.findViewById(R.id.date);
            this.adapter=adapter;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            Log.v("click","clicked");
            int mPosition = getLayoutPosition();
            Shift element = shifts.get(mPosition);
            shifts.set(mPosition, element);
            adapter.notifyDataSetChanged();
            Intent editShift = new Intent(view.getContext(), EditShift.class);
            if (editShift != null) {
                editShift.putExtra("shiftId", element.getId());
                Log.v("database","shift id: "+element.getId());
                context.startActivity(editShift);                                                 //TODO NEED to Activate this Intent
            } else {
                Toast.makeText(context, "Warning intent is null ", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
