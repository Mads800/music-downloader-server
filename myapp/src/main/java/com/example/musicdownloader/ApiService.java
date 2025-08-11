package com.example.musicdownloader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // طلب بحث فيديوهات/أغاني عبر السيرفر
    @GET("search")
    Call<List<Song>> searchSongs(@Query("q") String query);

    // طلب رابط تحميل فيديو/أغنية حسب معرف الفيديو
    @GET("download")
    Call<DownloadResponse> downloadVideo(@Query("videoId") String videoId);
}
