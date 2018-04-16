package com.yatty.sevenatenine.client;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusicService {
    private static final String TAG = "MusicManager";
    private static MediaPlayer sMediaPlayer;
    private static BackgroundMusicService instance;

    private BackgroundMusicService(Context context) {
        sMediaPlayer = MediaPlayer.create(context, R.raw.background_sound);
        sMediaPlayer.setLooping(true);
    }

    public static BackgroundMusicService getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundMusicService(context);
        }
        return instance;
    }

    public static BackgroundMusicService getInstance() {
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

//    public static void updateVolumeFromPrefs(Context context, float volume) {
//        sMediaPlayer.setVolume(volume, volume);
//    }

    public void release() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.stop();
        }
        sMediaPlayer.release();
    }
}
