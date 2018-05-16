package com.yatty.sevenatenine.api.commands_with_data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlayerResult implements Parcelable, Serializable {
    private static final char DIVIDER = '|';

    private String playerId;
    private int cardsLeft;
    private int newRating;

    protected PlayerResult(Parcel in) {
        playerId = in.readString();
        cardsLeft = in.readInt();
        newRating = in.readInt();
    }

    public static final Creator<PlayerResult> CREATOR = new Creator<PlayerResult>() {
        @Override
        public PlayerResult createFromParcel(Parcel in) {
            return new PlayerResult(in);
        }

        @Override
        public PlayerResult[] newArray(int size) {
            return new PlayerResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerId);
        dest.writeInt(cardsLeft);
        dest.writeInt(newRating);
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerId.substring(0, playerId.indexOf(DIVIDER));
    }

    public int getCardsLeft() {
        return cardsLeft;
    }

    public int getNewRating() {
        return newRating;
    }

    public void setNewRating(int newRating) {
        this.newRating = newRating;
    }
}
