package com.example.labbooking.Common;

import com.example.labbooking.Models.Building;
import com.example.labbooking.Models.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_BUILDING_STORE = "BUILDING_SAVE";
    public static final String KEY_LAB_LOAD_DONE = "LAB_LOAD_DONE";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Building currentBuilding;
    public static int step = 0; //Init 1st step as 0
    public static String campus = "";
}
