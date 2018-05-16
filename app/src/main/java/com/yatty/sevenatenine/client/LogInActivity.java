package com.yatty.sevenatenine.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yatty.sevenatenine.api.in_commands.ErrorResponse;
import com.yatty.sevenatenine.api.in_commands.LogInResponse;
import com.yatty.sevenatenine.api.out_commands.LogInRequest;
import com.yatty.sevenatenine.client.auth.AuthManager;
import com.yatty.sevenatenine.client.auth.SessionInfo;
import com.yatty.sevenatenine.client.messages.network.ConnectionRefusedMessage;
import com.yatty.sevenatenine.client.messages.network.ServerConnectedMessage;
import com.yatty.sevenatenine.client.network.NetworkService;

public class LogInActivity extends AppCompatActivity {
    public static final String TAG = LogInActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private Button mConnectButton;
    private Button mGoogleConnectButton;
    private EditText mNameEditText;
    private boolean mShouldMusicStay;
    private Handler mHandler;
    private String mName;
    private String mPasswordHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_log_in);
        mConnectButton = findViewById(R.id.button_log_in);
        mGoogleConnectButton = findViewById(R.id.button_google_log_in);
        mNameEditText = findViewById(R.id.et_name);
        mHandler = new LogInHandler(this, mNameEditText, mConnectButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSettings.isMusicEnabled(this)) {
            startService(BackgroundMusicService.getIntent(getApplicationContext()));
        }
        View rootView = findViewById(android.R.id.content);
        rootView.setBackground(ApplicationSettings.getBackgroundPicture(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mShouldMusicStay) {
            stopService(BackgroundMusicService.getIntent(getApplicationContext()));
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextActivity = MainActivity.getStartIntent(this);
        startActivity(nextActivity);
        mShouldMusicStay = true;
        finish();
    }

    public void connectButtonClicked(View view) {
        try {
            if (!NetworkService.isOnline(getApplicationContext())) {
                showErrorAlert("No connection");
                return;
            }
            if (!mNameEditText.getText().toString().isEmpty()) {
                view.setEnabled(false);
                showSnackbar("Connecting...");
                enterServer(
                    mNameEditText.getText().toString(),
                    AuthManager.getUniqueDeviceHash(getApplicationContext())
                );
            } else {
                showSnackbar("Enter nickname");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception during connect", e);
        }
    }

    public void googleSignInClicked(View view) {
        if (!NetworkService.isOnline(getApplicationContext())) {
            showErrorAlert("No connection");
            return;
        }
        view.setEnabled(false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        showSnackbar("Connecting...");
        try {
            if (account != null) {
                String personId = account.getId();
                enterServer(account);
                Log.d(TAG, "Acc id: " + personId);
            } else {
                Log.d(TAG, "No acc");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        } catch (Exception e) {
            showSnackbar("Failed to connect");
            Log.e(TAG, "Failed to connect to server", e);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void enterServer(GoogleSignInAccount account) {
        enterServer(account.getDisplayName(), "google acc: " + AuthManager.getSHAHash(account.getId()));
    }

    private void enterServer(String name, String passwordHash) {
        Log.d(TAG, "Connecting to server...");
        NetworkService.setHandler(mHandler);
        mName = name;
        mPasswordHash = passwordHash;
        startService(NetworkService.getConnectionIntent(getApplicationContext()));
    }

    private void showSnackbar(String title) {
        FrameLayout parentFrameLayout = findViewById(R.id.fl_parent);
        Snackbar snackbar = Snackbar.make(parentFrameLayout, title, Snackbar.LENGTH_SHORT);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbar.getView().getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbar.getView().setLayoutParams(params);
        snackbar.show();
    }

    private void showErrorAlert(String message) {
        new AlertDialog.Builder(LogInActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d(TAG, "getting google account");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "Got acc width id: " + account.getId());
                Log.d(TAG, "Acc display name: " + account.getDisplayName());
                Log.d(TAG, "Acc family name: " + account.getFamilyName());
                Log.d(TAG, "Acc given name: " + account.getGivenName());

                if (account.getId() == null) {
                    Log.e(TAG, "Failed to get Google Account id");
                    showSnackbar("Failed to connect to Google Account");
                }
                enterServer(account);
            } catch (Exception e) {
                showErrorAlert("Failed to connect to Google Account");
                Log.e(TAG, "Exception", e);
            }
        }
    }

    class LogInHandler extends Handler {
        private Context context;
        private EditText nameEditText;
        private Button connectButton;
        private AppCompatActivity appCompatActivity;

        LogInHandler(AppCompatActivity appCompatActivity, EditText nameEditText, Button connectButton) {
            context = appCompatActivity.getApplicationContext();
            this.nameEditText = nameEditText;
            this.appCompatActivity = appCompatActivity;
            this.connectButton = connectButton;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof ServerConnectedMessage) {
                Log.d(TAG, "Connected to server, sending login request");
                LogInRequest logInRequest = new LogInRequest();
                logInRequest.setName(mName);
                logInRequest.setPasswordHash(mPasswordHash);
                startService(NetworkService.getSendIntent(getApplicationContext(), logInRequest, false));
                mConnectButton.setEnabled(true);
                mGoogleConnectButton.setEnabled(true);
            } else if (msg.obj instanceof ConnectionRefusedMessage) {
                showErrorAlert("Failed to connect to server");
                mConnectButton.setEnabled(true);
                mGoogleConnectButton.setEnabled(true);
            } else if (msg.obj instanceof LogInResponse) {
                Log.d(TAG, "Entered server");
                LogInResponse logInResponse = (LogInResponse) msg.obj;
                SessionInfo.setAuthToken(logInResponse.getAuthToken());
                SessionInfo.setUserId(logInResponse.getPlayerId());
                SessionInfo.setUserRating(logInResponse.getRating());
                Intent nextActivity = LobbyListActivity.getStartIntent(context);
                context.startActivity(nextActivity);
                mShouldMusicStay = true;
                appCompatActivity.finish();
            } else if (msg.obj instanceof ErrorResponse) {
                Log.e(TAG, "Got an exception");
                ErrorResponse errorResponse = (ErrorResponse) msg.obj;
                String error = errorResponse.getShortDescription();
                showErrorAlert(error == null ? "Internal Server Error" : error);
                mConnectButton.setEnabled(true);
                mGoogleConnectButton.setEnabled(true);
            }
        }
    }
}