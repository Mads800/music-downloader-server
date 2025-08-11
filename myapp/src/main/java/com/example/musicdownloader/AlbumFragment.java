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
import android.widget.TextView;

public class AlbumFragment extends Fragment {

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // انفليت للواجهة
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        // إيجاد TextView
        TextView tvAlbum = view.findViewById(R.id.tvAlbum);

        // تفعيل البلور عشان الشادو يشتغل
        tvAlbum.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // عمل أنيميشن ألوان نيون متغيرة باستمرار
        int[] neonColors = {
                Color.parseColor("#FF00D4"), // وردي
                Color.parseColor("#00F0FF"), // سماوي
                Color.parseColor("#39FF14"), // أخضر نيون
                Color.parseColor("#FFEA00")  // أصفر نيون
        };

        ValueAnimator colorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                neonColors[0], neonColors[1], neonColors[2], neonColors[3], neonColors[0]
        );
        colorAnimator.setDuration(4000); // 4 ثواني
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.RESTART);

        colorAnimator.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            tvAlbum.setShadowLayer(25f, 0f, 0f, animatedColor); // Glow متغير اللون
            tvAlbum.setTextColor(animatedColor); // يخلي النص نفسه يتغير
        });

        colorAnimator.start();

        return view;
    }
}