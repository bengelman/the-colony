/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.World;

/**
 *
 * @author pdogmuncher
 */
public class EntityExplosion extends Entity{
    EntityLiving starter;
    int damage;
    
    public EntityExplosion(World world, double x, double y, int w, int h, int height, EntityLiving starter, int damage, String type){
        super(world, x, y, w, h, height, new String[]{
            type + "0.png",
            type + "1.png",
            type + "2.png",
            type + "3.png",
            type + "4.png"
        });
        this.starter = starter;
        new AudioPlayer("bang.wav").play(0);
        this.damage = damage;
    }
    @Override
    public BufferedImage getTexture(){
        return ImageRegistry.getImage(texture[age / 2], false);
    }
    @Override
    public void update(){
        super.update();
        if (age >= 10){
            world.entities.remove(this);
            return;
        }
        Rectangle radius = new Rectangle(((int)x) - 60, ((int)y) - 60, w + 120, h + 120);
        for (int i = 0; i < world.entities.size(); i++){
            if (world.entities.get(i) instanceof EntityLiving){
                EntityLiving l = (EntityLiving) world.entities.get(i);
                if (radius.intersects(l.getBounds())){
                    l.takeDamage(damage, starter);
                    
                }
            }
        }
    }
}
