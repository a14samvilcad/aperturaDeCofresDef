package m08.uf3.drops.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Set;

import m08.uf3.drops.Drops;
import m08.uf3.drops.Utils.Settings;
import m08.uf3.drops.helper.AssetManager;

public class MainMenuScreen implements Screen {

    GameScreen gameScreen;
    OrthographicCamera camera;

    final Drops game;
    private Stage stage;
    TextButton button;
    TextButton settingsButton;

    //para background
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture logoTexture;

    private Image playImage;
    private Image playImage2;


    private Sprite backgroundSprite;

    private Sprite logoSprite;



    public MainMenuScreen(Drops game) {
        this.game = game;


        // Creem la càmera de les dimensions del joc
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        camera.setToOrtho(false);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        //stage = new Stage(viewport);
        stage = new Stage(viewport);
        gameScreen = new GameScreen(stage.getBatch(), stage.getViewport(), game);

        //mySkin = new Skin(Gdx.files.internal(Settings.skin));


        crearLabels();
        //stage.addActor(title);
        //stage.addActor(message);
        stage.addActor(button);
        stage.addActor(settingsButton);
    }

    private void crearLabels(){
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont();

        Texture playTexture = new Texture(Gdx.files.internal("BotonJugar1.png"));
        playImage = new Image(playTexture);
        playImage.setScale(0.25f);

        Texture settingsTexture = new Texture(Gdx.files.internal("BotonAjustes1.png"));
        Image settingsImage = new Image(settingsTexture);
        settingsImage.setScale(0.25f);
        settingsImage.setPosition(1050,500);

        Cursor cursor = Gdx.graphics.newCursor(AssetManager.cursorZombie, 0, 0);
        Gdx.graphics.setCursor(cursor);
        /*
        title.setPosition((Settings.GAME_WIDTH - (title.getWidth() * Settings.TITLE_RESCALE_SIZE)) / 2, ((Settings.GAME_HEIGHT - title.getHeight()) / 2) + 50);
        message = new Label("Pulsa en la pantalla para empezar", new Label.LabelStyle(font, Color.WHITE));
        message.setPosition((Settings.GAME_WIDTH - (message.getWidth() * Settings.TITLE_RESCALE_SIZE)) / 2, (Settings.GAME_HEIGHT - message.getHeight()) / 2);*/

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.GOLD;


        Texture playTexture2 = new Texture(Gdx.files.internal("BotonJugar2.png"));
        playImage2 = new Image(playTexture2);
        playImage2.setScale(0.25f);

        button = new TextButton("", textButtonStyle);
        button.add(playImage).width(playImage.getWidth()).height(playImage.getHeight());
        button.setColor(Color.BLACK);
        button.setPosition((1050), (450));

        settingsButton = new TextButton("", textButtonStyle);
        settingsButton.add(settingsImage).width(settingsImage.getWidth()).height(settingsImage.getHeight());
        settingsButton.setColor(Color.BLACK);
        settingsButton.setPosition((1050), (300));



    }

    @Override
    public void show() {

        button.addListener(new InputListener() {
            @Override
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //button.add(playImage2).width(playImage2.getWidth()).height(playImage2.getHeight());
                button.addCaptureListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                game.setScreen(gameScreen);
                                dispose();
                            }
                        }, 3/4); // 1 es el tiempo en segundos antes de que se ejecute la tarea
                    }
                });
            }

            @Override
            public void exit (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.add(playImage).width(playImage.getWidth()).height(playImage.getHeight());
            }
        });
        //Pulsar boton PLAY


        //cosas fondo
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Background3.png"));
        backgroundSprite = new Sprite(backgroundTexture);

        logoTexture = new Texture(Gdx.files.internal("LogoMenuShadow.png"));
        logoSprite = new Sprite(logoTexture);

        logoSprite.setBounds(450, (Settings.GAME_HEIGHT/2), 400, 240);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float aspectRatio = screenWidth / screenHeight;
        float textureWidth = backgroundTexture.getWidth();
        float textureHeight = backgroundTexture.getHeight();
        float textureAspectRatio = textureWidth / textureHeight;

        if (textureAspectRatio > aspectRatio) {
            float scale = screenHeight / textureHeight;
            float width = textureWidth * scale;
            float height = textureHeight * scale;
            float x = (screenWidth - width) / 100;
            float y = 0;
            backgroundSprite.setBounds(x, y, width, height);
        } else {
            float scale = screenWidth / textureWidth;
            float width = textureWidth * scale;
            float height = textureHeight * scale;
            float x = 0;
            float y = (screenHeight - height) / 100;
            backgroundSprite.setBounds(x, y, width, height);
        }


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //PINTAR FONDO
        batch.begin();
        backgroundSprite.draw(batch);
        logoSprite.draw(batch);
        batch.end();

        //PINTAR STAGE
        stage.draw();
        stage.act(delta);



        // Si es fa clic en la pantalla, canviem la pantalla
        /*if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport(), game));
            dispose();
        }*/
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(width, height, true);
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


    //...Rest of class omitted for succinctness.

}