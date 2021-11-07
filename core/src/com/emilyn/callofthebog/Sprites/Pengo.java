package com.emilyn.callofthebog.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.emilyn.callofthebog.CallofTheBog;
import com.emilyn.callofthebog.Screens.PlayScreen;

public class Pengo extends Sprite {
    public enum State {RUNNING, STANDING};
    public State currentState;
    public State previousState; //previousState is not used atm but may be implemented later (Nov 7th 2021)

    public World world;
    public Body b2body;

    private TextureRegion PengoStanding;
    private Animation PengoRun;
    private float stateTimer;

    //not used rn but might be implemented later (November 7th)
    private boolean runningRight;


    private TextureRegion img;

    private int pengoSize = 7;

    public Pengo(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("PengoRunning_Spritesheet"));
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        runningRight = true; // not used atm may be implemented later (Nov 7th)


        this.world = world;
        definePengo();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i ++){
            frames.add(new TextureRegion(getTexture(), i* 362, 0, 362, 326));
        }

        PengoRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        PengoStanding = new TextureRegion(getTexture(),0, 0, 362, 326);
        setBounds(0, 0, 362 / CallofTheBog.PPM/10, 326 / CallofTheBog.PPM/10);
        setRegion(PengoStanding);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() /2, b2body.getPosition().y - getWidth() /2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case RUNNING:
                region = (TextureRegion) PengoRun.getKeyFrame(stateTimer, true);
                break;
            default:
                region = PengoStanding;
                break;
        }

        if ((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x >0|| runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }


        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void definePengo(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / CallofTheBog.PPM, 32 / CallofTheBog.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(pengoSize / CallofTheBog.PPM);
        fdef.filter.categoryBits = CallofTheBog.MARIO_BIT;
        fdef.filter.maskBits = CallofTheBog.DEFAULT_BIT | CallofTheBog.BRICK_BIT | CallofTheBog.COIN_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(pengoSize/ CallofTheBog.PPM, -2 / CallofTheBog.PPM), new Vector2(pengoSize/ CallofTheBog.PPM, 2 / CallofTheBog.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");

    }



}
