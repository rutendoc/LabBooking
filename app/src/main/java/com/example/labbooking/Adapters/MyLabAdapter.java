package com.example.labbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labbooking.Models.Lab.Lab;
import com.example.labbooking.R;

import java.util.List;

public class MyLabAdapter extends RecyclerView.Adapter<MyLabAdapter.MyViewHolder> {

    Context context;
    List<Lab> labList;

    public MyLabAdapter(Context context, List<Lab> labList) {
        this.context = context;
        this.labList = labList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_lab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_lab_name.setText(labList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return labList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_lab_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_lab_name = (TextView)itemView.findViewById(R.id.txt_lab_name);

        }
    }
}
