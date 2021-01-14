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

/**
 *
 * @author pdogmuncher
 */
public class EntityJournal extends Entity{
    String[] message;
    boolean hasPlayed = false;
    public EntityJournal(World world, int x, int y, int w, int h, int height, String[] texture, String[] message){
        super(world, x, y, w, h, height, texture);
        this.message = message;
        
    }
    public void onInteract(EntityPlayer p){
        super.onInteract(p);
        /*
        if (p.world.name.equals("house")){
            //Main.fullscreen = false;
            //Main.device.setFullScreenWindow(null);
            /*
            try {
                //WorldPanel.panel.showImage("files.png");
                WorldPanel.exportResource("guide.pdf");
            } catch (Exception ex) {
                Logger.getLogger(EntityJournal.class.getName()).log(Level.SEVERE, null, ex);
            }
            Desktop dt = Desktop.getDesktop();
            try {
                dt.open(new File("guide.pdf"));
            } catch (IOException ex) {
                Logger.getLogger(EntityJournal.class.getName()).log(Level.SEVERE, null, ex);
            }
            WorldPanel.panel.showImage(new String[]{"tips.png"});
            
            p.world.unlock();
            return;
        }*/
        if (world.name.equals("ubermart")){
            WorldPanel.panel.currentWorlds.get("machos nachos").unlock();
        }
        if (world.name.equals("jmishtin house") && !hasPlayed){
            hasPlayed = true;
            WorldPanel.panel.currentWorlds.get("museum2").unlock();
            world.unlock();
            WorldPanel.panel.addMessage(new String[]{"zayvehead.png Hmm... According to that letter, Devona was working for the Stosthor Triad. Considering they basically control half the planet I don't really want to mess with them, but $1 000 000 is a lot of cash... I suppose it would be a good idea to check out that base she mentioned.", "*Ring Ring Ring*", "*Click*", "zayvehead.png You've reached Zayve Brock QUALITY DETECTIVE SERVICES, how may I help you?", "curatorhead.png We managed to get the hidden door at the museum unlocked.", "zayvehead.png I'll be right over.", "*Click*"});
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Investigate the hidden door at the museum";
                }
                
            };
        }
        /*
        if (world.name.equals("tennfight")){
            WorldPanel.panel.currentWorlds.get("tennfight").unlock();
            WorldPanel.panel.nextObjective = "";
        }*/
        /*
        if (world.name.equals("museum2")){
            WorldPanel.panel.showImage(new String[]{"scene1.png", "scene2.png", "scene3.png", "scene4.png", "scene5.png"});
        }*/
        WorldPanel.panel.showDialogue(message, true);
    }
}
