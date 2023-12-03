package com.example.loonietunes;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    MaterialButton btnPlay, btnNext, btnPrevious, btnFast, btnRew;
    TextView txtSong, txtSStart, txtSEnd;
    SeekBar seekMusicBar;
    BarVisualizer barVisualizer;
    ImageView imageView;
    String songName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    @Override
    protected void onDestroy(){
        if (barVisualizer != null)
        {
            barVisualizer.release();
        }
        super.onDestroy();
    }
    Thread updateSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPrevious=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        btnPlay=findViewById(R.id.btnPlay);
        btnFast=findViewById(R.id.btnFF);
        btnRew=findViewById(R.id.btnRewind);

        txtSong=findViewById(R.id.textSong);
        txtSStart=findViewById(R.id.txtSongStart);
        txtSEnd=findViewById(R.id.txtSongEnd);

        seekMusicBar=findViewById(R.id.seekBar);
        barVisualizer=findViewById(R.id.wave);

        imageView=findViewById(R.id.img2);

        if (mediaPlayer !=null){
            mediaPlayer.start();
            mediaPlayer.release();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList)bundle.getParcelableArrayList("songs");
        String sName = intent.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        txtSong.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        txtSong.setText(songName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekMusicBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }

            }

        };

        seekMusicBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusicBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        seekMusicBar.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        String endTime = creatTime(mediaPlayer.getDuration());
        txtSEnd.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = creatTime(mediaPlayer.getCurrentPosition());
                txtSStart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setBackgroundResource(R.drawable.pause_24);
                    mediaPlayer.start();

                    TranslateAnimation moveAnim = new TranslateAnimation(-25, 25, -25, 25);
                    moveAnim.setInterpolator(new AccelerateInterpolator());
                    moveAnim.setDuration(600);
                    moveAnim.setFillEnabled(true);
                    moveAnim.setFillAfter(true);
                    moveAnim.setRepeatMode(Animation.REVERSE);
                    moveAnim.setRepeatCount(1);
                    imageView.startAnimation(moveAnim);

                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnNext.performClick();
            }
        });

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1){
            barVisualizer.setAudioSessionId(audioSessionId);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position+1)%mySongs.size());
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName= mySongs.get(position).getName();
                txtSong.setText(songName);
                mediaPlayer.start();

                startAnimation(imageView, 360f);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):position-1;
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = mySongs.get(position).getName();
                txtSong.setText(songName);
                mediaPlayer.start();
                startAnimation(imageView, -360f);
            }
        });

        btnFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }

            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });
    }
    public void startAnimation(View view, Float degree){
        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(imageView, "rotation", 0f, degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public String creatTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time = time+min+":";
        if (sec<10){
            time+="0";
        }
        time+=sec;
        return time;

    }
}