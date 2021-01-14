/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.chips;

import thecolony.WorldPanel;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author brend
 */
public class ItemDetectiveChip extends ItemChip{

    public ItemDetectiveChip(String name, String texture) {
        super(name, texture, new String[]{name, "Improves the user's problem-solving and observation skills on use"});
    }


    @Override
    public void activate(EntityPlayer user) {
        if (user.world.altedHint && EntityFighter.groups.get("chahEnemies").contains(EntityPlayer.class) && user.world.name.equals("haven")){
            WorldPanel.panel.addMessage(new String[]{"Wait a second... Didn't the Tenn cartel build a tunnel to a Triad base? That could be the base where Normac is."});
        }
        else{
            WorldPanel.panel.addMessage(new String[]{user.world.tips});
        }
        if (user.world.name.equals("convenience store")){
            WorldPanel.panel.currentWorlds.get("convenience store").unlock();
            WorldPanel.panel.nextObjective = "Talk to the cashier at Toys R OK";
        }
        
    }

    @Override
    public void onChipUpdate(EntityPlayer user) {
        
    }
    
}
