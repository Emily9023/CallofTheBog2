package com.emilyn.callofthebog.Screens;

import static java.lang.Math.pow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.emilyn.callofthebog.Tools.B2WorldCreator;
import com.emilyn.callofthebog.Tools.WorldContactListener;

import jdk.vm.ci.code.site.Call;

public class PlayScreen implements Screen {
    private CallofTheBog game;
    private TextureAtlas atlas;

    private float speedAccelerator = 1.003f;
    private float speedAcceleratorPower = 0;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader; //loads the map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d varibles
    private World world;
    private Box2DDebugRenderer b2dr; //graphical representation of objects and fixtures in the world


    //sprites
    private Pengo player;

    public Vector2 maxPengoSpeed = new Vector2(1, 1);
    public Vector2 MovementVector = new Vector2(0.3f, .3f);
    public Vector2 WorldForces = new Vector2(0, 0);


    public PlayScreen(CallofTheBog game){
        atlas = new TextureAtlas("Pengo.pack");

        this.game = game;
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen differences
        gamePort = new FitViewport(CallofTheBog.V_WIDTH / CallofTheBog.PPM, CallofTheBog.V_HEIGHT / CallofTheBog.PPM, gameCam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Maps/boggermap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,  1 / CallofTheBog.PPM);


        //initially set our gamecam to be centered correctly at the start of the game
        gameCam.position.set(0, gamePort.getWorldHeight()/2, 0); //usually set at (0,0)

        world = new World(new Vector2(WorldForces.x, WorldForces.y), true); //if sleeping, then no calculations needed
        b2dr = new Box2DDebugRenderer();


        //create mario in game world
        player= new Pengo(world, this);

        new B2WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());



    }


    public TextureAtlas getAtlas(){
        return atlas;

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= maxPengoSpeed.y){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(0, MovementVector.y), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -maxPengoSpeed.y){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(0, -MovementVector.y), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().y >= -maxPengoSpeed.x){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(.1f, 0), player.b2body.getWorldCenter(), true);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().y >= -maxPengoSpeed.x){ //screen is being clicked or anything
            player.b2body.applyLinearImpulse(new Vector2(-.1f, 0), player.b2body.getWorldCenter(), true);
        }

        //player.b2body.applyLinearImpulse(new Vector2(MovementVector.x, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt){ //updates any inputs
        handleInput(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);


        speedAcceleratorPower ++;

        gameCam.position.x = (float) (gameCam.position.x + (1/ CallofTheBog.PPM) * pow(speedAccelerator, speedAcceleratorPower));


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


        game.batch.setProjectionMatrix(gameCam.combined); //setting what the game can see
        game.batch.begin();
        player.draw(game.batch); //drawing the character onto the screen
        game.batch.end();

        //Set our batch to now draw what the Hud camera seets
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); //what is shown via camera
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
