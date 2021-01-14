/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.object;

import thecolony.entity.Entity;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.Entity;
import thecolony.entity.EntityNPC;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class EntityPortal extends Entity{
    Point point;
    String destinationName;
    public static boolean playedTenn = false;
    
    public EntityPortal(World world, int x, int y, int w, int h, int height, String[] texture, String destination, Point destinationLocation){
        super(world, x, y, w, h, height, texture);
        this.point = destinationLocation;
        this.destinationName = destination;
        if (destinationName.equals("glitch")){
            this.texture = new String[]{"locked.png"};
        }
    }
    @Override
    public void collide (final Entity e){
        if (destinationName.equals("glitch")) {
            return;
        }
        if (!WorldPanel.panel.currentTextQueue.isEmpty()) {
            return;
        }
        if (e == WorldPanel.panel.player){
            
            final World destination = WorldPanel.panel.currentWorlds.get(destinationName);
            if (destination.name.equals("bunker")){
                World wo = WorldPanel.panel.currentWorlds.get("haven market");
                for (int i = 0; i < wo.entities.size(); i++){
                    if (wo.entities.get(i) instanceof EntityNPC){
                        wo.entities.remove(i);
                        break;
                    }
                }
            }
            if (e.world.name.equals("prison")){
                WorldPanel.panel.addMessage(new String[]{"normachead.png Clear out this hangar while I try to get one of these ships running."});
            }
            if (e.world.name.equals("jmishtin house")){
                ((EntityPlayer)e).frozen = true;
                WorldPanel.panel.currentWorlds.get("haven suburbs").lock();
                WorldPanel.panel.addMessage(new String[]{"zayvehead.png And Jmishtin, just so you know, I'm actually a private investiagator. Hasta la vista.", "jmishtin.png Gosh golly, I'm dead."});
                WorldPanel.panel.runOnText = new Runnable(){
                    @Override
                    public void run() {
                        final EntityPlayer player = (EntityPlayer) e;
                        ((EntityPlayer)e).frozen = false;
                        player.lastx = player.x;
                        player.lasty = player.y;
                        player.lastWorld = player.world.name;
                        player.x = point.x;
                        player.y = point.y;
                        player.xvel = 0;
                        player.yvel = 0;
                        player.world.unload();
                        destination.loadWorld(player);
                        WorldPanel.panel.saveGame();
                    }
                    
                };
                return;
            }/*
            if (destinationName.equals("haven") && !playedCartel){
                playedCartel = true;
                ((EntityPlayer)e).frozen = true;
                WorldPanel.panel.addMessage(new String[]{"zayvehead.png Shoot... I forgot to solve that case the Tenn Cartel hired me for.", "zayvehead.png Oh well, I'm sure it's fine."});
                WorldPanel.panel.runOnText = new Runnable(){
                    @Override
                    public void run() {
                        final EntityPlayer player = (EntityPlayer) e;
                        player.lastx = player.x;
                        player.lasty = player.y;
                        player.lastWorld = player.world.name;
                        player.x = point.x;
                        player.y = point.y;
                        player.xvel = 0;
                        player.yvel = 0;
                        player.world.unload();
                        destination.loadWorld(player);
                        WorldPanel.panel.saveGame();
                    }
                    
                };
                return;
            }*/
            final EntityPlayer player = (EntityPlayer) e;
            player.lastx = player.x;
            player.lasty = player.y;
            player.lastWorld = player.world.name;
            player.x = point.x;
            player.y = point.y;
            player.xvel = 0;
            player.yvel = 0;
            player.world.unload();
            destination.loadWorld(player);
            WorldPanel.panel.saveGame();
            
            
            try {
                new AudioPlayer("click.wav").play(0);
            } catch (Exception ex) {
                Logger.getLogger(EntityPortal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*
            new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        player.x = point.x;
                        player.y = point.y;
                        player.world.unload();
                        destination.loadWorld(player);
                        WorldPanel.panel.saveGame();
                    }
                }, 
                30 
            );
            */
        }
    }
}
