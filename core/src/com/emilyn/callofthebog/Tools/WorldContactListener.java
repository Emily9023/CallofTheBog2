package com.emilyn.callofthebog.Tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.emilyn.callofthebog.CallofTheBog;
import com.emilyn.callofthebog.Sprites.InteractiveTileObject;
import com.emilyn.callofthebog.Sprites.Pengo;

import java.awt.event.ContainerListener;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
/*
        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            //checks if is an interactive tile object
            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHit();
            }
        }*/


        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef) {
            case CallofTheBog.EVIL_BIT | CallofTheBog.PENGO_BIT: //if hit bad guy
                Gdx.app.log("PENGO", "DIED");

                if (fixA.getFilterData().categoryBits == CallofTheBog.EVIL_BIT) {
                    Gdx.app.log("PENGO", "about to die");
                    ((Pengo) fixB.getUserData()).onHit();
                }
                else
                    ((Pengo) fixA.getUserData()).onHit();



            case CallofTheBog.BOULDER_BIT | CallofTheBog.PENGO_BIT: //if hit boulder guy

                if (fixA.getFilterData().categoryBits == CallofTheBog.BOULDER_BIT) {
                    Gdx.app.log("Pengo", "bonk");
                }

                break;

        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
