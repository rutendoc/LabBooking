package com.example.labbooking.Interfaces;

import com.example.labbooking.Models.Banner;

import java.util.List;

public interface ILookBookLoadListener {
    void onLookBookLoadSuccess(List<Banner> lookBooks);
    void onLookBookLoadFailed(String message);

}
