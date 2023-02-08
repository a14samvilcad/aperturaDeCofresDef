package m08.uf3.drops.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.graalvm.compiler.asm.sparc.SPARCAssembler;

import javax.swing.JViewport;

import m08.uf3.drops.Drops;
import m08.uf3.drops.Objects.Player;
import m08.uf3.drops.Objects.Zombies;
import m08.uf3.drops.Utils.Controller;
import m08.uf3.drops.Utils.Settings;
import m08.uf3.drops.helper.AssetManager;

public class GameScreen implements Screen {
    long lastZombieTime;
    Stage stage;
    Batch batch;
    Player bucket;
    Zombies zombies;
    final Drops game;
    ShapeRenderer shapeRenderer;
    Label vidas;
    Label score;
    BitmapFont font;


    //Map variables
    public OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;
    private TiledMapTileLayer mapLayer;
    private MapProperties propiedadesMap;

    private int tileWidth, tileHeight;
    private int mapWidthInTiles, mapHeightInTiles;
    private int mapWidthInPixels, mapHeightInPixels;

    //Controller variables
    Controller controller;

    private boolean menuVisible = false;
    private Texture menuSprite;
    private Sprite botonSalirSprite;

    //Cosas para abrir cofres
    public Label mensajeCofre;

    public GameScreen(Batch prevBatch, Viewport prevViewport, Drops game) {

        map = AssetManager.map;
        menuSprite=AssetManager.menuImage;

        botonSalirSprite= new Sprite(AssetManager.botonSalir);

        botonSalirSprite.setPosition(490, 280);


        propiedadesMap = map.getProperties();
        tileWidth         = propiedadesMap.get("tilewidth", Integer.class);
        tileHeight        = propiedadesMap.get("tileheight", Integer.class);
        mapWidthInTiles   = propiedadesMap.get("width", Integer.class);
        mapHeightInTiles  = propiedadesMap.get("height", Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        mapLayer = (TiledMapTileLayer) map.getLayers().get("Suelo");

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        prevViewport.setCamera(camera);

        tmr = new OrthogonalTiledMapRenderer(map);

        this.game = game;
        Settings.LIVES = 3;

        //Labels de vida y puntuacion
        crearLabels();


        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        controller = new Controller();

        // Creem la nau i la resta d'objectes
        bucket = new Player(368, 20, 64, 64, mapLayer, propiedadesMap, controller);

        // Afegim els actors a l'stage
        stage.addActor(bucket);
        stage.addActor(vidas);
        stage.addActor(score);
        stage.addActor(mensajeCofre);
        // Donem nom a l'Actor
        bucket.setName("bucket");



    }

    private void crearLabels(){
        BitmapFont bitmapfont = new BitmapFont();

        batch = new SpriteBatch();
        font = new BitmapFont();

        mensajeCofre = new Label("PRESS [E] TO OPEN", new Label.LabelStyle(bitmapfont, Color.WHITE));
        //System.out.println(mensajeCofre);
        mensajeCofre.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 200, camera.position.y + Gdx.graphics.getHeight() / 2 - 2 - mensajeCofre.getHeight());
        vidas = new Label("Vidas: "+ Settings.LIVES, new Label.LabelStyle(bitmapfont, Color.WHITE));
        vidas.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 15, camera.position.y + Gdx.graphics.getHeight() / 2 - 2 -vidas.getHeight());
        score = new Label("Puntuación: "+ Settings.SCORE, new Label.LabelStyle(bitmapfont, Color.WHITE));
        score.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 80, camera.position.y + Gdx.graphics.getHeight() / 2 - 2 - vidas.getHeight());

    }

    public void abrirCofre(){
        stage.addActor(mensajeCofre);
    }



    private void spawnZombie() {
        /*Rectangle zombieRect = new Rectangle();
        zombieRect.x = MathUtils.random(0, 800-46);
        zombieRect.y = MathUtils.random(0, 800-77);
        zombieRect.width = 46;
        zombieRect.height = 77;
        zombieDrops.add(zombieRect); */

        zombies = new Zombies(MathUtils.random(0, 800-46), MathUtils.random(0, 800-77), 46,77, bucket);
        stage.addActor(zombies);
        lastZombieTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cameraPosition();
        camera.update();
        tmr.setView(camera);
        tmr.render();
        stage.draw();
        stage.act(delta);
        vidas.setText("Vidas: "+ Settings.LIVES);
        score.setText("Puntuación: "+ Settings.SCORE);
        mensajeCofre.setText("PRESS [E] TO OPEN");
        //mensajeCofre.setSize(1000, 1000);
        controller.draw();
        if(TimeUtils.nanoTime() - lastZombieTime > 2000000000) spawnZombie();

        float cameraWidth = camera.viewportWidth;
        float cameraHeight = camera.viewportHeight;
        float buttonWidth = botonSalirSprite.getWidth();
        float buttonHeight = botonSalirSprite.getHeight();

        float x = camera.position.x - cameraWidth / 4 + buttonWidth / 2;
        float y = camera.position.y - cameraHeight / 2 + buttonHeight / 2;

        botonSalirSprite.setPosition(x+20, y+200);

        batch.begin();
        if (menuVisible) {
            batch.draw(menuSprite, x, y);
            botonSalirSprite.draw(batch);
        }
        batch.end();



        if(Settings.LIVES < 0){
            stage.dispose();
            game.setScreen(new MainMenuScreen(game));
        }


        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void cameraPosition() {
        if (Gdx.app.getType() == Application.ApplicationType.Android){
            if(bucket.getCollisionRectBucket().x < (mapWidthInPixels - (Gdx.graphics.getWidth() / 2) / 2) && bucket.getCollisionRectBucket().x > (Gdx.graphics.getWidth() / 2) / 2){
                camera.position.x = bucket.getCollisionRectBucket().x;
            }
            if(bucket.getCollisionRectBucket().y < (mapHeightInPixels - (Gdx.graphics.getHeight() / 2) / 2) && bucket.getCollisionRectBucket().y > (Gdx.graphics.getHeight() / 2) / 2) {
                camera.position.y = bucket.getCollisionRectBucket().y;
            }
        } else {
            if(bucket.getCollisionRectBucket().x < (mapWidthInPixels - (Gdx.graphics.getWidth() / 2)) && bucket.getCollisionRectBucket().x > (Gdx.graphics.getWidth() / 2)){
                camera.position.x = bucket.getCollisionRectBucket().x;
            }
            if(bucket.getCollisionRectBucket().y < (mapHeightInPixels - (Gdx.graphics.getHeight() / 2)) && bucket.getCollisionRectBucket().y > (Gdx.graphics.getHeight() / 2)) {
                camera.position.y = bucket.getCollisionRectBucket().y;
            }
            vidas.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 15, camera.position.y + Gdx.graphics.getHeight() / 2 - 2 -vidas.getHeight());
            score.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 80, camera.position.y + Gdx.graphics.getHeight() / 2 - 2 - score.getHeight());
            mensajeCofre.setPosition(camera.position.x - Gdx.graphics.getWidth() / 2 + 600, camera.position.y + Gdx.graphics.getHeight() / 2 - 200 - mensajeCofre.getHeight());
        }


    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(width, height, true);
        //super.resize(width, height);
        //controller.resize(width, height);
    }



    @Override
    public void show() {
        AssetManager.load();
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    menuVisible = !menuVisible;
                }
                return false;
            }


            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (menuVisible) {

                    Vector3 touchPos = new Vector3();
                    touchPos.set(screenX, screenY, 0);
                    camera.unproject(touchPos);
                    if (button == Input.Buttons.LEFT && botonSalirSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                        game.setScreen(new MainMenuScreen(game));
                    }

                }


                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }

        });


    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        AssetManager.dispose();
        tmr.dispose();
        map.dispose();
        batch.dispose();
        menuSprite.dispose();
    }

}
