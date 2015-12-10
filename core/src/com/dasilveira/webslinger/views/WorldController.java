package com.dasilveira.webslinger.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;
import com.dasilveira.webslinger.WebSlinger;

public class WorldController {
    private static final float TIMESTEP = 1 / 60f;
    private static final int VELO_ITER = 8, POS_ITER = 3;

    BitmapFont font;
    Array<Body> deletionList = new Array<Body>();
    Vector3 bottomLeft, bottomRight;
    OrthographicCamera box2dCam, camera2;
    Pixmap pixmap;
    ParticleEffect mist;
    World world;
    Box2DDebugRenderer debugRenderer;
    Array<Body> shotWebBodies;
    Array<Joint> killWebJoints;
    Array<Body> nextLevelBodies;
    private WebSlinger game;
    private Vector3 touchedArea;
    private int score;
    private Player player;
    private DistanceJoint webJoint;
    private DistanceJointDef webJointDef;
    private Sprite buildSprite;


    public WorldController(WebSlinger game) {
        this.game = game;
        pixmap = game.pixmap;
        pixmap.setColor(Color.GOLD);
        pixmap.fill();
        buildSprite = new Sprite(new Texture(pixmap));

        shotWebBodies = new Array();
        killWebJoints = new Array();
        nextLevelBodies = new Array();

        touchedArea = new Vector3();
        box2dCam = new OrthographicCamera(game.SCREEN_WIDTH / 25, game.SCREEN_HEIGHT / 25);
        box2dCam.position.set(0, 0, 0);
        loadGraphics();

        // Box2d init
        world = new World(new Vector2(0, -1.81f), true);
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawAABBs(false);
        debugRenderer.setDrawBodies(false);
        debugRenderer.setDrawContacts(false);
        debugRenderer.setDrawInactiveBodies(false);
        debugRenderer.setDrawVelocities(false);
        debugRenderer.JOINT_COLOR.set(Color.WHITE);

        player = new Player(world, 0, 0.5f, 0.5f, pixmap);
        world.setContactFilter(player);
        world.setContactListener(player);

        bottomLeft = new Vector3(0, Gdx.graphics.getHeight(), 0);
        bottomRight = new Vector3(Gdx.graphics.getWidth(), bottomLeft.y, 0);
        box2dCam.unproject(bottomLeft);
        box2dCam.unproject(bottomRight);
        addBuildings();

    }

    private void loadGraphics() {
        font = game.font;
        camera2 = game.camera;
        this.mist = game.mist;
    }

    private void addBuildings() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        for (int i = 0; i < 9; i++) {
            float px = MathUtils.randomTriangular(bottomRight.x - 19, bottomRight.x - 2);
            float py = MathUtils.randomTriangular(bottomLeft.y + 15, bottomLeft.y + 19, bottomLeft.y + 18);
            float hx = MathUtils.random(1f, 1.2f);
            float hy = MathUtils.random(0.35f, 0.55f);

            // body definition
            bodyDef.position.set(px, py);

            // shape
            PolygonShape buildShape = new PolygonShape();
            buildShape.setAsBox(hx, hy);

            // fixture definition
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = buildShape;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 0;

            Body building = world.createBody(bodyDef);
            building.createFixture(fixtureDef);

            // define pixmap texture
            buildSprite.setAlpha(0.7f);
            buildSprite.setSize(hx * 2, hy * 2);
            buildSprite.setOriginCenter();
            building.setUserData(buildSprite);

            buildShape.dispose();
        }
        for (int i = 0; i < 9; i++) {
            float px = MathUtils.randomTriangular(bottomRight.x - 32, bottomRight.x - 16);
            float py = MathUtils.randomTriangular(bottomLeft.y + 9, bottomLeft.y + 15, bottomLeft.y + 12);
            float hx = MathUtils.random(1f, 1.2f);
            float hy = MathUtils.random(0.35f, 0.55f);

            // body definition
            bodyDef.position.set(px, py);

            // shape
            PolygonShape buildShape = new PolygonShape();
            buildShape.setAsBox(hx, hy);

            // fixture definition
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = buildShape;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 0;

            Body building = world.createBody(bodyDef);
            building.createFixture(fixtureDef);

            buildSprite.setAlpha(0.7f);
            buildSprite.setSize(hx * 2, hy * 2);
            buildSprite.setOriginCenter();
            building.setUserData(buildSprite);

            buildShape.dispose();
        }
        for (int i = 0; i < 18; i++) {
            float px = MathUtils.random(bottomRight.x - 30, bottomRight.x - 5);
            float py = MathUtils.random(bottomLeft.y + 6, bottomLeft.y + 19);
            float hx = MathUtils.random(1f, 1.2f);
            float hy = MathUtils.random(0.35f, 0.55f);

            // body definition
            bodyDef.position.set(px, py);

            // shape
            PolygonShape buildShape = new PolygonShape();
            buildShape.setAsBox(hx, hy);

            // fixture definition
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = buildShape;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 0;

            Body building = world.createBody(bodyDef);
            building.createFixture(fixtureDef);

            buildSprite.setSize(hx * 2, hy * 2);
            buildSprite.setOriginCenter();
            building.setUserData(buildSprite);

            buildShape.dispose();
        }
    }

    private void shotWeb(Vector3 touchedWeb) {
        box2dCam.unproject(touchedWeb);

        world.getBodies(shotWebBodies);
        for (Body body : shotWebBodies) {
            if (body != player.getBody() && body.getPosition().epsilonEquals(touchedWeb.x, touchedWeb.y, 1.2f)) {
                webJointDef = new DistanceJointDef();
                webJointDef.bodyA = player.getBody();
                webJointDef.bodyB = body;
                webJointDef.dampingRatio = 160;
                webJointDef.frequencyHz = 9;
                webJointDef.length = 0.2f;
                webJointDef.collideConnected = true;
                webJoint = (DistanceJoint) world.createJoint(webJointDef);

                break;
            }
        }
    }

    public void killWebJoint() {
        world.getJoints(killWebJoints);
        for (Joint joint : killWebJoints) {
            world.destroyJoint(joint);
        }
    }

    public void render(float delta) {
        updateScene(delta);
    }

    private void updateScene(float delta) {
        cameraMove(delta);
        sideTeleport();

        if (world.getBodyCount() <= 2) {
            world.getBodies(nextLevelBodies);
            for (Body bod : nextLevelBodies) {
                if (bod.getType() == BodyDef.BodyType.StaticBody)
                    deletionList.add(bod);
            }
            addBuildings();
        }

        if (player.getBody().getPosition().y < bottomRight.y + 1)
            player.getBody().applyForceToCenter(0, 9, true);

        if (world.getJointCount() > 0) {
            webJoint.setLength(webJoint.getLength() - delta * 10);
            if (webJoint.getFrequency() >= 0.30f)
                webJoint.setFrequency(webJoint.getFrequency() - delta * 5.5f);
        }

        box2dCam.update();
        debugRenderer.render(world, box2dCam.combined);
        world.step(TIMESTEP, VELO_ITER, POS_ITER);

        deletionList.addAll(player.getDestroyList());
        if (deletionList.size > 0) {
            for (Body body : deletionList) {
                world.destroyBody(body);
                score += 9;
            }
            player.getDestroyList().clear();
            deletionList.clear();
        }
    }

    private void cameraMove(float delta) {
        if (player.getBody().getPosition().y > bottomLeft.y + 18) {
            if (player.getBody().getPosition().y > box2dCam.position.y + 6)
                box2dCam.position.y += delta * player.getBody().getLinearVelocity().y;
            else if (player.getBody().getPosition().y + 6 > box2dCam.position.y)
                box2dCam.position.y += delta * 16;
        } else if (player.getBody().getPosition().y < bottomLeft.y + 1) {
            if (player.getBody().getPosition().y < box2dCam.position.y - 6)
                box2dCam.position.y -= delta * -player.getBody().getLinearVelocity().y;
            else if (player.getBody().getPosition().y - 6 < box2dCam.position.y)
                box2dCam.position.y -= delta * -16;
        } else if (player.getBody().getPosition().y + 6 < box2dCam.position.y)
            box2dCam.position.y = 0;

    }

    private void sideTeleport() {
        if (player.getBody().getPosition().x < bottomLeft.x)
            player.getBody().setTransform(bottomRight.x, player.getBody().getPosition().y, player.getBody().getAngle());
        else if (player.getBody().getPosition().x > bottomRight.x)
            player.getBody().setTransform(bottomLeft.x, player.getBody().getPosition().y, player.getBody().getAngle());
    }

    public void setTouchedArea(Vector3 touchedArea) {
        this.touchedArea = touchedArea;
        shotWeb(touchedArea);
    }

    public int getScore() {
        return score;
    }

    public World getWorld() {
        return world;
    }
}

