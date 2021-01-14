/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.object;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import thecolony.Main;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import thecolony.entity.Entity;
import thecolony.entity.EntityPlayer;
import thecolony.items.Item;

/**
 *
 * @author pdogmuncher
 */
public class EntityStolenItems extends Entity{
    
    public EntityStolenItems(World world, int x, int y, int w, int h, int height, String[] texture){
        super(world, x, y, w, h, height, texture);
        
    }
    @Override
    public void onInteract(EntityPlayer p){
        super.onInteract(p);
        if (p.stolenInventory.size() != 0){
            for (Item i : p.stolenInventory){
                p.inventory.add(i);
            }
            p.stolenInventory.clear();
            WorldPanel.panel.addMessage(new String[]{"zayvehead.png Hey! This is all my stuff!"});
        }
            
    }
}
