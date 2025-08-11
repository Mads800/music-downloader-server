package com.example.musicdownloader;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DownloadSettingsFragment extends Fragment {

    public DownloadSettingsFragment() {
        // Constructor فارغ مطلوب
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // إنشاء View بسيط مع TextView
        TextView textView = new TextView(requireContext());
        textView.setText("إعدادات التنزيل");
        textView.setTextSize(24);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }
}
