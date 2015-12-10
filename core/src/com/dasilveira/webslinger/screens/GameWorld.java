package com.dasilveira.webslinger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector3;
import com.dasilveira.webslinger.WebSlinger;
import com.dasilveira.webslinger.views.WorldController;
import com.dasilveira.webslinger.views.WorldRenderer;

public class GameWorld extends BaseScene {
    private WebSlinger game;
    private Vector3 touchPos;
    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public GameWorld(WebSlinger game) {
        super(game);
        game.mist = game.manager.get("effects/mist.p", ParticleEffect.class);
        this.game = game;
        touchPos = new Vector3();
        loadItems();

        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        worldController.setTouchedArea(new Vector3(-300.0f, -300.0f, -300.0f));

        gameState = GameState.ACTION;
    }

    private void loadItems() {
        game.principalInput = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPos.set(screenX, screenY, 0);
                worldController.setTouchedArea(touchPos);

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                worldController.setTouchedArea(new Vector3(-300.0f, -300.0f, -300.0f));
                worldController.killWebJoint();

                return true;
            }
        };

        Gdx.input.setInputProcessor(game.principalInput);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f, .15f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldRenderer.drawScene(delta);
        super.render(delta);

        if (gameState == GameState.PAUSE || gameState == GameState.GAME_OVER) {
            scorePrincipal = worldController.getScore();
            return;
        }

        worldController.render(delta);
    }

    public void dispose() {
        worldRenderer.dispose();
        super.dispose();
    }

    @Override
    protected void handleBackPress() {
        if (gameState == GameState.ACTION) {
            pause();
            return;
        }
        if (gameState == GameState.PAUSE) {
            resume();
        }
    }

}