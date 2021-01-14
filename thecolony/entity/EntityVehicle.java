/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.Point;
import thecolony.entity.behavior.AIFighter;
import thecolony.entity.behavior.AIVehicle;
import thecolony.items.Item;
import thecolony.items.ItemWeapon;
import thecolony.World;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public class EntityVehicle extends EntityLiving{
    public EntityLiving rider = null;
    public Point offset;
    public ItemWeapon prevWeapon;
    public ItemWeapon[] newWeapons;
    public EntityVehicle(World world, int x, int y, int w, int h, int height, Point offset, String[][] textures, int health, int speed, ItemWeapon[] newWeapons){
        super(world, x, y, w, h, height, textures, health, speed, new AIVehicle(), 0);
        ((AIVehicle)ai).v = this;
        this.offset = offset;
        this.newWeapons = newWeapons;
    }
    public void mount(EntityLiving p){
        if (rider == null){
            p.dismount();
            //WorldPanel.panel.addMessage(new String[]{"Hmmm, no key in the ignition...", "*Bzzt bzzt*", "There, hotwired it"});
            rider = p;
            rider.riding = this;
            rider.x = this.x;
            rider.y = this.y;
            rider.w = this.w;
            rider.h = this.h;
            rider.maxVelocity = this.maxVelocity;
            for (Item i : inventory){
                rider.inventory.add(i);
            }
            inventory.clear();
            if (newWeapons.length > 0){
                prevWeapon = rider.weapon;
                rider.weapon = newWeapons[0];
                //if (rider.ai instanceof AIFighter){
                    //((AIFighter)rider.ai).firearm = newWeapons[0];
                //}
                for (ItemWeapon weapony : newWeapons){
                    rider.inventory.add(weapony);
                }
                
            }
            
        }
        else if (p == rider){
            rider.dismount();
        }
    }
    @Override
    public void onInteract(EntityPlayer p){
        mount(p);
    }
    @Override
    public void update(){
        super.update();
        if (this.health <= 0){
            rider.dismount();
        }
        if (rider != null){
            this.facingBack = rider.facingBack;
            this.facingRight = rider.facingRight;
        }
        ai.onUpdate();
    }
}
