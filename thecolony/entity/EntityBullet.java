/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import thecolony.entity.EntityLiving;
import thecolony.entity.Entity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.World;

/**
 *
 * @author pdogmuncher
 */
public class EntityBullet extends Entity{
    int damage, range;
    boolean ricochet;
    Point lastPos;
    EntityLiving firer;
    boolean explodey = false;
    boolean clusterBomb = false;
    String texturey;
    Color color;
    double velocity;
    public static final Color PLASMA = new Color(251F / 255F, 0F, 212F / 255F);
    public static final Color LASER = new Color(102F / 255F, 204F / 255F, 1F);
    public static final Color BULLET = new Color(191F / 255F, 136F / 255F, 6F / 255F);
    public EntityBullet(World world, double x, double y, int size, int damage, int range, double velocity, boolean ricochet, EntityLiving firer, String texture, Color color, boolean explodey, boolean clusterBomb){
        super(world, x, y, size, size, size, new String[][]{{texture + ".png", texture + ".png", texture + ".png"}});
        if (texture == null){
            this.texture = null;
        }
        this.color = color;
        this.damage = damage;
        this.range = range;
        this.velocity = velocity;
        this.ricochet = ricochet;
        this.firer = firer;
        this.explodey = explodey;
        this.clusterBomb = clusterBomb;
        texturey = texture;
    }
    public void update(){
        super.update();
        if (age > (range / velocity)){
            if (explodey){
                world.entities.add(new EntityExplosion(world, x, y, damage * 7, 0, damage * 7, firer, damage, "exp"));
            }
            world.entities.remove(this);
        }
            
    }
    public void drawEntity(Graphics g, int playerx, int playery, int screenw, int screenh){
        if (texture != null){
            super.drawEntity(g, playerx, playery, screenw, screenh);
            return;
        }
        
        int startX = (int)((xvel) * (10 / Math.sqrt(Math.pow((xvel), 2) + Math.pow((yvel), 2))));
        int startY = (int)((yvel) * (10 / Math.sqrt(Math.pow((xvel), 2) + Math.pow((yvel), 2))));
        //if (texture[0].equals("laser.png")){
            
        g.setColor(color);
        Stroke stroke = ((Graphics2D)g).getStroke();
        ((Graphics2D)g).setStroke(new BasicStroke(w));
        
        g.drawLine((int)this.getPosOnScreen(playerx, playery).x + startX, (int)this.getPosOnScreen(playerx, playery).y + startY, (int)((int)this.getPosOnScreen(playerx, playery).x) - startX,(int) ((int)this.getPosOnScreen(playerx, playery).y) - startY);
        ((Graphics2D)g).setStroke(stroke);
        //}
    }
    public void collide(Entity e){
        super.collide(e);
        if (e == firer){
            return;
        }
        if (!(e instanceof EntityBullet)) {
            if (ricochet && !(e instanceof EntityLiving)){
                if (new Rectangle((int)(x + (xvel / Math.abs(xvel)) * 1), (int)y, w, h).intersects(e.getBounds())){
                    xvel = -xvel;
                }
                else if (new Rectangle((int)x, (int)(y + (yvel / Math.abs(yvel)) * 1), w, h).intersects(e.getBounds())){
                    yvel = -yvel;
                }
            }
            else {
                this.world.entities.remove(this);
            }
        }
        if (e instanceof EntityLiving && e != firer){
            ((EntityLiving)e).takeDamage(damage, firer);
            
        }
        if (explodey && !(e instanceof EntityBullet)){
            world.entities.add(new EntityExplosion(world, x, y, damage * 7, 0, damage * 7, firer, damage, "exp"));
        }
        if (clusterBomb && !(e instanceof EntityBullet) && Math.floor(Math.sqrt(xvel * xvel + yvel * yvel) / 2) > 0){
            for (int i = 0; i < 4; i++){
                int velocity = (int)Math.sqrt(xvel * xvel + yvel * yvel);
                EntityBullet b = new EntityBullet(world, e.x + e.w / 2, e.y + (e.h / 2), (int)Math.ceil(w / 2), (int)Math.ceil(damage / 2), range, velocity, ricochet, firer, texturey, color, explodey, true);
                switch(i){
                    case 0:
                        b.xvel = velocity / 2;
                        break;
                    case 1:
                        b.yvel = velocity / 2;
                        break;
                    case 2:
                        b.xvel = -velocity / 2;
                        break;
                    case 3:
                        b.yvel = -velocity / 2;
                        break;
                }
                world.entities.add(b);
            }
        }
    }
}
