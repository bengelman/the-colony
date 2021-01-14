/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.chips;

import java.util.logging.Level;
import java.util.logging.Logger;
import thecolony.AudioPlayer;
import thecolony.WorldPanel;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author brend
 */
public class ItemQuickdrawChip extends ItemChip{
    int cooldown = 0;
    public ItemQuickdrawChip(String name, String texture) {
        super(name, texture, new String[]{name, "Allows the user to quickly fire a precise shot from their sidearm"});
    }


    @Override
    public void activate(EntityPlayer user) {
        
        if (cooldown == 0){
            cooldown = 600;
            final EntityPlayer theUser = user;
            WorldPanel.panel.addAnim("highnoon", 30, 10, new Runnable(){

                @Override
                public void run() {
                    fireBullet(theUser);
                }
            });
            //WorldPanel.panel.quickdrawTimer = 30;
        }
        else{
            WorldPanel.panel.addMessage(new String[]{"Item cooldown: " + cooldown / 20 + " seconds"});
        }
        
    }

    @Override
    public void onChipUpdate(EntityPlayer user) {
        
        if (cooldown > 0){
            cooldown--;
        }
    }
    public static void fireBullet(EntityPlayer firer){
        double x = firer.world.getXInWorld(WorldPanel.pointerLocation.x) - (firer.x + firer.w / 2.0D);
        double y = firer.world.getYInWorld(WorldPanel.pointerLocation.y) - firer.y;
        double angle = Math.atan((double)y / (double)x);
        
        if (x < 0){
            angle += Math.PI;
        }
        
        double indAngle = angle;
        EntityBullet bullet = new EntityBullet(firer.world, firer.x + firer.w / 2, firer.y, 3, 75, 700, 100, false, firer, null, EntityBullet.PLASMA, false, false);
        bullet.xvel = Math.cos(indAngle) * 100;
        bullet.yvel = Math.sin(indAngle) * 100;
        //bullet.x += Math.cos(indAngle) * (firer.w / 2);
        //bullet.y += Math.sin(indAngle) * (firer.h / 2);
        firer.world.entities.add(bullet);
        bullet.update();
        try {
            new AudioPlayer("plasma.wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(EntityBullet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
