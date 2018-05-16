package com.yatty.sevenatenine.client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class BackgroundMusicService extends Service {
    private static final String TAG = BackgroundMusicService.class.getSimpleName();
    MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mMediaPlayer = MediaPlayer.create(this, R.raw.music_background);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(100, 100);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, BackgroundMusicService.class);
        return intent;
    }
    /*private static MediaPlayer sMediaPlayer;
    private static BackgroundMusicService instance;

    private BackgroundMusicService(Context context) {
        sMediaPlayer = MediaPlayer.create(context, R.raw.music_background);
        sMediaPlayer.setLooping(true);
    }

    public BackgroundMusicService() {
    }

    public static BackgroundMusicService getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundMusicService(context);
        }
        return instance;
    }

    public void start() {
        if (!sMediaPlayer.isPlaying()) {
            sMediaPlayer.start();
        }
//        mp.setVolume(volume, volume);
    }

    public void pause() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.pause();
        }
    }

    public void stop() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.pause();
        }
        sMediaPlayer.stop();
    }

    public void release() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.stop();
        }
        sMediaPlayer.release();
    }*/
}
