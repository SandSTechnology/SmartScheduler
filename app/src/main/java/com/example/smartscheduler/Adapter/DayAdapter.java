package com.example.smartscheduler.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartscheduler.R;
import com.example.smartscheduler.model.TimeTableModel;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<viewHolder>{
    private final Context context;
    private final ArrayList<TimeTableModel> list;

    public DayAdapter(Context context, ArrayList<TimeTableModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetableitem, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final TimeTableModel timeTableModel = list.get(position);
        holder.time.setText(timeTableModel.getTimeslot());
        holder.name.setText(timeTableModel.getCourse());

        switch(position % 6)
        {
            case 0:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
                break;

            case 1:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
                break;

            case 2:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color3));
                break;

            case 3:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color4));
                break;

            case 4:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color5));
                break;
            case 5:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color6));
                break;

            default:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
