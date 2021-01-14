/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.object;

import thecolony.entity.Entity;
import java.awt.Point;
import java.util.ArrayList;
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
public class EntityGate extends Entity{
    Point point;
    String destinationName;
    public boolean locked = true;
    public String image = null;
    boolean hasPlayed = false;
    
    public EntityGate(World world, int x, int y, int w, int h, int height, String[] texture, String destination, Point destinationLocation){
        super(world, x, y, w, h, height, texture);
        this.point = destinationLocation;
        this.destinationName = destination;
    }
    @Override
    public void collide (Entity e){
        if (locked){
            return;
        }
        if (!WorldPanel.panel.currentTextQueue.isEmpty())return;
        if (e == WorldPanel.panel.player){
            final World destination = WorldPanel.panel.currentWorlds.get(destinationName);
            
            final EntityPlayer player = (EntityPlayer) e;
            if (e.world.name.equals("cell")){
                ArrayList<Entity> entities = new ArrayList(e.world.entities);
                for (Entity et : entities){
                    if (et instanceof EntityNPC){
                        e.world.entities.remove(et);
                    }
                }
            }
            if (e.world.name.equals("landingzone") && destination.name.equals("city")){
                WorldPanel.panel.addMessage(new String[]{"zayvehead.png Hmmm... I guess I should just ask around about the Triad base. This is Triad territory, so I should keep my mask on. I don't want anyone realizing I'm human.",
                });
            }
            if (e.world.name.equals("hangar") && destination.name.equals("cannon1")){
                WorldPanel.panel.addMessage(new String[]{"normachead.png There are anti-air cannons preventing us from escaping the atmosphere. Can you take these explosives and destroy the cannon control panels?",
                    "zayvehead.png I'll do my best.",
                    "normachead.png I'll see if I can provide some air support."
                });
            }
            if (e.world.name.equals("jmishtin house")){
                ((EntityPlayer)e).frozen = true;
                WorldPanel.panel.currentWorlds.get("haven suburbs").lock();
                WorldPanel.panel.addMessage(new String[]{
                    "jmishtin.png You just said that your name is Zayve Brock... Brock is a human surname! You can't be from the Triad, the Triad doesn't hire humans.",
                    "zayvehead.png They... changed their hiring policy.", 
                    "jmishtin.png The Triad hates humans. They would never hire one. You must be working against them!", 
                    "zayvehead.png ...",
                    "jmishtin.png You tricked me! I'll have to let the Triad know!",
                    "zayvehead.png I can't let you do that."
                });
                WorldPanel.panel.runOnText = new Runnable(){
                    @Override
                    public void run() {
                        WorldPanel.panel.addAnim("highnoon", 30, 10, new Runnable(){

                            @Override
                            public void run() {
                                new AudioPlayer("plasma.wav").play(0);
                                player.frozen = false;
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
                                WorldPanel.panel.showImage(new String[]{"cutportal.png"});
                            }
                            
                        });
                        //final EntityPlayer player = player;
                        //new AudioPlayer("plasma.wav").play(0);
                        //WorldPanel.panel.runNextTick = 
                        
                    }
                    
                };
                return;
            }
            if (player.world.name.equals("outsideprison")){
                //WorldPanel.panel.currentWorlds.get("secretbaseoutside").unlock();
                EntityNPC npc = new EntityNPC(WorldPanel.panel.currentWorlds.get("secretbaseoutside"), 300, 200, 36, 25, 48, "partner.png", new String[]{"zayvehead.png Sorry I took so long. What's up?", "normachead.png The ship had a record of a base in haven. I figured I'd check it out.", "normachead.png Anyways, sorry to call in a favor so soon, but I really need some help. This base is larger than I expected and there aren't any resistance soldiers in the area.", "normachead.png If you're willing to help, I could use an extra man."});
                Entity fighter = new Entity(WorldPanel.panel.currentWorlds.get("secretbaseoutside"), 50, 300, 100, 25, 100, new String[]{"fighter.png"});
                WorldPanel.panel.currentWorlds.get("secretbaseoutside").entities.add(fighter);
                
                WorldPanel.panel.currentWorlds.get("secretbaseoutside").entities.add(npc);
                WorldPanel.panel.currentWorlds.get("museum").lock();
                WorldPanel.panel.currentWorlds.get("haven").playPhtrip = true;
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
            WorldPanel.panel.saveGame();
            if (image != null && !hasPlayed){
                WorldPanel.panel.showImage(new String[]{image});
                hasPlayed = true;
            }
            
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
            );*/
        }
    }
}
