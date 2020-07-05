package com.example.labbooking.Interfaces;

import com.example.labbooking.Models.Building;

import java.util.List;

public interface IBuildingLoadListener {
    void onBuildingLoadSuccess(List<Building> buildingList);
    void onBuildingLoadFailure(String message);
}
