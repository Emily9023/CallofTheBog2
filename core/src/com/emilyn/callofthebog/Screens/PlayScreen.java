package com.emilyn.callofthebog.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.emilyn.callofthebog.Sprites.Pengo;

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


    //sprites
    private Pengo player;

    public Vector2 maxPengoSpeed = new Vector2(2, 0);
    public Vector2 MovementVector = new Vector2(0.1f, 4f);


    public PlayScreen(CallofTheBog game){
        this.game = game;
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen differences
        gamePort = new FitViewport(CallofTheBog.V_WIDTH / CallofTheBog.PPM, CallofTheBog.V_HEIGHT / CallofTheBog.PPM, gameCam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,  1 / CallofTheBog.PPM);

        //initially set our gamcacm to be centered correctly at the start of the game
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0); //usually set at (0,0)

        world = new World(new Vector2(0, -10), true); //if sleeping, then no calculations needed
        b2dr = new Box2DDebugRenderer();
        player= new Pengo(world);



        //create ground bodies/fixtures
        createFixtures(2);

        //create pipe bodies/fixtures
        createFixtures(3);

        //create brick bodies/fixtures
        createFixtures(5);

        //create coin bodies/fixtures
        createFixtures(4);



    }

    public void createFixtures(int layer){
        BodyDef bdef = new BodyDef(); //what does the body consist of - defines body
        PolygonShape shape = new PolygonShape(); //shape for fixture
        FixtureDef fdef = new FixtureDef(); //initialize the fixture
        Body body;

        for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //defines that the body is static
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CallofTheBog.PPM, (rect.getY() + rect.getHeight() / 2) / CallofTheBog.PPM); //sets the position of where the body is (position of the rect

            body = world.createBody(bdef); //create a body in the world according to the specifications of bdef

            shape.setAsBox(rect.getWidth() / 2 / CallofTheBog.PPM, rect.getHeight() /2 / CallofTheBog.PPM ); //set shape
            fdef.shape = shape; //initialize the shpae of the fixture definition
            body.createFixture(fdef); //sets the fixture of the body according to the specifications of fdef

        }

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(0, MovementVector.y), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= maxPengoSpeed.x){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(MovementVector.x, 0), player.b2body.getWorldCenter(), true);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -maxPengoSpeed.x){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(-MovementVector.x, 0), player.b2body.getWorldCenter(), true);
        }


    }

    public void update(float dt){ //updates any inputs
        handleInput(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        gameCam.position.x = player.b2body.getPosition().x;

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
