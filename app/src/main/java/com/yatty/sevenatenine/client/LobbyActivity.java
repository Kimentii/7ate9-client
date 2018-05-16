package com.yatty.sevenatenine.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yatty.sevenatenine.api.commands_with_data.PlayerInfo;
import com.yatty.sevenatenine.api.in_commands.GameStartedNotification;
import com.yatty.sevenatenine.api.in_commands.LobbyStateChangedNotification;
import com.yatty.sevenatenine.api.out_commands.LeaveLobbyRequest;
import com.yatty.sevenatenine.client.auth.SessionInfo;
import com.yatty.sevenatenine.client.network.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyActivity extends AppCompatActivity {
    private static final String TAG = LobbyActivity.class.getSimpleName();
//    private static final String EXTRA_PRIVATE_LOBBY_INFO = "private_lobby_info";
//    private static final String EXTRA_LOBBY_ID = "lobby_id";

    private TextView mPlayersNumberTextView;
    private ListView mLobbyPlayersListView;
    private boolean mShouldMusicStay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        mPlayersNumberTextView = findViewById(R.id.tv_player_rating);
        mLobbyPlayersListView = findViewById(R.id.lv_lobby_players);
        LobbyActivityHandler lobbyActivityHandler = new LobbyActivityHandler();
        NetworkService.setHandler(lobbyActivityHandler);
        Log.d(TAG, "Lobby players:");
        for (PlayerInfo playerInfo : SessionInfo.getPrivateLobbyInfo().getPlayers()) {
            Log.d(TAG, playerInfo.getPlayerId());
        }
        mLobbyPlayersListView.setAdapter(new LobbyPlayersListAdapter(this, SessionInfo
                .getPrivateLobbyInfo().getPlayers()));
        updatePlayersListUi(SessionInfo.getPrivateLobbyInfo().getPlayers());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mShouldMusicStay) {
            stopService(BackgroundMusicService.getIntent(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View rootView = findViewById(android.R.id.content);
        rootView.setBackground(ApplicationSettings.getBackgroundPicture(this));
        if (ApplicationSettings.isMusicEnabled(this)) {
            startService(BackgroundMusicService.getIntent(this));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leave lobby")
                .setMessage("Do you really want to leave lobby?")
                //android.R.string.yes
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LeaveLobbyRequest leaveLobbyRequest = new LeaveLobbyRequest();
                        leaveLobbyRequest.setAuthToken(SessionInfo.getAuthToken());
                        leaveLobbyRequest.setLobbyId(SessionInfo.getGameId());

                        startService(NetworkService.getSendIntent(getApplicationContext(),
                                leaveLobbyRequest, true));

                        Context context = getApplicationContext();
                        Intent nextActivity = LobbyListActivity.getStartIntent(context);
                        context.startActivity(nextActivity);
                        mShouldMusicStay = true;
                        finish();
                    }
                })
                .setNegativeButton("No", null).show();
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LobbyActivity.class);
        return intent;
    }

    private void updatePlayersListUi(PlayerInfo[] playerList) {
        Log.d(TAG, "updatePlayersListUi");
        for (PlayerInfo pi : playerList) {
            Log.d(TAG, pi.getPlayerId());
        }
        mPlayersNumberTextView.setText(String.valueOf(playerList.length)
                + "/" + String.valueOf(SessionInfo.getPublicLobbyInfo().getMaxPlayersNumber()));
        if (mLobbyPlayersListView.getAdapter() == null) {
            mLobbyPlayersListView.setAdapter(new LobbyPlayersListAdapter(this, playerList));
        } else {
            LobbyPlayersListAdapter lobbyPlayersListAdapter = (LobbyPlayersListAdapter) mLobbyPlayersListView.getAdapter();
            lobbyPlayersListAdapter.clear();
            lobbyPlayersListAdapter.addAll(playerList);
            lobbyPlayersListAdapter.notifyDataSetChanged();
        }
    }

    private class LobbyPlayersListAdapter extends ArrayAdapter<PlayerInfo> {
        private ArrayList<PlayerInfo> mPlayerList;

        public LobbyPlayersListAdapter(Context context, PlayerInfo[] playerList) {
            super(context, R.layout.item_player);
            this.mPlayerList = new ArrayList<>(Arrays.asList(playerList));
        }

        @Override
        public View getView(int position, View listView, ViewGroup parent) {
            PlayerInfo playerInfo = mPlayerList.get(position);

            if (listView == null) {
                listView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_player, null);
            }
            ((TextView) listView.findViewById(R.id.tv_player_name))
                    .setText(playerInfo.getPlayerName());
            ((TextView) listView.findViewById(R.id.tv_player_rating))
                    .setText(String.valueOf(playerInfo.getRating()));
            return listView;
        }

        @Override
        public int getCount() {
            return mPlayerList.size();
        }

        @Override
        public void addAll(PlayerInfo... items) {
            super.addAll(items);
            mPlayerList = new ArrayList<>(Arrays.asList(items));
        }

        @Override
        public void add(@Nullable PlayerInfo playerInfo) {
            super.add(playerInfo);
            mPlayerList.add(playerInfo);
        }

        @Override
        public void clear() {
            super.clear();
            mPlayerList = new ArrayList<>();
        }
    }

    private class LobbyActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof GameStartedNotification) {
                GameStartedNotification gameStartedNotification = (GameStartedNotification) msg.obj;
                NetworkService.setHandler(null);
                Intent intent = GameActivity.getStartIntent(getApplicationContext(), gameStartedNotification);
                startActivity(intent);
                finish();
            } else if (msg.obj instanceof LobbyStateChangedNotification) {
                Log.d(TAG, "Got LobbyStateChangedNotification");
                LobbyStateChangedNotification lobbyStateChangedNotification = (LobbyStateChangedNotification) msg.obj;
                if (lobbyStateChangedNotification.getPrivateLobbyInfo() == null) {
                    Log.d(TAG, "Private lobby info is null!!!!");
                }
                SessionInfo.setPrivateLobbyInfo(lobbyStateChangedNotification.getPrivateLobbyInfo());
                updatePlayersListUi(SessionInfo.getPrivateLobbyInfo().getPlayers());
            }
        }
    }
}
