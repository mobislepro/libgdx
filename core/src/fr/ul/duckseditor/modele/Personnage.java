package fr.ul.duckseditor.modele;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static  fr.ul.duckseditor.datafactory.Constant.*;
public class Personnage extends Acteur {
    protected Texture _texture;
    public Personnage(Monde monde,Texture texture,BodyDef.BodyType type,int x,int y) {
        super(PERSONNAGE_RAYON,PERSONNAGE_RAYON,monde);
        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.type=type;
        this.body=monde.getWorld().createBody(bodyDef);
        CircleShape shape=new CircleShape();
        shape.setRadius(PERSONNAGE_RAYON);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.density=25;
        fixtureDef.shape=shape;
        fixtureDef.restitution=0.1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    void draw(SpriteBatch sb) {
        sb.draw(_texture,body.getPosition().x,body.getPosition().y,PERSONNAGE_RAYON,PERSONNAGE_RAYON);
    }

}
