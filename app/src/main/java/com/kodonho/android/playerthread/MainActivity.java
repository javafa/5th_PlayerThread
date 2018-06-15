package com.kodonho.android.playerthread;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    TextView textView, textCurrent, textDuration;
    SeekBar seekBar;
    ImageButton imageButton;
    MediaPlayer mediaPlayer;
    SeekbarThread thread;

    public static final int STOP = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    int playerStatus = STOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textCurrent = findViewById(R.id.textCurrent);
        textDuration = findViewById(R.id.textDuration);
        seekBar = findViewById(R.id.seekBar);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerStatus == STOP || playerStatus == PAUSE){
                    mediaPlayer.start();
                    thread.start();
                    playerStatus = PLAY;
                    imageButton.setImageResource(android.R.drawable.ic_media_pause);
                }else if( playerStatus == PLAY){
                    mediaPlayer.pause();
                    playerStatus = PAUSE;
                    imageButton.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });

        // 노래제목
        textView.setText("삐딱하게 GD");
        // 플레이어에 노래 세팅
        mediaPlayer = MediaPlayer.create(this, R.raw.a);
        // 노래의 길이를 seekbar 에 세팅
        seekBar.setMax(mediaPlayer.getDuration());
        // 노래의 길이을 텍스트 뷰에도 세팅
        setDuration(mediaPlayer.getDuration());
        playerStatus = STOP;
        // Seekbar 스레드를 초기화
        thread = new SeekbarThread();
    }

    private void setDuration(int position){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        textDuration.setText(sdf.format(position));
    }

    private void setCurrent(int position){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        textCurrent.setText(sdf.format(position));
    }

    public void setSeekBar(final int position){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(position);
                setCurrent(position);
            }
        });
    }

    // 1초에 한번씩 미디어 플레이어를 체크해서 현재 위치값을 가져오고
    // Seekbar에 세팅하는 스레드
    class SeekbarThread extends Thread {
        boolean runFlag = true;
        public void run(){
            while(runFlag){
                try {
                    // 미디어 플레이어의 현재 위치
                    int position = mediaPlayer.getCurrentPosition();
                    setSeekBar(position);
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
