package fr.ul.duckseditor.modele;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.duckseditor.datafactory.TextureFactory;

public class Duck {
    private Monde monde;
    private Texture duck;
    private Body duckBody;
    public Duck(Monde monde)
    {
        this.monde=monde;
        World world=monde.getWorld();
        //debugRenderer =new Box2DDebugRenderer();
        FixtureDef fixtureDef=new FixtureDef();
        PolygonShape shape=new PolygonShape();
        BodyDef bodyDef=new BodyDef();
        duck= TextureFactory.getDuck();
        bodyDef=new BodyDef();
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(duck.getWidth()/2,60+duck.getHeight());
        duckBody=world.createBody(bodyDef);
        CircleShape sha=new CircleShape();
        sha.setRadius(20f);

        fixtureDef=new FixtureDef();
        fixtureDef.density=10;
        fixtureDef.shape=sha;
        duckBody.createFixture(fixtureDef);

    }
    public void draw(SpriteBatch sb)
    {
        sb.draw(duck,duckBody.getPosition().x,duckBody.getPosition().y,40,40);
    }

    public Body getDuckBody() {
        return duckBody;
    }
}
