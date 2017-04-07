package com.mygdx.game.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SnakeGame;
import com.mygdx.game.asset_helpers.AssetDescriptors;
import com.mygdx.game.configs.GameConfig;
import com.mygdx.game.screens.menu.MenuScreen;

/**
 * Created by Jay Nguyen on 4/6/2017.
 */

public class LoadingScreen extends ScreenAdapter {

    private final SnakeGame game;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 1.5f;
    private static final float PROGRESS_BAR_HEIGHT = 50;

    private float progress;
    private float waitTimer = 0.75f;
    private boolean screenReady;

    public LoadingScreen(SnakeGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        //load assets
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.COIN_SOUND);
        assetManager.load(AssetDescriptors.LOSE_SOUND);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        draw();

        if(screenReady){ //flag used to fix "frames are not ready"
            game.setScreen(new MenuScreen(game));
        }
    }

    private void update(float delta) {
        progress = assetManager.getProgress(); //progress is from 0 to 1

        //update returns true when all loading is finished
        if (assetManager.update()) {

            waitTimer -= delta;
            if (waitTimer <= 0) {
                screenReady = true;
            }

        }
    }

    private void draw() {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect((GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2, (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
