/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.drugs;

import java.util.Random;
import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;
import thecolony.items.ItemAid;

/**
 *
 * @author pdogmuncher
 */
public class ItemExenol extends ItemAid{
    public ItemExenol(){
        super("Exenol", "exenol.png", 10);
    }
    public boolean use(EntityPlayer p){
        super.use(p);
        p.timeWithoutExenol = 0;
        
        if (p.exenolLevel == 0){
            p.exenolLevel = 1;
            p.maxHealth -= 10;
        }
        else if (Math.random() < 0.2 && p.exenolLevel < 9){
            p.exenolLevel++;
        }
        WorldPanel.panel.gameloop.setDelay(2000 / WorldPanel.TICKS_PER_SECOND);
        if (WorldPanel.panel.exenolTimer > 0){
            p.health -= 50;
            WorldPanel.panel.showDialogue(new String[]{"Overdose of exenol"}, true);
            WorldPanel.panel.gameloop.setDelay(100);
            p.exenolLevel++;
        }
            
        
        WorldPanel.panel.exenolTimer = 24 * WorldPanel.TICKS_PER_SECOND;
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, "Slows down time, but is addictive"};
    }
}
