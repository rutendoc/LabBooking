package com.example.labbooking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labbooking.Common.Common;
import com.example.labbooking.Interfaces.IRecyclerItemSelectedListener;
import com.example.labbooking.Models.Building;
import com.example.labbooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyBuildingAdapter extends RecyclerView.Adapter<MyBuildingAdapter.MyViewHolder> {
    Context context;
    List<Building> buildingList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyBuildingAdapter(Context context, List<Building> buildingList) {
        this.context = context;
        this.buildingList = buildingList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_building, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_building_name.setText(buildingList.get(position).getName());
        if (cardViewList.contains(holder.card_building)){
            cardViewList.add(holder.card_building);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white background for all cards not selected
                for (CardView cardView: cardViewList)
                    cardView.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white));

                //Set background for only selected item
                for (CardView cardView: cardViewList)
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.holo_green_light));

                //Send broadcast to tell BookingActivity to enable Button Next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_BUILDING_STORE, buildingList.get(pos));
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_building_name;
        CardView card_building;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_building = (CardView)itemView.findViewById(R.id.card_building);
            txt_building_name = (TextView)itemView.findViewById(R.id.txt_building_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
