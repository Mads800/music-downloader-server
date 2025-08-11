package com.example.musicdownloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DownloadSongAdapter extends RecyclerView.Adapter<DownloadSongAdapter.SongViewHolder> {

    public interface OnSongClickListener {
        void onSongClick(Song song);
        void onRenameSong(Song song);
        void onDeleteSong(Song song);
    }

    private List<Song> songs;
    private OnSongClickListener listener;

    public DownloadSongAdapter(List<Song> songs, OnSongClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_downloaded_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvSongName.setText(song.getTitle());

        // دوران الديسك
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(4000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        holder.imgDisk.startAnimation(rotate);

        // تحميل صورة الغلاف داخل الديسك
        Glide.with(holder.imgSongCover.getContext())
                .load(song.getCoverUrl()) // لازم تضيف coverUrl في كلاس Song
                .placeholder(R.drawable.song_cover_placeholder)
                .circleCrop()
                .into(holder.imgSongCover);

        // كليك على العنصر لتشغيل الأغنية
        holder.itemView.setOnClickListener(v -> listener.onSongClick(song));

        // كليك على الثلاث نقاط لفتح PopupMenu
        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnMore);
            popup.getMenu().add("Renommer");
            popup.getMenu().add("Supprimer");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Renommer")) {
                    listener.onRenameSong(song);
                } else if (item.getTitle().equals("Supprimer")) {
                    listener.onDeleteSong(song);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDisk, imgSongCover, btnMore;
        TextView tvSongName;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDisk = itemView.findViewById(R.id.imgDisk);
            imgSongCover = itemView.findViewById(R.id.imgSongCover);
            btnMore = itemView.findViewById(R.id.btnMore);
            tvSongName = itemView.findViewById(R.id.tvSongName);
        }
    }
}