package com.example.smartscheduler.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartscheduler.R;

public class viewHolder extends RecyclerView.ViewHolder {
    TextView time,name;
    CardView cardView;
    public viewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.classtime);
        name = itemView.findViewById(R.id.subname);
        cardView = itemView.findViewById(R.id.cardview);
    }
}
