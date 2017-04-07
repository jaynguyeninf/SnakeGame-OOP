package com.mygdx.game.common;

/**
 * Created by Jay Nguyen on 4/5/2017.
 */

public enum GameState {
    READY,
    PLAYING,
    GAME_OVER;

    public boolean isReady(){return this == READY;}
    public boolean isPlaying(){return this == PLAYING;}
    public boolean isGameOver(){return this == GAME_OVER;}
}
