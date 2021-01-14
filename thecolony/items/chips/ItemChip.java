/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.chips;

import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;
import thecolony.items.Item;

/**
 *
 * @author pdogmuncher
 */
public abstract class ItemChip extends Item{
    public String[] desc;
    public ItemChip(String name, String texture, String[] desc){
        super(name, texture);
        this.desc = desc;
        flip = false;
    }
    public boolean use(EntityPlayer user){
        super.use(user);
        user.chip = this;
        return false;
    }
    public abstract void onChipUpdate(EntityPlayer user);
    @Override
    public String[] getMouseover(){
        return desc;
    }
    public abstract void activate(EntityPlayer user);
}
