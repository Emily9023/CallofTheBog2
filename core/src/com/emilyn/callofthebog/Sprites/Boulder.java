package com.emilyn.callofthebog.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.emilyn.callofthebog.CallofTheBog;


public class Boulder extends InteractiveTileObject{
    public Boulder(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);

        fixture.setUserData(this);
        setCategoryFilter(CallofTheBog.BRICK_BIT);


    }

    @Override
    public void onHit() {

        Gdx.app.log("Brick", "Collision");


    }
}
