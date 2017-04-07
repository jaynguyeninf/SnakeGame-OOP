package com.mygdx.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.asset_helpers.AssetDescriptors;
import com.mygdx.game.asset_helpers.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.configs.GameConfig;
import com.mygdx.game.entities.BodyPart;
import com.mygdx.game.entities.Coin;
import com.mygdx.game.entities.Snake;
import com.mygdx.game.entities.SnakeHead;
import com.mygdx.game.utils.ViewportUtils;
import com.mygdx.game.utils.debug.DebugCameraController;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class GameRenderer implements Disposable { //only need disposed() from Disposable

    private final GameController gameController;
    private final SpriteBatch batch;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private TextureRegion backgroundRegion, bodyRegion, headRegion, coinRegion;

    private DebugCameraController debugCameraController;

    private static final float PADDING = 20f;

    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController gameController) {
        this.batch = batch;
        this.assetManager = assetManager;
        this.gameController = gameController;

        init();
    }

    // == init ==
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT); //internal camera
        shapeRenderer = new ShapeRenderer();

        //get assets
        font = assetManager.get(AssetDescriptors.UI_FONT);

        TextureAtlas gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        bodyRegion = gameplayAtlas.findRegion(RegionNames.BODY);
        coinRegion = gameplayAtlas.findRegion(RegionNames.COIN);
        headRegion = gameplayAtlas.findRegion(RegionNames.HEAD);


        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_X_CENTER, GameConfig.WORLD_Y_CENTER);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderGameplay();
        renderHud();
//        renderDebug();
    }

    private void renderGameplay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawGameplay();

        batch.end();
    }

    private void drawGameplay() {
        //draw background
        batch.draw(backgroundRegion, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        //draw coin
        Coin coin = gameController.getCoin();
        if (coin.isAvailable()) {
            batch.draw(coinRegion, coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
        }

        Snake snake = gameController.getSnake();
        //draw snake head
        SnakeHead head = snake.getHead();
        batch.draw(headRegion, head.getX(), head.getY(), head.getWidth(), head.getHeight());


        //draw body parts
        for (BodyPart bodyPart : snake.getBodyParts()) {
            batch.draw(bodyRegion, bodyPart.getX(), bodyPart.getY(), bodyPart.getWidth(), bodyPart.getHeight());
        }
    }

    private void renderHud() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined); //internal camera
        batch.begin();

        drawHud();

        batch.end();
    }

    private void drawHud() {
        //draw high score
        String highScoreText = "High Score: " + (int) GameManager.INSTANCE.getDisplayHighScore();
        glyphLayout.setText(font, highScoreText);
        font.draw(batch, glyphLayout, PADDING, hudViewport.getWorldHeight() - PADDING);

        //draw score
        float scoreXPosition = hudViewport.getWorldWidth() - glyphLayout.width;
        float scoreYPosition = hudViewport.getWorldHeight() - PADDING;
        String scoreText = "Score: " + (int) GameManager.INSTANCE.getDisplayScore();
        glyphLayout.setText(font, scoreText);

        font.draw(batch, glyphLayout, scoreXPosition, scoreYPosition);
    }

    private void renderDebug() {
        ViewportUtils.drawGrid(viewport, shapeRenderer);

        viewport.apply();

        //copy current color
        Color oldColor = new Color(shapeRenderer.getColor());
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        shapeRenderer.end();
        //set color back to default
        shapeRenderer.setColor(oldColor);
    }

    private void drawDebug() {

        Snake snake = gameController.getSnake();

        shapeRenderer.setColor(Color.YELLOW);
        //draw body parts before head so that they dont overlap when adding new body part
        for (BodyPart bodyPart : snake.getBodyParts()) {
            Rectangle bodyPartBounds = bodyPart.getBounds();
            shapeRenderer.rect(bodyPartBounds.x, bodyPartBounds.y, bodyPartBounds.width, bodyPartBounds.height);
        }

        shapeRenderer.setColor(Color.GREEN);
        SnakeHead head = snake.getHead();
        Rectangle headBounds = head.getBounds();
        //draw head
        shapeRenderer.rect(headBounds.x, headBounds.y, headBounds.width, headBounds.getHeight());


        //draw coin
        shapeRenderer.setColor(Color.BLUE);
        Coin coin = gameController.getCoin();
        if (coin.isAvailable()) { //only draw when coin is available
            Rectangle coinBounds = coin.getBounds();
            //draw coin
            shapeRenderer.rect(coinBounds.x, coinBounds.y, coinBounds.width, coinBounds.height);
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(viewport);
        ViewportUtils.debugPixelsPerUnit(hudViewport);
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
