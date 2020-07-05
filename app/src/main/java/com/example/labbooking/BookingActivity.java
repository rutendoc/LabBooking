package com.example.labbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.example.labbooking.Adapters.MyViewPagerAdapter;
import com.example.labbooking.Common.Common;
import com.example.labbooking.Common.NonSwipeViewPager;
import com.example.labbooking.Models.Building;
import com.example.labbooking.Models.Lab.Lab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference buildingRef;

    @BindView(R.id.step_view)
    StepView step_view;
    @BindView(R.id.view_pager)
    NonSwipeViewPager view_pager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3 || Common.step > 0){
            Common.step--;
            view_pager.setCurrentItem(Common.step);
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if (Common.step < 3 || Common.step == 0){
            Common.step++; //increase
            //After you choose building
            if(Common.step == 1){
                if(Common.currentBuilding != null){
                    loadLabByBuilding(Common.currentBuilding.getBuildingID());
                }
                view_pager.setCurrentItem(Common.step);
            }
        }
    }

    private void loadLabByBuilding(String buildingID) {
        dialog.show();

        //Select all labs of building
        ///Campus/South/Building/6hdQtW512j4jKHCurXpc/Labs
        if(!TextUtils.isEmpty(Common.campus)){
            buildingRef = FirebaseFirestore.getInstance()
                    .collection("Campus")
                    .document(Common.campus)
                    .collection("Building")
                    .document(buildingID)
                    .collection("Labs");

            buildingRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Lab> labs = new ArrayList<>();
                            for(QueryDocumentSnapshot labSnapshot:task.getResult()){
                                Lab lab = labSnapshot.toObject(Lab.class);
                                lab.setLabID(labSnapshot.getId()); //get ID of Lab

                                labs.add(lab);
                            }
                            //Send Broadcast to BookingStep2Fragment to load Recycler
                            Intent intent = new Intent(Common.KEY_LAB_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_LAB_LOAD_DONE, labs);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
        }

    }


    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.currentBuilding = intent.getParcelableExtra(Common.KEY_BUILDING_STORE);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,
                new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        setupStepView();
        setColorButton();

        //View
        view_pager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        //We have 4 fragments so we need to keep the state of this 4 screen page
        view_pager.setOffscreenPageLimit(4);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //Show step
                step_view.go(position, true);
                if (position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.lightGrey);
        }
        else {
            btn_next_step.setBackgroundResource(R.color.darkGrey);
        }

        if (btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.lightGrey);
        }
        else {
            btn_previous_step.setBackgroundResource(R.color.darkGrey);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Building");
        stepList.add("Building");
        stepList.add("Time");
        stepList.add("Confirm");
        step_view.setSteps(stepList);
    }
}
