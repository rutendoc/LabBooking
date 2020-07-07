package com.example.labbooking.Interfaces;

import com.example.labbooking.Models.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);

}
