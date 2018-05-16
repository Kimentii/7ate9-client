package com.yatty.sevenatenine.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yatty.sevenatenine.api.commands_with_data.PlayerResult;

import java.util.Arrays;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = GameOverActivity.class.getSimpleName();
    private static final String EXTRA_CARDS_LEFT = "cards_left_key";
    private static final String EXTRA_WINNER_ID = "winner_name_key";
    private static final String EXTRA_PLAYER_ID = "player_name_key";
    private TextView mWinnerNameTextView;
    private Button mToLobbyListButton;
    private TextView mGameOverText;
    private String mPlayerId;
    private String mWinnerId;
    private ListView mScoreBoard;
    private boolean mShouldMusicStay;
    PlayerResult[] mScores;


    public static Intent newIntent(Context context, String playerName, String winnerName, PlayerResult[] scores) {
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra(EXTRA_PLAYER_ID, playerName);
        intent.putExtra(EXTRA_WINNER_ID, winnerName);
        intent.putExtra(EXTRA_CARDS_LEFT, scores);
        return intent;
    }

    @Override
    public void onClick(View v) {
        Intent intent = LobbyListActivity.getStartIntent(getApplicationContext());
        startActivity(intent);
        mShouldMusicStay = true;
        finish();
    }

    private class ScoreBoardAdapter extends ArrayAdapter<PlayerResult> {
        public ScoreBoardAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, mScores);
            //this.sort();
        }

        @Override
        public View getView(int position, View listView, ViewGroup parent) {
            PlayerResult result = getItem(position);

            if (listView == null) {
                listView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_2, null);
            }
            ((TextView) listView.findViewById(android.R.id.text1))
                    .setText(result.getPlayerName() + " (" + result.getNewRating() + ")");
            ((TextView) listView.findViewById(android.R.id.text2))
                    .setText("Cards left: " + result.getCardsLeft());
            return listView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        mGameOverText = findViewById(R.id.tv_game_over);
        mWinnerNameTextView = findViewById(R.id.tv_winner_name);
        mToLobbyListButton = findViewById(R.id.button_to_lobby_list);
        mScoreBoard = findViewById(R.id.lv_score_board);
        mToLobbyListButton.setOnClickListener(this);
        Intent intent = getIntent();
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID);
        mWinnerId = intent.getStringExtra(EXTRA_WINNER_ID);
        Parcelable parcelableArray[] = intent.getParcelableArrayExtra(EXTRA_CARDS_LEFT);
        if (parcelableArray != null) {
            mScores = Arrays.copyOf(parcelableArray, parcelableArray.length, PlayerResult[].class);
        }
        sortByCardCount(mScores);
        if (mWinnerId == null) mWinnerId = mScores[0].getPlayerId();
        if (mPlayerId.equals(mWinnerId)) {
            mGameOverText.setText("Winner winner chicken dinner!");
        } else {
            mGameOverText.setText("Better luck next time!");
        }
        mWinnerNameTextView.setText(mWinnerId.substring(0, mWinnerId.indexOf("|")));
        Log.d(TAG, "Winner: " + mWinnerId);
        Log.d(TAG, "Player name: " + mPlayerId);
        Log.d(TAG, "Scores:");
        for (int i = 0; i < mScores.length; i++) {
            Log.d(TAG, mScores[i].getPlayerId() + ": " + mScores[i].getCardsLeft());
        }
        ScoreBoardAdapter adapter = new ScoreBoardAdapter(this);
        mScoreBoard.setAdapter(adapter);
    }

    public void sortByCardCount(PlayerResult[] res) {
        PlayerResult tmp;
        for (int i = 0; i < res.length; i++)
            for (int j = i + 1; j < res.length; j++)
                if (res[j].getCardsLeft() < res[i].getCardsLeft()) {
                    tmp = res[i];
                    res[i] = res[j];
                    res[j] = tmp;
                }
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
}
