package com.emilyn.callofthebog.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.emilyn.callofthebog.CallofTheBog;



import jdk.vm.ci.code.site.Call;

public class Hud {
    public Stage stage;
    private Viewport viewport; //seperate for the tangible world

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label bogLabel;


    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;



        viewport = new FitViewport(CallofTheBog.V_WIDTH, CallofTheBog.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bogLabel= new Label("Bog", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(bogLabel).expandX().padTop(10); //expands to length of screen
        table.add(worldLabel).expandX().padTop(10); //expands to length of screen
        table.add(timeLabel).expandX().padTop(10); //expands to length of screen
        table.row();//adds new row
        table.add(scoreLabel).expandX(); //expands to length of screen
        table.add(levelLabel).expandX(); //expands to length of screen
        table.add(countdownLabel).expandX(); //expands to length of screen

        stage.addActor(table);//adds table to stage
    }

}