package com.yatty.sevenatenine.client.messages.network;

import android.os.Parcel;
import android.os.Parcelable;

public class ConnectionRefusedMessage implements Parcelable {
    public ConnectionRefusedMessage() {
    }

    protected ConnectionRefusedMessage(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConnectionRefusedMessage> CREATOR = new Creator<ConnectionRefusedMessage>() {
        @Override
        public ConnectionRefusedMessage createFromParcel(Parcel in) {
            return new ConnectionRefusedMessage(in);
        }

        @Override
        public ConnectionRefusedMessage[] newArray(int size) {
            return new ConnectionRefusedMessage[size];
        }
    };
}
