package com.example.song;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

private SongViewModel songViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(20);
//        recyclerView.setDrawingCacheEnabled(true);
//        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        SongListAdapter adapter = new SongListAdapter(getApplicationContext());

        songViewModel.getSongList().observe(this, new Observer<ArrayList<Song>>() {
            @Override
            public void onChanged(ArrayList<Song> songs) {
                //adapter.setSongList(songs);
                adapter.setSongList(songs);
                recyclerView.setAdapter(adapter);
            }
        });

    }





}