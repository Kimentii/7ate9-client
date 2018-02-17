package com.yatty.sevenatenine.api;

import android.os.Handler;

public class MoveRequest implements CommandInterface {
    public static final String COMMAND_TYPE = "MoveRequest";
    public final String _type = COMMAND_TYPE;
    private String gameId;
    private int moveNumber;
    private int move;

    @Override
    public void doLogic(Handler handler) {

    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getMove() {
        return move;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setMove(int move) {
        this.move = move;
    }
}
