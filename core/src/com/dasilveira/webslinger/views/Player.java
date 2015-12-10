package com.dasilveira.webslinger.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player implements ContactFilter, ContactListener {
    public final float WIDTH;
    private Body body;
    private Fixture fixture;
    private Pixmap pixmap;
    private Array<Body> destroyList = new Array<Body>();

    public Player(World world, float x, float y, float playerWidth, Pixmap pixmap) {
        this.pixmap = pixmap;
        this.WIDTH = playerWidth;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(WIDTH / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 5;
        fixtureDef.density = 12;

        body = world.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);

        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixmap.setBlending(Pixmap.Blending.None);
        Sprite playerSprite = new Sprite(new Texture(pixmap));
        playerSprite.setSize(WIDTH, WIDTH);
        playerSprite.setOriginCenter();
        body.setUserData(playerSprite);

        shape.dispose();
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA == fixture || fixtureB == fixture)
            return body.getLinearVelocity().y < 0;
        return false;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA() == fixture || contact.getFixtureB() == fixture) {
            if (contact.getWorldManifold().getPoints()[0].y <= body.getPosition().y) {
                if (contact.getFixtureA() != fixture)
                    destroyList.add(contact.getFixtureA().getBody());
                if (contact.getFixtureB() != fixture)
                    destroyList.add(contact.getFixtureB().getBody());
            }
        }
    }

    public Array<Body> getDestroyList() {
        return destroyList;
    }

}
