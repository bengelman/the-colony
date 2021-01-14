/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.drugs;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;
import thecolony.items.ItemAid;

/**
 *
 * @author pdogmuncher
 */
public class ItemAdrenaline extends ItemAid{
    public ItemAdrenaline(){
        super("Adrenaline Shot", "adrenaline.png", 0);
    }
    @Override
    public boolean use(EntityPlayer p){
        super.use(p);
        p.maxVelocity *= 1.5;
        p.health -= 10;
        if (p.adrenalineTimer > 0){
            p.health -= 50;
            WorldPanel.panel.showDialogue(new String[]{"Overdose of adrenaline"}, true);
        }
        else{
            try {
                p.heartbeat = new AudioPlayer("heartbeat.wav");
                p.heartbeat.play(-1);
            } catch (Exception ex) {
                Logger.getLogger(ItemAdrenaline.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        p.adrenalineTimer = 10 *  WorldPanel.TICKS_PER_SECOND;
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{"Adrenaline Shot", "Provides a temporary speed boost, but damages the user"};
    }
}
