package com.yatty.sevenatenine.client;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class PreferenceFragment extends android.preference.PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = PreferenceFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        EditTextPreference editTextPreference = (EditTextPreference) findPreference(getResources()
                .getString(R.string.key_server_ip));

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: SharedPreferences key: " + key);
        boolean musicEnabled = ApplicationSettings.isMusicEnabled(getActivity().getApplicationContext());
        if (musicEnabled) {
            Log.i(TAG, "Music enabled");
            getActivity().startService(BackgroundMusicService.getIntent(getActivity().getApplicationContext()));
        } else {
            Log.i(TAG, "Music disabled");
            getActivity().stopService(BackgroundMusicService.getIntent(getActivity().getApplicationContext()));
        }
        Drawable background = ApplicationSettings.getBackgroundPicture(getActivity().getApplicationContext());
        View rootView = getActivity().findViewById(android.R.id.content);
        rootView.setBackground(background);
    }
}