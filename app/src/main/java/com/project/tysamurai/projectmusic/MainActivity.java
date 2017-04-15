package com.project.tysamurai.projectmusic;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;


    SongRVAdaptor adaptor;
    RecyclerView songview;
    ArrayList<Songs> songlist;


    // OnCreate -------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songlist=new ArrayList<>();
        getSongList();

        songview=(RecyclerView) findViewById(R.id.song_list);

        songview.setLayoutManager(new LinearLayoutManager(this));

        adaptor=new SongRVAdaptor();
        songview.setAdapter(adaptor);

        setController();

    }

    //--------------------------------------------------------------------


    @Override
    protected void onStart() {
        super.onStart();

        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    // connect to the service--------------------------------------------------------
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songlist);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
// --------------------------------------------------------------------------------



    public void getSongList(){
        ContentResolver musicResolver=getContentResolver();
        Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor=musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songlist.add(new Songs(thisId,thisTitle,thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng()){
            return musicSrv.getDur();
        }
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng()){
            return musicSrv.getPosn();
        }
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound){
            return musicSrv.isPng();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    private void setController(){
        //set the controller up
        if(controller==null)
        controller = new MusicController(this);

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.musicControls));
        controller.setEnabled(true);
    }

    //play next song
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //play previous song
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

//--------------------------------------------------------------------------------------------------
    // RecyclerView class --------------------------------------------------------------------------
    class SongRVAdaptor extends RecyclerView.Adapter<SongHolder>{
        @Override
        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li=getLayoutInflater();
            View itemview=li.inflate(R.layout.songelement,null,false);

            return new SongHolder(itemview);
        }

        @Override
        public void onBindViewHolder(SongHolder holder, final int position) {

            String displayTitle=songlist.get(position).getTitle();
            String displayArtist=songlist.get(position).getArtist();

            if(displayTitle.length()>30){
                displayTitle=displayTitle.substring(0,31) + "...";
            }
            if(displayArtist.length()>30){
                displayArtist=displayArtist.substring(0,31) + "...";
            }
            holder.title.setText(displayTitle);
            holder.artist.setText(displayArtist);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicSrv.setSong(position);
                    musicSrv.playSong();
                    if(playbackPaused){
                        setController();
                        playbackPaused=false;
                    }
                    controller.hide();
                    controller.show();
                    if(musicSrv!=null && musicSrv.isPng()) {
                        musicSrv.seek(0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return songlist.size();
        }
    }

    //-----------------------------------------------------------------------------
}