/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import thecolony.ImageRegistry;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.behavior.MoveScript;

/**
 *
 * @author pdogmuncher
 */
public class Entity implements Serializable{
    public double x, y; 
    public int w, h;
    public int height;
    public boolean collidedLastTick = false;
    public double xvel = 0, yvel = 0;
    public World world;
    public String[] texture;
    //public int textureIndex = 0;
    public int age = 0;
    public boolean facingRight = false;
    public boolean noai = false;
    public boolean topLayer = false;
    
    
    public Entity(World world, double x, double y, int w, int h, int height, String[][] texture){
        this.world = world;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.height = height;
        this.texture = texture[(int)(texture.length * Math.random())];
    }
    public Entity(World world, double x, double y, int w, int h, int height, String[] texture){
        this.world = world;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.height = height;
        this.texture = texture;
    }
    
    public Rectangle getBounds(){
        return new Rectangle((int)x, (int)y, w, h);
    }
    
    public void setBounds(Rectangle rect){
        this.x = rect.x;
        this.y = rect.y;
        this.h = rect.height;
        this.w = rect.width;
    }
    public int getTextureIndex(){
        if (this.xvel != 0 || this.yvel != 0){
            return (age / 8) % 2 + 1;
        }
        else{
            return 0;
        }
    }
    public MoveScript moveScript = null;
    public void update(){
        collidedLastTick = false;
        age++;
        if (noai){
            return;
        }
        if (moveScript != null){
            if (WorldPanel.panel.currentTextQueue.isEmpty()){
                moveScript.step(this);
            }
            return;
        }
        Rectangle oldBounds;
        
        outer:
        for (int j = 0; j < Math.abs(xvel); j++){
            oldBounds = getBounds();
            this.x += xvel / Math.abs(xvel);
            for (int i = 0; i < world.entities.size(); i++){
                if (getBounds().intersects(world.entities.get(i).getBounds()) && !oldBounds.intersects(world.entities.get(i).getBounds())){
                    collidedLastTick = true;
                    Entity e = world.entities.get(i);
                    if (!(e instanceof EntityBullet)){
                        setBounds(oldBounds);
                    }
                    
                    collide(world.entities.get(i));
                    e.collide(this);
                    if (!(e instanceof EntityBullet)){
                        break outer;
                    }
                    
                    
                }
            }
        }
        blobs:
        for (int i = 0; i < Math.abs(yvel); i++){
            oldBounds = getBounds();
            this.y += yvel / Math.abs(yvel);
            for (int j = 0; j < world.entities.size(); j++){
                if (getBounds().intersects(world.entities.get(j).getBounds()) && !oldBounds.intersects(world.entities.get(j).getBounds())){
                    collidedLastTick = true;
                    Entity e = world.entities.get(j);
                    if (!(e instanceof EntityBullet)){
                        setBounds(oldBounds);
                    }
                    collide(world.entities.get(j));
                    e.collide(this);
                    if (!(e instanceof EntityBullet)){
                        break blobs;
                    }
                }
            }
        }
        if (xvel > 0){
            //facingRight = true;
        }
        else if (xvel < 0){
            //facingRight = false;
        }
    }
    public void collide(Entity e){
        
    }
    public BufferedImage getTexture(){
        if (facingRight){
            return ImageRegistry.getFlippedImage(texture[getTextureIndex()]);
        }
        try {
            return ImageRegistry.getImage(texture[getTextureIndex()], false);
        }catch(Exception e){
            System.out.println("Error with " + texture[0]);
        }
        return null;
    }
    public void drawEntity(Graphics g, int playerx, int playery, int screenw, int screenh){
        if (this.getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh))){
            g.drawImage(getTexture(), this.getPosOnScreen(playerx, playery).x,this.getPosOnScreen(playerx, playery).y, w, height, null);
        }
    }
    public boolean drawLinear = true;
    public void drawLinear(Graphics g, int playerx, int playery, int screenw, int screenh){
        if (drawLinear){
            if (getPosOnScreen(playerx, playery).intersects(new Rectangle(0, 0, screenw, screenh))){
                g.drawImage(getTexture(), getPosOnScreen(playerx, playery).x, screenh - height, w, height, null);
            }
        }
    }
    public Rectangle getPosOnScreen(int playerx, int playery){
        Rectangle tangle = new Rectangle((int)world.getXOnScreen(x), (int)world.getYOnScreen(y), w, height);
        tangle.y += (h - height);
        return tangle;
    }
    public void onInteract(EntityPlayer player){
        
    }
        
        
}
