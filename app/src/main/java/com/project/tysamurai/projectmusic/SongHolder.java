package com.project.tysamurai.projectmusic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tanay on 8/4/17.
 */

public class SongHolder extends RecyclerView.ViewHolder {
    TextView artist,title;

    public SongHolder(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.artist = (TextView) itemView.findViewById(R.id.artist);
    }
}
