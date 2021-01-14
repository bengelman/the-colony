/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class ItemAid extends Item{
    public int healing;
    public ItemAid(String name, String texture, int protection){
        super(name, texture);
        this.healing = protection;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{this.name, "Restores " + healing + " health"};
    }
    @Override
    public boolean use(EntityPlayer user){
        super.use(user);
        user.health += healing;
        try {
            new AudioPlayer("heal.wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(ItemAid.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (user.health > user.maxHealth){
            user.health = user.maxHealth;
        }
        user.inventory.remove(this);
        return true;
    }
}
