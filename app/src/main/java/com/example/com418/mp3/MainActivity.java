package com.example.com418.mp3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button btnPlay,btnPause,btnStop;
    MediaPlayer mPlayer;
    Boolean PAUSED=false;
    TextView tvMP3;
    SeekBar seekMP3;
    Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay=(Button)findViewById(R.id.palyBtn);
        btnPause=(Button)findViewById(R.id.stopBtn);
        btnStop=(Button)findViewById(R.id.RstopBtn);
        seekMP3 = (SeekBar) findViewById(R.id.seekMP3);
        switch1 = (Switch) findViewById(R.id.switch1);


        tvMP3=(TextView)findViewById(R.id.musicText);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song1);
                // mPlayer.prepare();
                String selectedMP3=mPlayer.toString();
                mPlayer.setLooping(true);
                mPlayer.start();
                btnPlay.setClickable(false);
                btnPause.setClickable(true);
                btnStop.setClickable(true);
                tvMP3.setText("실행중인 음악 :  " + selectedMP3);
                //pbMP3.setVisibility(View.VISIBLE);

            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PAUSED == false) {
                    mPlayer.pause();
                    btnPause.setText("이어듣기");
                    PAUSED = true;
                   // pbMP3.setVisibility(View.INVISIBLE);
                } else {
                    mPlayer.start();
                    makeThread();
                    PAUSED = false;
                    btnPause.setText("일시정지");
                    //pbMP3.setVisibility(View.VISIBLE);
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
                mPlayer.reset();
                btnPlay.setClickable(true);
                btnPause.setClickable(false);
            }
        });

        switch1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (switch1.isChecked()) {
                    mPlayer = MediaPlayer.create(getApplicationContext(),  R.raw.song1);
                    mPlayer.start();
                    makeThread();
                } else {
                   mPlayer.stop();
                }
            }
        });

        seekMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {        }

            public void onStartTrackingTouch(SeekBar seekBar) {      }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                }

            }
        });




    }
    void makeThread() {
        new Thread() {
            public void run() {
                // 음악이 계속 작동 중이라면
                while (mPlayer.isPlaying()) {
                    seekMP3.setMax(mPlayer.getDuration()); // 음악의 시간을 최대로 설정
                    seekMP3.setProgress(mPlayer.getCurrentPosition());
                    SystemClock.sleep(100);
                }
                //seekMP3.setProgress(0);
                //switch1.setChecked(false);  /// error
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch1.setChecked(false);  /// error
                    }
                });
            }
        }.start();
    }
    public void serviceStart(View v){

        Intent intent=new Intent(this,MusicService.class);
        startService(intent);
    }
}
