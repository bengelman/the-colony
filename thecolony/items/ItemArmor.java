/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

import thecolony.WorldPanel;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class ItemArmor extends Item{
    public int protection;
    String desc;
    public ItemArmor(String name, String desc, String texture, String back, int protection){
        super(name, texture);
        this.backImage = back;
        this.protection = protection;
        this.desc = desc;
        flip = true;
    }
    
    @Override
    public boolean use(EntityPlayer user){
        super.use(user);
        user.armor = this;
        return false;
        //WorldPanel.panel.showDialogue(new String[]{name, "Damage Reduction: " + protection}, false);
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, desc, "Damage Reduction: " + protection};
    }
}
