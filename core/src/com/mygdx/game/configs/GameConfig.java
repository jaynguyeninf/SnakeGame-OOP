package com.mygdx.game.configs;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class GameConfig {
    //===========Desktop========
    public static final float DESKTOP_WIDTH = 800;
    public static final float DESKTOP_HEIGHT = 480;

    //===========HUD/GUI=============
    public static final float HUD_WIDTH = 800;
    public static final float HUD_HEIGHT = 480;

    //===========World Units==========
    public static final float WORLD_WIDTH = 25; //default 25
    public static final float WORLD_HEIGHT = 15; //default 15

    public static final float WORLD_X_CENTER = WORLD_WIDTH / 2;
    public static final float WORLD_Y_CENTER = WORLD_HEIGHT / 2;

    public static final float MOVE_TIME = 0.2f; //default 0.2 second
    public static final float SNAKE_SPEED = 1f; //default 1 world unit

    public static final float SNAKE_SIZE = 1;
    public static final float COIN_SIZE = 1f;
    public static final float COIN_SCORE = 20f;

    //fixed snake goes behind score and high score displays
    private static final float Y_OFFSET = 2;
    public static final float MAX_Y = WORLD_HEIGHT - Y_OFFSET;





    private GameConfig() {
    }
}
