package com.mygdx.game.screens.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.SnakeGame;
import com.mygdx.game.asset_helpers.AssetDescriptors;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.screens.menu.MenuScreen;
import com.mygdx.game.sound_helpers.SoundListener;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class GameScreen extends ScreenAdapter {

    private final SnakeGame game;
    private final AssetManager assetManager;
    private final SoundListener soundListener;

    private GameRenderer gameRenderer;
    private GameController gameController;
    private Sound coinSound, loseSound;

    // == Constructor ==
    public GameScreen(SnakeGame game) {
        this.game = game;
        assetManager = game.getAssetManager();

        soundListener = new SoundListener() {
            @Override
            public void hitCoin() {
                coinSound.play();
            }

            @Override
            public void lose() {
                loseSound.play();
            }
        };
    }

    @Override
    public void show() {

        //get assets
        coinSound = assetManager.get(AssetDescriptors.COIN_SOUND);
        loseSound = assetManager.get(AssetDescriptors.LOSE_SOUND);

        gameController = new GameController(soundListener);
        gameRenderer = new GameRenderer(game.getBatch(), game.getAssetManager(), gameController);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        gameRenderer.render(delta);

        //call after render to finish all renderings
        if (GameManager.INSTANCE.isGameOver()) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
    }
}
