/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import thecolony.ImageRegistry;
import thecolony.entity.behavior.AINPC;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.behavior.MoveScript;
import thecolony.entity.triad.EntityThug;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;

/**
 *
 * @author pdogmuncher
 */
public class EntityNPC extends EntityLiving{
    String[] conversation;
    boolean fired = false;
    ItemWeapon knife = new ItemWeapon("Laser Knife", "laserknife.png", "A superheated plasma knife", 20, 25);
    public EntityNPC(World world, double x, double y, int w, int h, int height, String texture, String[] conversation){
        super(world, x, y, w, h, height, new String[][]{{texture, texture, texture}}, 50, 0, new AINPC(), 0);
        this.conversation = conversation;
        if (world.name.equals("museum2")){
            this.moveScript = new MoveScriptMuseum();
        }
        else if (world.name.equals("base") && texture.endsWith("0.png")){
            this.moveScript = new MoveScriptBase();
        }
    }
    
    @Override
    public void takeDamage(int amount, EntityLiving attacker){
        //super.takeDamage(amount, attacker);
    }
    @Override
    public void update(){
        this.xvel = 0;
        this.yvel = 0;
        super.update();
        
    }
    @Override
    public void onInteract(final EntityPlayer p){
        super.onInteract(p);
        if (moveScript != null)return;
        if (fired){
            return;
        }
        if (p.world.name.equals("hangar") && !p.world.cleared){
            WorldPanel.panel.addMessage(new String[]{"normachead.png We can't leave until the hangar is cleared of enemies"});
            return;
        }
        if (p.world.name.equals("toys r ok")){
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Take down the thief";
                    p.world.unlock();
                }
                
            };
            
        }
        /*
        WorldPanel.panel.currentTextQueue.clear();
        WorldPanel.panel.textTimer = WorldPanel.TICKS_PER_SECOND * 4;
        for (int i = 0; i < conversation.length; i++){
            WorldPanel.panel.currentTextQueue.add(conversation[i]);
            
        }*/
        String[] text = new String[conversation[0].equals("UNLOCK") ? conversation.length - 1 : conversation.length];
        System.arraycopy(conversation, conversation[0].equals("UNLOCK") ? 1 : 0, text, 0, text.length);
        WorldPanel.panel.addMessage(text);
        if (p.world.name.equals("convenience store")){
            WorldPanel.panel.runOnText = new Runnable(){
            @Override
            public void run(){
                WorldPanel.panel.showImage(new String[]{"dewcam.png"});
                WorldPanel.panel.addMessage(new String[]{"You have the ability to equip special biological implant chips, which provide special abilities.", "You have a detective chip that will give you a hint about the current area. Open your inventory using [E] and equip the detective chip, then activate it with [F]."});
                WorldPanel.panel.nextObjective = "Use your detective chip";
                
                
            }
        };
        }
        if (p.world.name.equals("museum2")){
            WorldPanel.panel.nextObjective = "Search the room for clues";
            
        }
        if (p.world.name.equals("haven suburbs")){
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Search Devona's house for clues";
                    p.world.unlock();
                }
                
            };
        }
        if (p.world.name.equals("prisoncontrol")){
            WorldPanel.panel.currentWorlds.get("cell").unlock(1);
        }
        if (p.world.name.equals("cell")){
            WorldPanel.panel.runOnText = new Runnable(){
                public void run(){
                    p.world.unlock(0);
                    if (!p.inventory.contains(knife)){
                        p.inventory.add(knife);
                        p.inventory.remove(p.weapon);
                        p.weapon = knife;
                    }
                }
            };
        }
        if (p.world.name.equals("landingzone")){
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    WorldPanel.panel.nextObjective = "Find the Triad base";
                    p.world.unlock();
                }
                
            };
        }
        if (p.world.name.equals("secretbaseoutside")){
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    p.world.unlock();
                    WorldPanel.panel.nextObjective = "Infiltrate the base";
                }
                
            };
        }
        if (p.world.name.equals("secretbaseoffice")){
            WorldPanel.panel.runOnText = new Runnable(){
                @Override
                public void run() {
                    p.world.unlock();
                    WorldPanel.panel.nextObjective = "Defend against the gunships";
                }
                
            };
        }
        if (p.world.name.equals("basehangar") || p.world.name.equals("outsideprison") || p.world.name.equals("museum") || p.world.name.equals("cannon1") || p.world.name.equals("prison2")){
            final String wld = p.world.name;
            WorldPanel.panel.runOnText = new Runnable(){
                
                @Override
                public void run() {
                    WorldPanel.panel.currentWorlds.get(wld).unlock();
                }
                
            };
            
        }
        if (!conversation[0].equals("UNLOCK"))
            fired = true;
    }
    class MoveScriptMuseum extends MoveScript implements Serializable{

        @Override
        public void step(Entity entity) {
            if (time == 0){
                WorldPanel.panel.addMessage(new String[]{"curatorhead.png Ah! Good morning."});
                
                
                WorldPanel.panel.player.frozen = true;
                WorldPanel.panel.player.facingBack = true;
            }
            else if (time < 57){
                WorldPanel.panel.player.xvel = 1;
                WorldPanel.panel.player.x += 4;
            }
            else if (time == 57){
                WorldPanel.panel.player.xvel = 0;
                WorldPanel.panel.addMessage(new String[]{"curatorhead.png You must be Zayve Brock. Thank you for coming on such short notice.", 
                    "zayvehead.png No problem. You said you had a case?", 
                    "curatorhead.png Yes, that would be correct. First, I need to be sure that you'll keep what I tell you confidential.", 
                    "zayvehead.png Of course.",
                    "curatorhead.png Thank you, I appreciate that. Now, where to begin?",
                    "curatorhead.png Our museum has a large collection of exhibits about the war. We have weapons, uniforms, and even some vehicles. We recently added a new artifact to our collection. An Inferno-Class bomber.",
                    "zayvehead.png Oh yeah, I've heard of them. They're the cool-looking triangular ships, right?",
                    "curatorhead.png Yes, exactly. Do you know what their most important function was?",
                    "zayvehead.png Payload delivery, right?",
                    "curatorhead.png Yes, exactly. Now, the issue we came across was that, upon some investiagtion, we discovered that this ship was carrying a very special payload. An intact antimatter warhead.",
                    "zayvehead.png An antimatter warhead? One of those could level a planet!",
                    "curatorhead.png Now, the museum has a sizeable security force, as I'm sure you can understand, so the warhead was protected. However, we were planning to turn the warhead over to the Colonies Defense Forces, since we didn't want something so dangerous to be kept in our museum.",
                    "zayvehead.png The CDF doesn't really have a presence in Haven right now. How exactly did you plan on moving it?",
                    "curatorhead.png That was exactly our problem. We didn't want the Stosthor Triad to realize that we had possession of such a powerful weapon, and as such we had to be careful about how we contacted the CDF.",
                    "curatorhead.png Before we could contact them, however, the warhead was stolen.",
                    "zayvehead.png Stolen?!",
                    "curatorhead.png Yes, stolen.",
                    "zayvehead.png That's rather concerning. Do you know who did it?",
                    "curatorhead.png No, we dont. That's why we hired you. Some other artifacts were stolen as well, so we can't be sure that the burglars were even specifically looking for the warhead. They may have just picked up whatever they could find. Regardless, it's gone, and we need you to track it down.",
                    "zayvehead.png I think you want someone a little more qualified for this job. I tend to deal more with vandalized houses and convenience store robberies than heists involving planet-destroying superweapons.",
                    "curatorhead.png I disagree, I think you're exactly what we need. I've heard some very good things about you, and we need someone who can be discreet. We don't want factions like the Tenn Cartel or Stosthor Triad finding out there's an antimatter warhead that they could get their hands on. If they managed to find the warhead, it would be catastrophic.",
                    "zayvehead.png Even so, with those stakes there's a good chance my life would be at risk. That's not a risk I want to take.",
                    "curatorhead.png We can offer you $1 000 000 for the return of the warhead.",
                    "zayvehead.png Wow. I guess that changes things. When can I start?",
                    "curatorhead.png Immediately, if you are able."
                });
            }
            else if (time == 58){
                WorldPanel.panel.player.frozen = false;
                
            }
            time++;
        }
        
        
    }
    class MoveScriptBase extends MoveScript{
        EntityThug thug;
        @Override
        public void step(Entity entity) {
            if (WorldPanel.panel.player.y < 1450){
                return;
            }
            if (time == 0){
                WorldPanel.panel.player.frozen = true;
            }
            else if (time < 25){
                WorldPanel.panel.player.facingBack = false;
                WorldPanel.panel.player.y += 4;
                WorldPanel.panel.player.yvel = 1;
            }
            else if (time == 25){
                WorldPanel.panel.player.yvel = 0;
                WorldPanel.panel.addMessage(new String[]{
                    "zayvehead.png Devona Martenzel! Put your tentacles where I can see them.",
                    "devona.png Wh-what do you want?",
                    "zayvehead.png Tell me what happened to the goods stolen from the museum.",
                    "devona.png What?? I think you m-might have the wrong person...",
                    "zayvehead.png You created the software used to deactive security systems during a recent museum heist.",
                    "devona.png Yeah, I, uh, did program something that could do that. I d-don't know anything about what it was used for, though. You'll want to talk to Cinquen Cinquetel. H-he was the one who told me to do it.",
                    "zayvehead.png Where can I find him?",
                    "devona.png I don't know. H-he's the Triad's fifth in command, so he'd be protected.",
                    "zayvehead.png Well, I guess you're going to get me to him. Get-"
                });
            }
            else if (time == 26){
                thug = new EntityThug(entity.world, 300, 996);
                thug.noai = true;
                entity.world.entities.add(thug);
            }
            else if (time < 63){
                thug.xvel = 1;
                thug.y += 8;
            }
            else if (time == 70){
                thug.xvel = 0;
                WorldPanel.panel.addMessage(new String[]{
                    "zayvehead.png Dangit."
                });
            }
            else if (time == 71){
                EntityPlayer ep = (EntityPlayer) entity.world.player;
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
                WorldPanel.panel.nextObjective = "Help Normac";
                
                final World destination = WorldPanel.panel.currentWorlds.get("cell");
                final EntityPlayer player = (EntityPlayer) entity.world.player;
                String[] images = new String[]{"cutportal.png"};
                player.lastx = player.x;
                player.lasty = player.y;
                player.lastWorld = player.world.name;
                player.x = 100;
                player.y = 100;
                player.xvel = 0;
                player.yvel = 0;
                player.world.unload();
                destination.loadWorld(player);
                WorldPanel.panel.showImage(images);
                WorldPanel.panel.saveGame();
            }
            time++;
        }
        
    }
}
