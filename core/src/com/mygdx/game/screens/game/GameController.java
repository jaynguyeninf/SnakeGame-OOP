package com.mygdx.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.configs.GameConfig;
import com.mygdx.game.entities.BodyPart;
import com.mygdx.game.entities.Coin;
import com.mygdx.game.entities.Direction;
import com.mygdx.game.entities.Snake;
import com.mygdx.game.entities.SnakeHead;
import com.mygdx.game.sound_helpers.SoundListener;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class GameController {
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    private final SoundListener soundListener;

    private Snake snake;
    private Coin coin;

    private float timer;

    public GameController(SoundListener soundListener) {
        this.soundListener = soundListener;
        snake = new Snake();
        coin = new Coin();
        restart(); //fixed where in MenuScreen and click Play button but game won't start because it's still in GameOver state
    }

    public void update(float delta) {

        //update score
        GameManager.INSTANCE.updateDisplayScores(delta);

        //Updates only when game is in playing state
        if (GameManager.INSTANCE.isPlaying()) {

            //check inputs
            queryInput();

            //debugging methods
            queryDebugInput();

            timer += delta;
            if (timer >= GameConfig.MOVE_TIME) {
                timer = 0;
                snake.move();

                checkOutOfBounds();
                checkCollision();
            }

            spawnCoin();

        } else {
            checkForRestart();
        }
    }

    private void queryInput() {
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (leftPressed) {
            snake.setDirection(Direction.LEFT);
        } else if (rightPressed) {
            snake.setDirection(Direction.RIGHT);
        } else if (upPressed) {
            snake.setDirection(Direction.UP);
        } else if (downPressed) {
            snake.setDirection(Direction.DOWN);
        }
    }

    //debugging
    private void queryDebugInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
            snake.insertBodyPart();
        }
    }

    private void checkOutOfBounds() {
        SnakeHead head = snake.getHead();

        //check X bounds (left & right)
        if (head.getX() >= GameConfig.WORLD_WIDTH) { //out of right bounds
            head.setX(0);
        } else if (head.getX() < 0) { //out of left bounds
            head.setX(GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SPEED); //when out of bounds, immediate shows on the other bounds
        }

        //check Y bounds (up & down)
        if (head.getY() >= GameConfig.MAX_Y) { //out of up bounds
            head.setY(0);
        } else if (head.getY() < 0) { //out of down bounds
            head.setY(GameConfig.MAX_Y - GameConfig.SNAKE_SPEED); //when out of bounds, immediate shows on the other bounds
        }
    }

    private void checkCollision() {
        //collision between head and coin
        SnakeHead head = snake.getHead();
        Rectangle headBounds = head.getBounds();
        Rectangle coinBounds = coin.getBounds();

        boolean overlapped = Intersector.overlaps(headBounds, coinBounds);

        if (coin.isAvailable() && overlapped) {
            soundListener.hitCoin();
            snake.insertBodyPart();
            coin.setAvailable(false);
            GameManager.INSTANCE.incrementScore(GameConfig.COIN_SCORE);
        }

        //collision between head and body part
        for (BodyPart bodyPart : snake.getBodyParts()) {

            //temporary fix for when snake hits coin but also detects collision with its body
            if (bodyPart.isJustAdded()) {
                bodyPart.setJustAdded(false);
                continue; //continues the for loop but ignore the rest of the code unlike "break"
            }

            Rectangle bodyPartBounds = bodyPart.getBounds();
            if (Intersector.overlaps(bodyPartBounds, headBounds)) {
                soundListener.lose();
                GameManager.INSTANCE.updateHighScore();
                GameManager.INSTANCE.setGameOver();
            }
            ;
        }

    }

    private void spawnCoin() {
        if (!coin.isAvailable()) { //spawn of coin is not available
            float coinX = MathUtils.random((int) (GameConfig.WORLD_WIDTH - GameConfig.COIN_SIZE));
            float coinY = MathUtils.random((int) (GameConfig.MAX_Y - GameConfig.COIN_SIZE));

            coin.setAvailable(true);

            coin.setPosition(coinX, coinY);
        }
    }

    private void checkForRestart(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            restart();
        }
    }

    private void restart(){
        GameManager.INSTANCE.reset();
        snake.resetBodyParts();
        coin.setAvailable(false);
        timer = 0; // restart time or speed
    }


    //getters
    public Snake getSnake() {
        return snake;
    }

    public Coin getCoin() {
        return coin;
    }


}
