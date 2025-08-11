package com.example.musicdownloader;

import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdManager {

    public static void initializeAds(Context context) {
        MobileAds.initialize(context, initializationStatus -> {});
    }

    public static void loadBannerAd(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
