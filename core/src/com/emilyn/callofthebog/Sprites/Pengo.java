package com.emilyn.callofthebog.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.emilyn.callofthebog.CallofTheBog;

import java.io.*;

public class Pengo extends Sprite {

    public World world;
    public Body b2body;

    public Pengo(World world){
        this.world = world;
        definePengo();
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
        b2body.createFixture(fdef).setUserData(this);

    }



}
