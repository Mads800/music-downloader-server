package com.example.musicdownloader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetPlayer extends BottomSheetDialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_URL = "url";
    private static final String ARG_COVER = "coverUrl";

    private String title;
    private String url;
    private String coverUrl;

    private TextView tvTitle;
    private ImageView imgCover;

    public static BottomSheetPlayer newInstance(String title, String url, String coverUrl) {
        BottomSheetPlayer fragment = new BottomSheetPlayer();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_URL, url);
        args.putString(ARG_COVER, coverUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_player, container, false);

        // استلام البيانات
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            url = getArguments().getString(ARG_URL);
            coverUrl = getArguments().getString(ARG_COVER);
        }

        // ربط العناصر من الواجهة
        tvTitle = view.findViewById(R.id.tvSongTitle);
        imgCover = view.findViewById(R.id.imgSongCover);
        ImageView imgDisk = view.findViewById(R.id.imgDisk);
        Button btnDownload = view.findViewById(R.id.btnDownload);

        tvTitle.setText(title);
        Glide.with(requireContext()).load(coverUrl).into(imgCover);

        // دوران القرص
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(4000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        imgDisk.startAnimation(rotate);

        // زر تحميل
        btnDownload.setOnClickListener(v -> {
            BottomSheetDownloadOption downloadSheet = BottomSheetDownloadOption.newInstance(title, url, coverUrl);
            downloadSheet.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "DownloadOptions");
        });

        return view;
    }

    // توسيع BottomSheet تلقائياً عند الفتح
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            if (parent != null) {
                BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    // تغيير البيانات عند تشغيل أغنية جديدة
    public void playSong(String url, String title, String coverUrl) {
        this.url = url;
        this.title = title;
        this.coverUrl = coverUrl;

        if (getView() != null) {
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
            if (imgCover != null) {
                Glide.with(requireContext()).load(coverUrl).into(imgCover);
            }
        }
    }

    // لتحديث نتائج البحث لو أردت ذلك
    public void updateSearchResults(List<Song> results) {
        Log.d("BottomSheetPlayer", "تحديث نتائج البحث: " + results.size());
        // يمكنك إضافة RecyclerView أو عرض النتائج هنا
    }
}
