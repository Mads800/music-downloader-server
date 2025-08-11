package com.example.musicdownloader;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        FragmentSearchResultat.OnSearchListener,
        FragmentSearchResultat.OnSongSelectedListener {

    private static final String TAG = "MainActivity";

    // ✅ إعلان Interstitial (اختبره أولًا قبل وضع ID الحقيقي)
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; // تجريبي
    // ✅ إعلان Banner (اختبره أولًا قبل وضع ID الحقيقي)
    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"; // تجريبي

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    private FragmentSearchResultat fragmentSearchResultat;
    private BottomSheetPlayer bottomSheetPlayer;

    // لتخزين الأغنية التي نريد تشغيلها بعد الإعلان
    private Song pendingSong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdMob();
        setupBottomNavigation();

        // إنشاء Fragment البحث وضبط المستمعين
        fragmentSearchResultat = new FragmentSearchResultat();
        fragmentSearchResultat.setOnSearchListener(this);
        fragmentSearchResultat.setOnSongSelectedListener(this);

        // عرض Fragment داخل الحاوية
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentSearchResultat)
                .commit();

        loadInterstitialAd();
    }

    private void initAdMob() {
        MobileAds.initialize(this, initializationStatus -> {});
        adView = findViewById(R.id.adView);
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.fragment_home) {
                // عرض FragmentSearchResultat - الصفحة الرئيسية
                fragmentSearchResultat = new FragmentSearchResultat();
                fragmentSearchResultat.setOnSearchListener(this);
                fragmentSearchResultat.setOnSongSelectedListener(this);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentSearchResultat)
                        .commit();
                return true;

            } else if (id == R.id.fragment_songs) {
                SongFragment songFragment = new SongFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, songFragment)
                        .commit();
                return true;

            } else if (id == R.id.fragment_album) {
                AlbumFragment albumFragment = new AlbumFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, albumFragment)
                        .commit();
                return true;

            } else if (id == R.id.fragment_setting) {
                SettingFragment settingFragment = new SettingFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, settingFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }



        @Override
    public void onSearch(String query) {
        performSearch(query);
    }

    @Override
    public void onSongSelected(Song song) {
        // عرض SongFragment مع تمرير بيانات الأغنية
        SongFragment songFragment = new SongFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_song", song);  // تأكد أن Song implements Serializable
        songFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, songFragment)
                .addToBackStack(null) // يسمح بالرجوع للشاشة السابقة بالزر رجوع
                .commit();
    }

    private void performSearch(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchSongs(query).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    fragmentSearchResultat.updateSearchResults(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "لا توجد نتائج", Toast.LENGTH_SHORT).show();
                    fragmentSearchResultat.updateSearchResults(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "خطأ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                fragmentSearchResultat.updateSearchResults(new ArrayList<>());
            }
        });
    }

    private void openBottomSheetPlayer(Song song) {
        if (song != null) {
            bottomSheetPlayer = BottomSheetPlayer.newInstance(
                    song.getTitle(),
                    song.getAudioUrl(),
                    song.getCoverUrl()
            );
            bottomSheetPlayer.show(getSupportFragmentManager(), "BottomSheetPlayer");
        }
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.d(TAG, "Interstitial Ad Loaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull com.google.android.gms.ads.LoadAdError loadAdError) {
                mInterstitialAd = null;
                Log.d(TAG, "Failed to load Interstitial Ad: " + loadAdError.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public BottomSheetPlayer getBottomSheetPlayer() {
        return bottomSheetPlayer;
    }
}