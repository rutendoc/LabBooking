package com.example.labbooking.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labbooking.Adapters.MyBuildingAdapter;
import com.example.labbooking.Common.Common;
import com.example.labbooking.Common.SpaceItemDecoration;
import com.example.labbooking.Interfaces.IAllBuildingsLoadListener;
import com.example.labbooking.Interfaces.IBuildingLoadListener;
import com.example.labbooking.Models.Building;
import com.example.labbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllBuildingsLoadListener, IBuildingLoadListener {

    //Variables
    CollectionReference allBuildingsRef;
    CollectionReference buildingRef;

    IAllBuildingsLoadListener iAllBuildingsLoadListener;
    IBuildingLoadListener iBuildingLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_building)
    RecyclerView recycler_building;

    Unbinder unbinder;

    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allBuildingsRef = FirebaseFirestore.getInstance().collection("Campus");
        iAllBuildingsLoadListener = this;
        iBuildingLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        initView();
        loadAllBuildings();

        return itemView;
    }

    private void initView() {
        recycler_building.setHasFixedSize(true);
        recycler_building.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_building.addItemDecoration(new SpaceItemDecoration(4));
    }

    private void loadAllBuildings() {
        allBuildingsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            list.add("Please choose city");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllBuildingsLoadListener.onAllBuildingsLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllBuildingsLoadListener.onAllBuildingsLoadFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onAllBuildingsLoadSuccess(List<String> areaNameList) {

        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                    loadBuildingOfCampus(item.toString());
                }
                else
                    recycler_building.setVisibility(View.GONE);
            }
        });
    }

    private void loadBuildingOfCampus(String campusName) {
        dialog.show();

        Common.campus = campusName;

        buildingRef = FirebaseFirestore.getInstance()
                .collection("Campus")
                .document(campusName)
                .collection("Building");

        buildingRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Building> list = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Building building = documentSnapshot.toObject(Building.class);
                        building.setBuildingID(documentSnapshot.getId());
                        list.add(building);
                    }

                    iBuildingLoadListener.onBuildingLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBuildingLoadListener.onBuildingLoadFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onAllBuildingsLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBuildingLoadSuccess(List<Building> buildingList) {
        MyBuildingAdapter adapter = new MyBuildingAdapter(getActivity(), buildingList);
        recycler_building.setAdapter(adapter);
        recycler_building.setVisibility(View.GONE);

        dialog.dismiss();
    }

    @Override
    public void onBuildingLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
