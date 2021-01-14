/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.object;

import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.Entity;
import thecolony.entity.EntityNPC;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class EntityCutscene extends Entity{
    Point point;
    String destinationName;
    String image;
    boolean hasPlayed = false;
    public EntityCutscene(World world, int x, int y, int w, int h, int height, String[] texture, String destination, Point destinationLocation, String image){
        super(world, x, y, w, h, height, texture);
        this.point = destinationLocation;
        this.destinationName = destination;
        this.image = image;
    }
    @Override
    public void collide (Entity e){
        if (!WorldPanel.panel.currentTextQueue.isEmpty())return;
        if (e == WorldPanel.panel.player){
            EntityPlayer ep = (EntityPlayer) e;
            if (ep.world.name.equals("base")){
                ItemHelmet helmets = new ItemHelmet("CyberMask", "Can expand over the wearer's face at will.", "cybermask1.png", "maskflip.png", 4);
                ItemArmor inmare = new ItemArmor("Inmate Uniform", "A uniform for prison inmates.", "inmateoutfit.png", "inmateflipped.png", 2);
                ItemWeapon shank = new ItemWeapon("Rock", "rock.png", "A rock. Pretty useless.", 3, 100);
                ep.stolenInventory = new ArrayList<>(ep.inventory);
                ep.inventory.clear();
                ep.inventory.add(helmets);
                ep.inventory.add(inmare);
                ep.inventory.add(shank);
                ep.weapon = shank;
                ep.helmet = helmets;
                ep.armor = inmare;
                WorldPanel.panel.nextObjective = "Escape the prison";
            }
            if (destinationName.equals("cannon1") && ep.world.name.equals("hangar")){
                WorldPanel.panel.nextObjective = "Destroy the cannon control panels";
            }
            final World destination = WorldPanel.panel.currentWorlds.get(destinationName);
            final EntityPlayer player = (EntityPlayer) e;
            String[] images = new String[]{image};
            
            if (player.world.name.equals("base")){
                images = new String[]{image, "prisontransport.png"};
            }
            player.lastx = player.x;
            player.lasty = player.y;
            player.lastWorld = player.world.name;
            player.x = point.x;
            player.y = point.y;
            player.xvel = 0;
            player.yvel = 0;
            player.world.unload();
            destination.loadWorld(player);
            if (!hasPlayed){
                WorldPanel.panel.showImage(images);
                hasPlayed = true;
            }
            WorldPanel.panel.saveGame();
            try {
                new AudioPlayer("click.wav").play(0);
            } catch (Exception ex) {
                Logger.getLogger(EntityCutscene.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
