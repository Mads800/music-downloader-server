package com.example.musicdownloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchResultatAdapter extends RecyclerView.Adapter<SearchResultatAdapter.SearchViewHolder> {

    public interface OnSearchResultClickListener {
        void onItemClick(Song song);
        void onDownloadClick(Song song);
        void onMoreOptionsClick(Song song);
    }

    private final List<Song> songs;
    private final OnSearchResultClickListener listener;

    public SearchResultatAdapter(List<Song> songs, OnSearchResultClickListener listener) {
        // تم إزالة عمل نسخة جديدة للقائمة، الآن نستخدم القائمة التي تمرر لنا مباشرةً
        this.songs = songs != null ? songs : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_resultat, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Song song = songs.get(position);
        if (song == null) return;

        holder.tvSongName.setText(song.getTitle());

        Glide.with(holder.imgSongCover.getContext())
                .load(song.getCoverUrl())
                .placeholder(R.drawable.song_cover_placeholder)
                .circleCrop()
                .into(holder.imgSongCover);

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(4000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        holder.imgDisk.startAnimation(rotate);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(song);
        });

        holder.btnDownload.setOnClickListener(v -> {
            if (listener != null) listener.onDownloadClick(song);
        });

        holder.btnMore.setOnClickListener(v -> {
            if (listener != null) listener.onMoreOptionsClick(song);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    // هذه الدالة لتحديث البيانات
    public void updateList(List<Song> newList) {
        songs.clear();
        if (newList != null && !newList.isEmpty()) {
            songs.addAll(newList);
        }
        notifyDataSetChanged();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDisk, imgSongCover, btnMore, btnDownload;
        TextView tvSongName;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDisk = itemView.findViewById(R.id.imgDisk);
            imgSongCover = itemView.findViewById(R.id.imgSongCover);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            tvSongName = itemView.findViewById(R.id.tvSongName);
        }
    }
}
