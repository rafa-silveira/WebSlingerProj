package com.dasilveira.webslinger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasilveira.webslinger.loads.LoadScreen;

/**
 * Created by Rafael Silveira on 30/11/2015.
 */

public class WebSlinger extends Game {
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    static {
        SCREEN_WIDTH = 800;
        SCREEN_HEIGHT = 480;
    }

    public AssetManager manager;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;
    public Skin skin;
    public Pixmap pixmap;
    public Drawable backGDraw;
    public TextureAtlas atlas;

    public Viewport viewport;
    public float soundVolume;
    public boolean soundEnabled;
    public Music music;
    public Preferences prefs;
    public InputAdapter principalInput;
    public ParticleEffect mist;

    @Override
    public void create() {
        manager = new AssetManager();
        batch = new SpriteBatch();
        prefs = Gdx.app.getPreferences("SpiderBeginsPrefs");
        camera = new OrthographicCamera();
        camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.CORAL);
        pixmap.fill();
        backGDraw = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        soundVolume = prefs.getFloat("sound vol", 1.0f);
        soundEnabled = prefs.getBoolean("sound stat", true);

        this.setScreen(new LoadScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        font.dispose();
        skin.dispose();
        pixmap.dispose();
        atlas.dispose();
        music.dispose();
        super.dispose();
    }
}
