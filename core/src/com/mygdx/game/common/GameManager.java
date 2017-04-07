package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.SnakeGame;

/**
 * Created by Jay Nguyen on 4/5/2017.
 */

// Singleton class
public class GameManager {

    public static final GameManager INSTANCE = new GameManager();
    private static final String HIGH_SCORE_KEY = "highscore";

    private GameState state = GameState.READY;
    private Preferences prefs;

    private int score;
    private int displayScore; //special effects
    private int highScore;
    private int displayHighScore;

    // == Constructor ==
    private GameManager() {
        prefs = Gdx.app.getPreferences(SnakeGame.class.getSimpleName()); //instantiate
        highScore = prefs.getInteger(HIGH_SCORE_KEY, 0); //key, default value
        displayHighScore = highScore;
    }

    // == Getters ==

    public boolean isReady() {
        return state.isReady();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }

    // == Setters ==
    public void setPlaying() {
        state = GameState.PLAYING;
    }

    public void setGameOver() {
        state = GameState.GAME_OVER;
    }

    public float getDisplayScore() {
        return displayScore;
    }

    public float getDisplayHighScore() {
        return displayHighScore;
    }

    // ===== updates =====
    public void updateHighScore(){
        if(score < highScore){
            return;
        }

        highScore = score;
        prefs.putInteger(HIGH_SCORE_KEY, highScore);
        prefs.flush(); //save/persist
    }

    public void updateDisplayScores(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (100 * delta)); //
        }

        if (displayHighScore < highScore) {
            displayHighScore = Math.min(highScore, displayHighScore + (int) (100 * delta));
        }
    }


    // === increment score ===
    public void incrementScore(float amount) {
        score += amount;
    }

    //=== reset ===
    public void reset() {
        setPlaying();
        score = 0;
        displayScore = 0;
    }



}
