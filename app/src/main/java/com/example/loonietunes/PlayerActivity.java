package com.example.loonietunes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.material.button.MaterialButton;

public class PlayerActivity extends AppCompatActivity {

    MaterialButton btnPlay, btnNext, btnPrevious, btnBack, btnFast, btnRew;
    TextView txtSong, txtSongStart, txtSongEnd;
    SeekBar seekMusicBar;
    BarVisualizer barVisualizer;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }
}