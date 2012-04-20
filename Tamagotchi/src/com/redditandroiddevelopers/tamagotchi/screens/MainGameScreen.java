
package com.redditandroiddevelopers.tamagotchi.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.redditandroiddevelopers.tamagotchi.TamagotchiGame;
import com.redditandroiddevelopers.tamagotchi.ui.Draggable;
import com.redditandroiddevelopers.tamagotchi.ui.DraggableImage;

/**
 * This screen instance will represent the main game screen where your creature
 * will live.<br>
 * This is where the user will spend most of the time.
 */
public class MainGameScreen extends CommonScreen implements ClickListener, Draggable {

    private static final String TAG = "Tamagotchi:MainGameScreen";

    private Button btnLight;
    private Button btnShower;
    private Button btnToilet;
    private Button btnFood;
    // TODO make it drag down,not click
    private DraggableImage btnDragDown;
    
    public MainGameScreen(TamagotchiGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // TODO: add an OrthographicCamera

        // add groups for better readability and flexibility
        final Group ui = new Group("ui");
        final Group topButtons = new Group("top_buttons");
        
        final AssetManager assetManager = game.assetManager;

        // load needed textures for this screen
        assetManager.load("InGame/button.png", Texture.class);
        assetManager.load("InGame/arrow.png", Texture.class);
        // make sure all textures are loaded before continuing

        // TODO: Add a loading screen if loading takes too long
        assetManager.finishLoading();

        // prepare texture regions from the loaded textures
        // TODO: group the images into ONE Texture, and then create individual TextureRegions from that
        final Texture buttonTexture = assetManager.get(
                "InGame/button.png", Texture.class);
        final Texture arrowTexture = assetManager.get(
                "InGame/arrow.png", Texture.class);
        final TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture);
        final TextureRegion arrowTextureRegion = new TextureRegion(arrowTexture);

        // set margin between buttons
        final int marginBetweenButtons = 10;

        // buttons have the same width and height. using this value allows
        // precise placement of the buttons
        final int width = buttonTexture.getWidth() + marginBetweenButtons;

        // create food button
        btnFood = new Button(new TextureRegion(assetManager.get(
                "InGame/button.png", Texture.class)));
        btnFood.x = width * 0;
        btnFood.setClickListener(this);

        // create toilet button
        btnToilet = new Button(buttonTextureRegion);
        btnToilet.x = width * 1;
        btnToilet.setClickListener(this);

        // create shower button
        btnShower = new Button(buttonTextureRegion);
        btnShower.x = width * 2;
        btnShower.setClickListener(this);

        // create light button
        btnLight = new Button(buttonTextureRegion);
        btnLight.x = width * 3;
        btnLight.setClickListener(this);

        // add buttons to 'topButtons'
        topButtons.addActor(btnFood);
        topButtons.addActor(btnToilet);
        topButtons.addActor(btnShower);
        topButtons.addActor(btnLight);

        // adjust width of 'topButtons'
        topButtons.width = width * topButtons.getActors().size();

        // position topButtons in top right corner
        topButtons.x = stage.right() - topButtons.width;
        topButtons.y = stage.top() - width;

        // add 'topButtons' to the 'ui' group
        ui.addActor(topButtons);


        btnDragDown = new DraggableImage(arrowTextureRegion);
        btnDragDown.y = stage.top() - 64;
        btnDragDown.setClickListener(this);
        btnDragDown.setDraggable(this);
        ui.addActor(btnDragDown);

        // add the 'ui' to the stage
        stage.addActor(ui);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void draw(float delta) {
        stage.draw();
    }

    @Override
    public void click(Actor actor, float x, float y) {
        // touch input was received, time to find the culprit
        if (actor == btnFood) {
            Gdx.app.debug(TAG, "Touch on food button");
        } else if (actor == btnToilet) {
            Gdx.app.debug(TAG, "Touch on toilet button");
        } else if (actor == btnShower) {
            Gdx.app.debug(TAG, "Touch on shower button");
        } else if (actor == btnLight) {
            Gdx.app.debug(TAG, "Touch on light button");
        } else {
            Gdx.app.error(TAG, "Unknown actor");
            assert false;
        }
    }

    @Override
    public void hide() {
        // AssetManagers needs to be manually told to unload resources
        final AssetManager assetManager = game.assetManager;
        assetManager.unload("InGame/arrow.png");
        assetManager.unload("InGame/button.png");
        super.hide();
    }

    @Override
    public void drag(Actor a, float x, float y, int pointer) {
        // NOTICE THE += HERE!
        //a.x += x;
        a.y += y;
    }

}