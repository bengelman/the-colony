/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.object;

import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import java.awt.Point;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.Entity;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class EntityBed extends Entity{
    public EntityBed(World world, int x, int y, int w, int h, int height, String[] texture){
        super(world, x, y, w, h, height, texture);
        
    }
    public void onInteract(EntityPlayer p){
        super.onInteract(p);
        p.health = p.maxHealth;
        WorldPanel.panel.showImage(new String[]{"cutportal.png"});
        WorldPanel.panel.exenolTimer = 1;
        
    }
        
}
