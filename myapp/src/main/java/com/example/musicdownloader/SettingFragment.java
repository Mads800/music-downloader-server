package com.example.musicdownloader;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// إضافة استيراد DownloadSettingsFragment لحل مشكلة cannot resolve symbol
import com.example.musicdownloader.DownloadSettingsFragment;

public class SettingFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // ربط XML
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // عناصر الواجهة
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        Switch switchSound = view.findViewById(R.id.switchSound);
        Switch switchTheme = view.findViewById(R.id.switchTheme);
        Button btnDownloadSettings = view.findViewById(R.id.btnDownloadSettings);
        Button btnAbout = view.findViewById(R.id.btnAbout);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        // أزرار المشاركة والتقييم الجديدة
        Button btnShareApp = view.findViewById(R.id.btnShareApp);
        Button btnRateApp = view.findViewById(R.id.btnRateApp);

        // تهيئة SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // استرجاع حالة السويتشات عند الفتح
        switchSound.setChecked(sharedPreferences.getBoolean("sound_enabled", true));
        switchTheme.setChecked(sharedPreferences.getBoolean("dark_mode", false));

        // تأثير النيون على العنوان
        tvTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
            tvTitle.setShadowLayer(25f, 0f, 0f, animatedColor);
            tvTitle.setTextColor(animatedColor);
        });

        colorAnimator.start();

        // تفعيل/تعطيل الصوت
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("sound_enabled", isChecked).apply();
            Toast.makeText(getContext(), isChecked ? "الصوت مفعّل" : "الصوت معطّل", Toast.LENGTH_SHORT).show();
        });

        // تبديل الثيم بين داكن وفاتح
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("dark_mode", isChecked).apply();
            Toast.makeText(getContext(), isChecked ? "الوضع الداكن مفعل" : "الوضع الفاتح مفعل", Toast.LENGTH_SHORT).show();
            // هنا ممكن تضيف كود لتغيير الثيم فعلاً
        });

        // زر إعدادات التنزيل (يفتح Fragment جديد)
        btnDownloadSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "فتح إعدادات التنزيل", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DownloadSettingsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // زر معلومات التطبيق (Dialog)
        btnAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("معلومات التطبيق")
                    .setMessage("MusicDownloader v1.0\nتطبيق لتنزيل وتشغيل الموسيقى بسهولة.")
                    .setPositiveButton("حسناً", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // زر تسجيل الخروج (مسح البيانات وفتح شاشة تسجيل الدخول)
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
            editor.clear().apply();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // زر مشاركة التطبيق
        btnShareApp.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = "جرّب تطبيق MusicDownloader لتنزيل وتشغيل الموسيقى بسهولة! حمله الآن من المتجر.";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "مشاركة عبر"));
        });

        // زر تقييم التطبيق (يفتح رابط متجر Google Play)
        btnRateApp.setOnClickListener(v -> {
            String packageName = requireContext().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        });

        return view;
    }
}
