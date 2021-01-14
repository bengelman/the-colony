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
public class ItemNoctaine extends ItemAid{
    public ItemNoctaine(){
        super("Noctaine", "noctaine.png", 0);
    }
    public boolean use(EntityPlayer p){
        super.use(p);
        p.timeWithoutNoctaine = 0;
        p.health = p.maxHealth;
        if (p.noctaineLevel == 0){
            if (Math.random() > 0.5){
                p.noctaineLevel = 1;
                p.maxHealth -= 10;
            }
        }
        else if (Math.random() < 0.2 && p.noctaineLevel < 9){
            p.noctaineLevel++;
        }
        if (p.noctaineTimer > 0){
            p.health += 50;
            WorldPanel.panel.showDialogue(new String[]{"Overdose of noctaine"}, true);
            p.noctaineLevel++;
        }
        if (p.health > p.maxHealth){
            p.health = p.maxHealth;
        }
        
        p.noctaineTimer = 24 * WorldPanel.TICKS_PER_SECOND;
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, "Restores the user to max health, but is addictive and damages vision"};
    }
}
