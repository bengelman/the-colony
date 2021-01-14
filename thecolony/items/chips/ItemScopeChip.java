/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.chips;

import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author brend
 */
public class ItemScopeChip extends ItemChip{

    public ItemScopeChip(String name, String texture) {
        super(name, texture, new String[]{name, "Provides the user with an enhanced targeting system for firearms"});
    }


    @Override
    public void activate(EntityPlayer user) {
        
    }

    @Override
    public void onChipUpdate(EntityPlayer user) {
        
    }
    
}
