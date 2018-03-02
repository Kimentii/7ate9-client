package com.yatty.sevenatenine.api.in_commands;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;

import com.yatty.sevenatenine.api.commands_with_data.Card;
import com.yatty.sevenatenine.client.Constants;

import java.util.LinkedList;
import java.util.List;

public class GameStartedEvent implements InCommandInterface {
    public static final int UID = GameStartedEvent.COMMAND_TYPE.hashCode();
    public static final String TAG = "TAG";
    public static final String COMMAND_TYPE = "GameStartedEvent";
    public final String _type = COMMAND_TYPE;

    private Card firstCard;
    private List<Card> playerCards;

    protected GameStartedEvent(Parcel in) {
        firstCard = in.readParcelable(Card.class.getClassLoader());
        playerCards = in.createTypedArrayList(Card.CREATOR);
    }

    public static final Creator<GameStartedEvent> CREATOR = new Creator<GameStartedEvent>() {
        @Override
        public GameStartedEvent createFromParcel(Parcel in) {
            return new GameStartedEvent(in);
        }

        @Override
        public GameStartedEvent[] newArray(int size) {
            return new GameStartedEvent[size];
        }
    };

    @Override
    public void doLogic(Handler handler) {
        Log.d(TAG, "GameStartedEvent.doLogic");
        Log.d(TAG, "First card value: " + firstCard.getValue());
        Log.d(TAG, "First card modifier: " + firstCard.getModifier());
        for (int i = 0; i < playerCards.size(); i++) {
            Log.d(TAG, "Gard.value: " + playerCards.get(i).getValue());
            Log.d(TAG, "Gard.modifier: " + playerCards.get(i).getModifier());
        }
        Message message = new Message();
        message.obj = COMMAND_TYPE;
        message.sendingUid = UID;
        handler.sendMessage(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(firstCard, flags);
        dest.writeTypedList(playerCards);
    }

    public Card getFirstCard() {
        return firstCard;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }
}