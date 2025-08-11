package com.example.musicdownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BottomSheetDownloadOption extends BottomSheetDialogFragment {

    private String songTitle;
    private String songUrl;
    private String coverUrl;

    public static BottomSheetDownloadOption newInstance(String title, String url, String coverUrl) {
        BottomSheetDownloadOption fragment = new BottomSheetDownloadOption();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        args.putString("coverUrl", coverUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_download_options, container, false);

        // استلام البيانات من الباندل
        if (getArguments() != null) {
            songTitle = getArguments().getString("title");
            songUrl = getArguments().getString("url");
            coverUrl = getArguments().getString("coverUrl");
        }

        // ربط عناصر الواجهة
        TextView tvSongTitle = view.findViewById(R.id.tvSongTitle);
        RadioGroup rgFormat = view.findViewById(R.id.rgFormat);
        Spinner spinnerQuality = view.findViewById(R.id.spinnerQuality);
        Button btnDownload = view.findViewById(R.id.btnDownload);

        tvSongTitle.setText("Choisir format et qualité pour: " + songTitle);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"128 kbps", "192 kbps", "320 kbps"});
        spinnerQuality.setAdapter(adapter);

        btnDownload.setOnClickListener(v -> {
            String format = (rgFormat.getCheckedRadioButtonId() == R.id.rbMp3) ? "MP3" : "MP4";
            String quality = spinnerQuality.getSelectedItem().toString();

            startDownload(songTitle, songUrl, coverUrl, format, quality);
            dismiss(); // إغلاق الـ bottom sheet بعد التحميل
        });

        return view;
    }

    private void startDownload(String title, String url, String coverUrl, String format, String quality) {
        SharedPreferences prefs = requireContext().getSharedPreferences("downloads", Context.MODE_PRIVATE);
        String json = prefs.getString("songs", "[]");

        try {
            JSONArray songArray = new JSONArray(json);

            JSONObject songObj = new JSONObject();
            songObj.put("title", title);
            songObj.put("url", url);
            songObj.put("coverUrl", coverUrl);
            songObj.put("format", format);
            songObj.put("quality", quality);

            songArray.put(songObj);

            prefs.edit().putString("songs", songArray.toString()).apply();

            Toast.makeText(getContext(), "Téléchargé avec succès", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show();
        }
    }
}