package fr.ul.duckseditor.modele;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.ul.duckseditor.control.FileChooser;
import fr.ul.duckseditor.datafactory.Constant;
import fr.ul.duckseditor.datafactory.TextureFactory;
import fr.ul.duckseditor.view.EditorScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static fr.ul.duckseditor.datafactory.Constant.*;

public class Monde {
    private World world;
    private Heros heros;
    //private Body groundBody;
    private ZoneDeJeu zoneDeJeu;

    private Acteur prisonnier;
    private Acteur bandit;

    private Acteur play;
    private Acteur stop;

    private Acteur trash;

    private Acteur carre;
    private Acteur rectangle;

    private  Acteur save;
    private  Acteur load;
    private  Acteur rewrite;

    private Acteur left;
    private Acteur rigth;
    private Panel panel;

    private OrthographicCamera camera;

    private List<Acteur> objectOnSurface=new ArrayList<Acteur>();
    private  List<Acteur>btns=new ArrayList<Acteur>();
    private List<Body> toDelete=new ArrayList<Body>();

    public Monde(OrthographicCamera camera)
    {
        world=new World(new Vector2(0,0f),true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        this.camera=camera;
        heros=new Heros(this,WORLD_WIDTH/2,WORLD_HEIGTH/2);
        //border();
        zoneDeJeu=new ZoneDeJeu(this);
        panel=new Panel(this);
        trash=new Bouton(this,panel.posX,panel.posY);
        ((Bouton) trash).setTexture(TextureFactory.getTrash());
        btns.add(trash);
        bandit=new Bandit(this,(int)(trash.getX()),(int) (trash.getHauteur()+trash.getY()+1));
        bandit.getBody().setType(BodyDef.BodyType.StaticBody);
        btns.add(bandit);
        prisonnier=new Prisonnier(this,(int)(bandit.getX()),(int) (bandit.getHauteur()+bandit.getY()+1));
        prisonnier.getBody().setType(BodyDef.BodyType.StaticBody);
        btns.add(prisonnier);
        carre=new Carre(this, BodyDef.BodyType.StaticBody,(int)(bandit.getX()+1),(int) (prisonnier.getHauteur()+prisonnier.getY()+1));
        btns.add(carre);
        rectangle=new Rectangulaire(this, BodyDef.BodyType.StaticBody,(int)(carre.getX()),(int) (carre.getHauteur()+carre.getY()+1));
        btns.add(rectangle);
        load=new Bouton(this,0,(int)(rectangle.getHauteur()+rectangle.getY()+3));
        ((Bouton) load).setTexture(TextureFactory.getLoad());
        btns.add(load);
        save=new Bouton(this,(int)(load.getX()+load.getLargeur()+2),(int)(load.getY()));
        ((Bouton) save).setTexture(TextureFactory.getSave());
         btns.add(save);
        rewrite=new Bouton(this,(int)(carre.getX()+1),(int)(save.getY()+save.getHauteur()+3));
        ((Bouton) rewrite).setTexture(TextureFactory.getRewrite());
        btns.add(rewrite);
        play=new Bouton(this,(int)(carre.getX()),(int)(rewrite.getY()+rewrite.getHauteur()+5));
        ((Bouton) play).setTexture(TextureFactory.getPlay());
        btns.add(play);
    }

    public World getWorld() {
        return world;
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
    public Acteur getPlay() {
        return play;
    }
    public Acteur getStop() {
        return stop;
    }
    public Acteur getTrash() {
        return trash;
    }
    public Acteur getSave() {
        return save;
    }
    public Acteur getLoad() {
        return load;
    }
    public Acteur getRewrite() {
        return rewrite;
    }
    public Acteur getLeft() {
        return left;
    }
    public Acteur getRigth() {
        return rigth;
    }
    public List<Acteur> getActeurOnSurface() {
        return objectOnSurface;
    }
    public List<Body> getToDelete() {
        return toDelete;
    }
    public void setToDelete(List<Body> toDelete) {
        this.toDelete = toDelete;
    }
    public Panel getPanel() {
        return panel;
    }
    public Heros getHeros() {
        return heros;
    }
    public ZoneDeJeu getZoneDeJeu() {
        return zoneDeJeu;
    }
    public Acteur getPrisonnier() {
        return prisonnier;
    }
    public Acteur getBandit() {
        return bandit;
    }
    public Acteur getCarre() {
        return carre;
    }
    public Acteur getRectangle() {
        return rectangle;
    }
    public List<Acteur> getObjectOnSurface() {
        return objectOnSurface;
    }
    public List<Acteur> getBtns() {
        return btns;
    }
    public void render(float delta, SpriteBatch sb)
    {
        heros.draw(sb);
        for(Body body:toDelete)
        {
            Acteur act=null;
            for(Acteur acteur:getActeurOnSurface())
            {
                if(acteur.getBody()==body)
                {
                    act=acteur;
                    world.destroyBody(body);//destruction de body
                }
            }
            if(act!=null) {
                getObjectOnSurface().remove(act);//l'enlever des objet a afficher
            }
        }
        getWorld().step(Gdx.graphics.getDeltaTime(),6,2);;
         draw(sb);
       for(Acteur o:objectOnSurface)
       {
           o.draw(sb);

       }

    }
    public void draw(SpriteBatch sb)
    {
        panel.draw(sb);

        for(Acteur acteur:btns)
        {
            acteur.draw(sb);
        }
    }

    /*public void border()
    {

        BodyDef bodyDef=new BodyDef();
        bodyDef.type=BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, Constant.WORLD_HEIGTH/6);
        PolygonShape shape=new PolygonShape();
        //float[] points={0,WORLD_HEIGTH/6,0,WORLD_HEIGTH,WORLD_WIDTH,WORLD_HEIGTH,WORLD_WIDTH,WORLD_HEIGTH/6};
        //shape.set(points);
        shape.setAsBox(WORLD_WIDTH,WORLD_HEIGTH);
        groundBody=world.createBody(bodyDef);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.density=0;
        fixtureDef.shape=shape;
        groundBody.createFixture(fixtureDef);
        shape.dispose();
    }*/
    public boolean isActeurInSurface(Body body)
    {
        for(Acteur object:objectOnSurface)
        {
            if(body==object.getBody())
            {
                return true;
            }
        }
        return false;
    }

    public Pixmap screenShot()
    {
        byte[] pixels= ScreenUtils.getFrameBufferPixels(Gdx.graphics.getWidth()/5,0,Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight(),true);
        for(int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth()*4/5, Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        return pixmap;
    }
    public void save(String filename)
    {
        PixmapIO.writePNG(Gdx.files.absolute( filename+".png"),screenShot());
        FileHandle fileHandle=Gdx.files.absolute( filename+".mdl");
        fileHandle.writeString(savePos(),false);
    }
    public String savePos()
    {
        Json json=new Json();
        String strJson="";
        for(Acteur acteur:getActeurOnSurface()) {
            strJson += acteur.getType() + ";" + acteur.getBody().getPosition().x + ";" + acteur.getBody().getPosition().y + ";" + acteur.largeur + ";" + acteur.hauteur + "\n";
        }
        return  strJson;
    }
    public void load(String filename)
    {
        String[] str=Gdx.files.absolute(filename).readString().split("\n");
        objectOnSurface=new ArrayList<Acteur>();
        for(String line:str)
        {
            String[] att=line.split(";");
            if(att.length!=5)
                continue;
            String className=att[0];
            float x=Float.parseFloat(att[1]);
            float y=Float.parseFloat(att[2]);
            Acteur o=null;
            if (className.trim().compareToIgnoreCase(Carre.class.toString())==0) {
                o =new Carre(this, BodyDef.BodyType.DynamicBody,x,y);

            } else if (className.trim().compareToIgnoreCase(Rectangulaire.class.toString())==0) {
                o =new Rectangulaire(this, BodyDef.BodyType.DynamicBody,x,y);
            }else if(className.trim().compareToIgnoreCase(Bandit.class.toString())==0){
                o=new Bandit(this,x,y);
            }else if(className.trim().compareToIgnoreCase(Prisonnier.class.toString())==0){
                o=new Prisonnier(this,x,y);
            }
            objectOnSurface.add(o);
        }
    }

}
