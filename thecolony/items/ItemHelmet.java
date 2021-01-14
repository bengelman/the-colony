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
public class ItemHelmet extends Item{
    public int protection;
    String desc;
    public ItemHelmet(String name, String desc, String texture, String back, int protection){
        super(name, texture);
        this.backImage = back;
        this.protection = protection;
        this.desc = desc;
        flip = true;
    }
    @Override
    public boolean use(EntityPlayer user){
        super.use(user);
        user.helmet = this;
        if (this.image.startsWith("cybermask") || this.image.equals("mask.png")){
            this.image = "cybermask1.png";
        }
        return false;
        //WorldPanel.panel.showDialogue(new String[]{name, "Damage Reduction: " + protection}, false);
    }
    public void equippedUpdate(EntityLiving user){
        if (this.image.equals("cybermask1.png") && WorldPanel.panel.totalTime % 5 == 0){
            image = "cybermask2.png";
        }
        else if (this.image.equals("cybermask2.png") && WorldPanel.panel.totalTime % 5 == 0){
            image = "cybermask3.png";
        }
        else if (image.equals("cybermask3.png") && WorldPanel.panel.totalTime % 5 == 0){
            image = "mask.png";
        }
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, desc, "Damage Reduction: " + protection};
    }
        
}
