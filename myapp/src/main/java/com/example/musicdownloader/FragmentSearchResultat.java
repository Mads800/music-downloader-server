package com.example.musicdownloader;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSearchResultat extends Fragment implements SearchResultatAdapter.OnSearchResultClickListener {

    private RecyclerView recyclerView;
    private SearchResultatAdapter adapter;
    private List<Song> searchResults = new ArrayList<>();
    private EditText etSearch;

    public interface OnSearchListener {
        void onSearch(String query);
    }

    public interface OnSongSelectedListener {
        void onSongSelected(Song song);
    }

    private OnSearchListener onSearchListener;
    private OnSongSelectedListener onSongSelectedListener;

    public void setOnSearchListener(OnSearchListener listener) {
        this.onSearchListener = listener;
    }

    public void setOnSongSelectedListener(OnSongSelectedListener listener) {
        this.onSongSelectedListener = listener;
    }

    public void updateSearchResults(List<Song> newResults) {
        if (adapter != null) {
            adapter.updateList(newResults);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        etSearch = view.findViewById(R.id.etSearch);
        recyclerView = view.findViewById(R.id.rvSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SearchResultatAdapter(searchResults, this);
        recyclerView.setAdapter(adapter);

        etSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchSongs(query); // 📌 الآن البحث يتم مباشرة من هنا
                }
                return true;
            }
            return false;
        });

        return view;
    }

    private void searchSongs(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchSongs(query).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.clear();
                    searchResults.addAll(response.body());
                    adapter.updateList(searchResults);
                } else {
                    Toast.makeText(getContext(), "لا توجد نتائج", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(getContext(), "خطأ في الاتصال بالسيرفر", Toast.LENGTH_SHORT).show();
                Log.e("SearchError", t.getMessage(), t);
            }
        });
    }

    @Override
    public void onDownloadClick(Song song) {
        BottomSheetPlayer player = BottomSheetPlayer.newInstance(
                song.getTitle(),
                song.getUrl(),
                song.getCoverUrl()
        );
        player.show(requireActivity().getSupportFragmentManager(), "PlayerBottomSheet");
    }

    @Override
    public void onMoreOptionsClick(Song song) {
        BottomSheetDownloadOption options = BottomSheetDownloadOption.newInstance(
                song.getTitle(),
                song.getUrl(),
                song.getCoverUrl()
        );
        options.show(requireActivity().getSupportFragmentManager(), "DownloadOptions");
    }

    @Override
    public void onItemClick(Song song) {
        if (onSongSelectedListener != null) {
            onSongSelectedListener.onSongSelected(song);
        }
    }
}
