package com.emilyn.callofthebog.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.emilyn.callofthebog.CallofTheBog;
import com.emilyn.callofthebog.Sprites.Brick;
import com.emilyn.callofthebog.Sprites.Coin;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //create ground bodies/fixtures

        Body body;
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CallofTheBog.PPM, (rect.getY() + rect.getHeight() / 2) / CallofTheBog.PPM); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef); //create a body in the world according to the specifications of bdef

            shape.setAsBox(rect.getWidth() / 2 / CallofTheBog.PPM, rect.getHeight() /2 / CallofTheBog.PPM ); //set shape
            fdef.shape = shape; //initialize the shpae of the fixture definition
            body.createFixture(fdef); //sets the fixture of the body according to the specifications of fdef

        }

        //create pipe bodies/fixtures

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CallofTheBog.PPM, (rect.getY() + rect.getHeight() / 2) / CallofTheBog.PPM); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef); //create a body in the world according to the specifications of bdef

            shape.setAsBox(rect.getWidth() / 2 / CallofTheBog.PPM, rect.getHeight() /2 / CallofTheBog.PPM ); //set shape
            fdef.shape = shape; //initialize the shpae of the fixture definition
            body.createFixture(fdef); //sets the fixture of the body according to the specifications of fdef

        }

        //create brick bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(world, map, rect);

        }
        //create coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(world, map, rect);

        }


    }

}

