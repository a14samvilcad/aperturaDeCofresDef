package m08.uf3.drops.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import m08.uf3.drops.helper.AssetManager;
import m08.uf3.drops.Screens.GameScreen;
import  m08.uf3.drops.Utils.Settings;


public class Zombies extends Actor {

    private Vector2 position;
    private int width, height;
    int vida = 3;

    private Player player;

    private TextureRegion[] animacionStatic;

    private int currentFrame = 0;
    private float frameTime = 0.1f;
    private float stateTime = 0;

    private Rectangle collisionRect;

    private Vector2 velocity = new Vector2();
    private float speed = 35;

    public Zombies (float x, float y, int width, int height, Player player){
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        position = new Vector2(x, y);
        this.player = player;

        collisionRect = new Rectangle();
        collisionRect.x = x;
        collisionRect.y = y;
        collisionRect.width = this.width;
        collisionRect.height = this.height;

        animacionStatic = AssetManager.ZombieGhoulStaticAnimation;

        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    public void act(float delta){
        super.act(delta);

        Vector2 playerPosition = new Vector2(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
        Vector2 zombiePosition = new Vector2(position.x + width / 2, position.y + height / 2);

        velocity = playerPosition.sub(zombiePosition).nor().scl(speed);

        position.add(velocity.x * delta, velocity.y * delta);

        //Colision personaje con los bordes del mapa
        if (this.position.y <= 5){
            this.position.y = 5;
        }
        if (this.position.x <= 5){
            this.position.x = 5;
        }
        if (this.position.x >= 1600 - this.width - 5){
            this.position.x = 1600 - this.width - 5;
        }
        if (this.position.y >= 950 - this.height - 2){
            this.position.y = 950 - this.height- 2;
        }

        collisionRect.x = this.position.x;
        collisionRect.y = this.position.y;
        collisionRect.width = this.width;
        collisionRect.height = this.height;

        if(vida == 0){
            dispose();
        }

        stateTime += delta;
        if (stateTime >= frameTime){
                currentFrame++;
            stateTime = 0;
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getZombieDirection(), this.position.x, this.position.y, width, height);

    }

    private TextureRegion getZombieDirection() {
        TextureRegion zombieDir = null;
        if (currentFrame < 0 || currentFrame >= animacionStatic.length) {
            currentFrame = 0;
        }
        zombieDir = animacionStatic[currentFrame];

        return zombieDir;
    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void dispose() {this.remove();}
}