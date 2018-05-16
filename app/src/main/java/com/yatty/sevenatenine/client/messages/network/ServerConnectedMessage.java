package com.yatty.sevenatenine.client.messages.network;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerConnectedMessage implements Parcelable {

    public ServerConnectedMessage() {
    }

    protected ServerConnectedMessage(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServerConnectedMessage> CREATOR = new Creator<ServerConnectedMessage>() {
        @Override
        public ServerConnectedMessage createFromParcel(Parcel in) {
            return new ServerConnectedMessage(in);
        }

        @Override
        public ServerConnectedMessage[] newArray(int size) {
            return new ServerConnectedMessage[size];
        }
    };
}
