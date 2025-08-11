package com.example.musicdownloader;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String audioUrl;
    private String coverUrl;

    // Constructor
    public Song(String title, String audioUrl, String coverUrl) {
        this.title = title;
        this.audioUrl = audioUrl;
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    // ✅ لتفادي أخطاء في بعض الأماكن مثل FragmentSearchResultat
    public String getUrl() {
        return audioUrl;
    }
}
