package com.emilyn.callofthebog.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.emilyn.callofthebog.CallofTheBog;
import com.emilyn.callofthebog.Screens.PlayScreen;

import java.io.*;

public class Pengo extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion img;

    public Pengo(World world, PlayScreen Screen){

        this.world = world;
        definePengo();
        TextureRegion img = new TextureRegion(new Texture("backup fabric.png"));
        setRegion(img);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() /2, b2body.getPosition().y - getWidth() /2);
    }

    public void definePengo(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / CallofTheBog.PPM, 32 / CallofTheBog.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / CallofTheBog.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(7 / CallofTheBog.PPM, -2 / CallofTheBog.PPM), new Vector2(7 / CallofTheBog.PPM, 2 / CallofTheBog.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");

    }



}
