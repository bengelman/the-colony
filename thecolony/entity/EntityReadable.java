/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import thecolony.World;
import thecolony.WorldPanel;

/**
 *
 * @author brend
 */
public class EntityReadable extends Entity{
    String[] message;
    public EntityReadable(World world, double x, double y, int w, int h, int height, String[] texture, String[] message) {
        super(world, x, y, w, h, height, texture);
        this.message = message;
    }
    @Override
    public void onInteract(EntityPlayer player){
        WorldPanel.panel.addMessage(message);
    }
}
