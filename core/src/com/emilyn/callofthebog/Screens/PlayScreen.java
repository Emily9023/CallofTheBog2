package com.emilyn.callofthebog.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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


    public PlayScreen(CallofTheBog game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(CallofTheBog.V_WIDTH, CallofTheBog.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0); //usually set at (0,0)



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
        update(delta);



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

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
