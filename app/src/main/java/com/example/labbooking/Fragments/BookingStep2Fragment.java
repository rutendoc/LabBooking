package com.example.labbooking.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labbooking.Adapters.MyLabAdapter;
import com.example.labbooking.Common.Common;
import com.example.labbooking.Common.SpaceItemDecoration;
import com.example.labbooking.Models.Lab.Lab;
import com.example.labbooking.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_lab)
    RecyclerView recycler_lab;

    private BroadcastReceiver labDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Lab> labArrayList = intent.getParcelableArrayListExtra(Common.KEY_LAB_LOAD_DONE);
            //Create adapter later
            MyLabAdapter adapter = new MyLabAdapter(getContext(), labArrayList);
            recycler_lab.setAdapter(adapter);

        }
    };
    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(labDoneReceiver, new IntentFilter(Common.KEY_LAB_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(labDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);

        unbinder = ButterKnife.bind(this, itemView);
        
        initView();
        
        return itemView;
    }

    private void initView() {
        recycler_lab.setHasFixedSize(true);
        recycler_lab.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_lab.addItemDecoration(new SpaceItemDecoration(4));
    }
}
