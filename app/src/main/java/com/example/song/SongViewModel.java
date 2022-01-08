package com.example.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongViewModel extends AndroidViewModel {
    private SongRepository repository;

    public SongViewModel(@NonNull Application application) {
        super(application);
        repository = new SongRepository(application);
        repository.refreshData();
    }

    public MutableLiveData<ArrayList<Song>> getSongList()
    {
        return repository.getSongList();
    }
}
