package com.dasilveira.webslinger.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
    private Sprite waterG, userSprs;
    private WorldController controller;
    private OrthographicCamera camera2, boxCam;
    private SpriteBatch batch;
    private BitmapFont fontScore;
    private Pixmap pixmap;
    private Array<Body> tmpBodies;
    private ParticleEffect mist;

    public WorldRenderer(WorldController worldController) {
        this.controller = worldController;
        batch = new SpriteBatch();
        fontScore = controller.font;
        camera2 = worldController.camera2;
        boxCam = worldController.box2dCam;
        tmpBodies = new Array();

        pixmap = controller.pixmap;
        pixmap.setColor(Color.NAVY);
        pixmap.fill();
        waterG = new Sprite(new Texture(pixmap));
        waterG.setAlpha(0.3f);

        mist = controller.mist;
        mist.scaleEffect(0.03f);
        mist.setEmittersCleanUpBlendFunction(false);
        mist.reset();
    }

    public void drawScene(float delta) {
        camera2.update();
        batch.setProjectionMatrix(camera2.combined);
        batch.begin();
        fontScore.draw(batch, String.valueOf(controller.getScore()), 10.0f,
                camera2.viewportHeight - 10.0f);
        batch.end();

        boxCam.update();
        batch.setProjectionMatrix(boxCam.combined);

        batch.begin();
        controller.getWorld().getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() instanceof Sprite) {
                userSprs = (Sprite) body.getUserData();
                userSprs.setPosition(body.getPosition().x - userSprs.getWidth() / 2,
                        body.getPosition().y - userSprs.getHeight() / 2);
                userSprs.draw(batch);
            }
            if (body.getType() == BodyDef.BodyType.DynamicBody) {
                mist.setPosition(body.getPosition().x, body.getPosition().y);
                mist.draw(batch, delta);
            }
        }

        waterG.setPosition(controller.bottomLeft.x, controller.bottomLeft.y + 1);
        waterG.setSize(boxCam.viewportWidth, -boxCam.viewportHeight * 3);
        waterG.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        tmpBodies.clear();
        controller.shotWebBodies.clear();
        controller.killWebJoints.clear();
        controller.nextLevelBodies.clear();
        controller.debugRenderer.dispose();
        controller.world.dispose();
        mist.dispose();
    }
}
