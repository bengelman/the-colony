/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import thecolony.ImageRegistry;
import thecolony.entity.EntityLiving;

/**
 *
 * @author pdogmuncher
 */
public class Item implements Serializable, Cloneable, Comparable{
    public String image, backImage;
    public String name;
    public boolean flip = false;
    
    
    public Item(String name, String texture){
        this.name = name;
        image = texture;
    }
    public boolean use(EntityPlayer user){
        return false;
    }
    public String[] getMouseover(){
        return null;
    }
    public BufferedImage getImage(Entity user, boolean inventory){
        if (inventory){
            return ImageRegistry.getImage(image, false);
        }
        if (user.facingRight && flip) {
            if (user instanceof EntityLiving){
                if (((EntityLiving)user).facingBack){
                    return ImageRegistry.getFlippedImage(backImage);
                }
            }
            return ImageRegistry.getFlippedImage(image);
        }
        if (user instanceof EntityLiving && flip){
            if (((EntityLiving)user).facingBack){
                return ImageRegistry.getImage(backImage, false);
            }
        }
        return ImageRegistry.getImage(image, false);
    }
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int compareTo(Object o) {
        return name.compareTo(o.toString());
    }

}
