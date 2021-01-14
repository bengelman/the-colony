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
public class ItemCigarette extends ItemAid{
    public ItemCigarette(){
        super("Cigarette", "cigarette.png", 0);
    }
    public boolean use(EntityPlayer p){
        super.use(p);
        p.timeWithoutCigarette = 0;
        p.totalTimeWithoutCigarette = 0;
        if (Math.random() < 0.2 && !p.nicoteneAddict){
            p.maxHealth -= 10;
            p.nicoteneAddict = true;
        }
            
        
        p.cigaretteTimer = 24 * WorldPanel.TICKS_PER_SECOND;
        WorldPanel.panel.addMessage(new String[]{p.world.tips});
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, "Provides a hint about the current area and negates recoil on firearms, but is addictive"};
    }
}
