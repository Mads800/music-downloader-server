package com.example.musicdownloader;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SongFragment extends Fragment {

    private Song currentSong;

    public SongFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        // عناصر الواجهة
        TextView tvAlbum = view.findViewById(R.id.tvAlbum);
        TextView tvSongTitle = view.findViewById(R.id.tvSongTitle);
        Button btnPlay = view.findViewById(R.id.btnPlaySong);

        // استقبال بيانات الأغنية
        if (getArguments() != null) {
            currentSong = (Song) getArguments().getSerializable("selected_song");
            if (currentSong != null) {
                tvSongTitle.setText(currentSong.getTitle());
            } else {
                tvSongTitle.setText("لا توجد أغنية محددة");
            }
        }

        // تأثير نيون على النص
        tvAlbum.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        int[] neonColors = {
                Color.parseColor("#FF00D4"),
                Color.parseColor("#00F0FF"),
                Color.parseColor("#39FF14"),
                Color.parseColor("#FFEA00")
        };

        ValueAnimator colorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                neonColors[0], neonColors[1], neonColors[2], neonColors[3], neonColors[0]
        );
        colorAnimator.setDuration(4000);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.RESTART);

        colorAnimator.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            tvAlbum.setShadowLayer(25f, 0f, 0f, animatedColor);
            tvAlbum.setTextColor(animatedColor);
        });

        colorAnimator.start();

        // زر تشغيل الأغنية
        btnPlay.setOnClickListener(v -> playSong());

        return view;
    }

    private void playSong() {
        if (currentSong != null) {
            // هنا تحط الكود اللي يشغل الأغنية أو يفتح مشغل
            // مثال: تشغيل أغنية محلياً أو أونلاين
            // حالياً هنحط Log كاختبار
            System.out.println("تشغيل الأغنية: " + currentSong.getTitle());
        } else {
            System.out.println("لا توجد أغنية للتشغيل.");
        }
    }
}
