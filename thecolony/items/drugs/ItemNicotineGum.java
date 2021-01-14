/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.drugs;

import thecolony.entity.EntityPlayer;
import thecolony.items.ItemAid;

/**
 *
 * @author pdogmuncher
 */
public class ItemNicotineGum extends ItemAid{
    public ItemNicotineGum(){
        super("Nicotene Gum", "gum.png", 20);
    }
    public boolean use(EntityPlayer p){
        super.use(p);
        p.timeWithoutCigarette = 0;
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, "Restores 20 health", "Removes nicotene withdrawl symptoms"};
    }
}
