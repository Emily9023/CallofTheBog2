package com.emilyn.callofthebog.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.emilyn.callofthebog.CallofTheBog;
import com.emilyn.callofthebog.Scenes.Hud;

public class PlayScreen implements Screen {
    private CallofTheBog game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader; //loads the map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d varibles
    private World world;
    private Box2DDebugRenderer b2dr; //grpahical representation of objects and fixtures in the world



    public PlayScreen(CallofTheBog game){
        this.game = game;
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen differences
        gamePort = new FitViewport(CallofTheBog.V_WIDTH, CallofTheBog.V_HEIGHT, gameCam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //initially set our gamcacm to be centered correctly at the start of the game
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0); //usually set at (0,0)

        world = new World(new Vector2(0, 0), true); //if sleeping, then no calculations needed
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef(); //what does the body consist of - defines body
        PolygonShape shape = new PolygonShape(); //shape for fixture
        FixtureDef fdef = new FixtureDef(); //initialize the fixture
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef); //create a body in the world according to the specifications of bdef

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() /2 ); //set shape
            fdef.shape = shape; //initialize the shpae of the fixture definition
            body.createFixture(fdef); //sets the fixture of the body according to the specifications of fdef

        }

        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() /2 );
            fdef.shape = shape;
            body.createFixture(fdef);

        }

        //create brick bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() /2 );
            fdef.shape = shape;
            body.createFixture(fdef);

        }

        //create coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() /2 );
            fdef.shape = shape;
            body.createFixture(fdef);

        }





    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (Gdx.input.isTouched()){ //screen is being clicked or anything
            gameCam.position.x += 100 * dt;
        }

    }

    public void update(float dt){ //updates any inputs
        handleInput(dt);

        gameCam.update(); //update changes in the cam
        renderer.setView(gameCam); //makes the renderer render the gameCam

    }

    @Override
    public void render(float delta) {
        //seperate our update logive from render
        update(delta);


        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //Set our batch to now draw what the Hud camera seets
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); //what is shown via camera
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
