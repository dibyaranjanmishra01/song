package com.example.song;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongRepository {
    private MutableLiveData<ArrayList<Song>> songList;
    private Context context;

    public SongRepository(Application application) {
        this.songList  = new MutableLiveData<>();
        this.context = application;
    }

    public void refreshData(){
        GetSongs asyncTask = new GetSongs(context);
        if(asyncTask.getStatus() != AsyncTask.Status.RUNNING) asyncTask.execute();
        else if(asyncTask.getStatus() == AsyncTask.Status.FINISHED)
        {
            Toast.makeText(context, "music loaded", Toast.LENGTH_SHORT).show();
        }
    }

    public MutableLiveData<ArrayList<Song>> getSongList()
    {
        if(songList==null) refreshData();
        return songList;
    }

    class GetSongs extends AsyncTask<Void,Void,ArrayList<Song>>
    {
        Context context;
        public GetSongs(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            return getSongList(context);
        }

        @Override
        protected void onPostExecute(ArrayList<Song> strings) {
            super.onPostExecute(strings);
            songList.setValue(strings);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

    public ArrayList<Song> getSongList(Context context)
    {
        /*
         * All the audio files can be accessed using the below initialised musicUri.
         * And there is a cursor to iterate over each and every column.
         */
        ContentResolver contentResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null, null);
        ArrayList<Song> songArrayList = new ArrayList<Song>();
        // If cursor is not null
        if(musicCursor != null && musicCursor.moveToFirst())
        {
            //get Columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int albumIDColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int pathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            // Store the title, id and artist name in Song Array list.
            do
            {
                Song song = new Song();
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                String duration = musicCursor.getString(durationColumn);
                String albumID = musicCursor.getString(albumIDColumn);
                String path = musicCursor.getString(pathColumn);
                song.setTitle(title);
                song.setArtist(artist);
                song.setDuration(duration);
                song.setAlbum_id(albumID);
                song.setPath(path);
                // Add the info to our array.
                songArrayList.add(song);
            }
            while (musicCursor.moveToNext());

            // For best practices, close the cursor after use.
            musicCursor.close();
        }
        return songArrayList;
    }

}
