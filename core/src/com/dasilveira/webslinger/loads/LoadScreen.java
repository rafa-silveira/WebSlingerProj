package com.dasilveira.webslinger.loads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dasilveira.webslinger.WebSlinger;
import com.dasilveira.webslinger.screens.MenuScene;

public class LoadScreen extends ScreenAdapter {

    private Pixmap pixmap;
    private Texture backG, backG2, backG3, logo;
    private AssetManager manager;
    private WebSlinger game;
    private float percent, bgCounter, bgCounter2;

    public LoadScreen(WebSlinger game) {
        game.manager.load("background/RafBoSlogo.png", Texture.class);
        game.manager.finishLoading();

        this.game = game;
        manager = game.manager;

        logo = game.manager.get("background/RafBoSlogo.png", Texture.class);

        pixmap = game.pixmap;
        pixmap.setColor(Color.CHARTREUSE);
        pixmap.fill();
        backG = new Texture(pixmap);
        pixmap.setColor(Color.GOLD);
        pixmap.fill();
        backG2 = new Texture(pixmap);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        backG3 = new Texture(pixmap);

        manager.load("models/models.pack", TextureAtlas.class);
        manager.load("fonts/kenFuture-39.fnt", BitmapFont.class);
        manager.load("ui/uiskin.json", Skin.class);
        manager.load("ui/uiskin.atlas", TextureAtlas.class);
        manager.load("sounds/somnambulism.mp3", Music.class);
        manager.load("effects/mist.p", ParticleEffect.class);
        manager.finishLoading();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.isKeyPressed(Input.Keys.BACK);

        if (game.manager.update()) {
            game.atlas = manager.get("models/models.pack", TextureAtlas.class);
            game.font = manager.get("fonts/kenFuture-39.fnt", BitmapFont.class);
            game.font.setColor(Color.GOLD);
            game.skin = new Skin(manager.get("ui/uiskin.atlas", TextureAtlas.class));
            game.skin = manager.get("ui/uiskin.json", Skin.class);
            game.music = manager.get("sounds/somnambulism.mp3", Music.class);

            if (Gdx.input.isTouched()) {
                game.setScreen(new MenuScene(game));
            }
        }

        percent = Interpolation.linear.apply(percent, game.manager.getProgress() * logo.getWidth()
                + 70, 0.03f);
        bgCounter = Interpolation.linear.apply(bgCounter, percent, 0.03f);
        bgCounter2 = Interpolation.linear.apply(bgCounter2, bgCounter, 0.03f);

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.disableBlending();
        game.batch.draw(backG, 0, 0, 800, percent);
        game.batch.draw(backG2, 0, 0, 800, bgCounter);
        game.batch.draw(backG3, 0, 0, 800, bgCounter2);
        game.batch.enableBlending();
        game.batch.draw(logo, 200, 200, 0, 0, (int) bgCounter2, logo.getHeight());
        if (percent / 4 <= 101)
            game.font.draw(game.batch, (int) percent / 4 + "%", 30, 30);
        game.batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        backG.dispose();
        backG2.dispose();
        backG3.dispose();
        logo.dispose();
        manager.unload("background/RafBoSlogo.png");
        super.dispose();
    }
}
