package com.example.labbooking.Interfaces;

import java.util.List;

public interface IAllBuildingsLoadListener {
    void onAllBuildingsLoadSuccess(List<String> areaNameList);
    void onAllBuildingsLoadFailure(String message);
}
