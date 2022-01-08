package com.example.song;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SongListAdapter extends RecyclerView.Adapter<SongViewHolder>{

    ArrayList<Song> songList;
    Context context;

    final public static Uri artworkUri = Uri.parse("content://media/external/audio/albumart");

    public SongListAdapter(Context context) {
        this.context = context;
    }

    public void setSongList(ArrayList<Song> songList)
    {
        this.songList = songList;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_alter,parent,false);
        return new SongViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        new LoadBitmap(holder.albumArt,context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,song.getPath());
        String songTitle = (song.getTitle()!=null)?song.getTitle():"NULL";
        holder.title.setText(songTitle);
        holder.artist.setText(song.getArtist());
        holder.duration.setText(song.getDuration());

        //Glide.with(context).load(R.drawable.ic_launcher_background).placeholder(R.color.purple_200).into(holder.albumArt);
    }

    @Override
    public int getItemCount() {
        if(songList == null) return 0;
        return songList.size();
    }
}

class SongViewHolder extends RecyclerView.ViewHolder {
    TextView title,artist,duration;
    ImageView albumArt;
    Context context;
    public SongViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        artist = (TextView) itemView.findViewById(R.id.artist);
        duration = (TextView) itemView.findViewById(R.id.duration);
        albumArt = (ImageView) itemView.findViewById(R.id.album_art);
        this.context = context;
    }
    public void setAlbumArt(String path)
    {

    }
}

class LoadBitmap extends AsyncTask<String,Void,byte[]>
{

    private final ImageView albumart;
    private final Context context;

    public LoadBitmap(ImageView albumArt,Context context) {
        this.albumart = albumArt;
        this.context = context;
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] result = null;
        try{
            mmr.setDataSource(strings[0]);

            result = mmr.getEmbeddedPicture();
            mmr.release();
        }catch(Exception ignored){}
        return result;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        Glide.with(context).load(bytes).placeholder(R.color.purple_200).into(albumart);
    }
}



