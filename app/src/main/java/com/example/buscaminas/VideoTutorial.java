package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

public class VideoTutorial extends AppCompatActivity {
    private VideoView videoView;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_tutorial);
        videoView =(VideoView) findViewById(R.id.videoView2);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.setVideoURI(Uri.parse(("android.resource://" + getPackageName() + "/" + R.raw.videoplayback)));
        videoView.start();
    }
}