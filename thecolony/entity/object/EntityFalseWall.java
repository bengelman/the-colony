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
import thecolony.entity.behavior.MoveScript;

/**
 *
 * @author pdogmuncher
 */
public class EntityFalseWall extends Entity{
    public EntityFalseWall(World world, int x, int y, int w, int h, int height, String[] texture){
        super(world, x, y, w, h, height, texture);
        
    }
    public void onInteract(EntityPlayer p){
        super.onInteract(p);
        
        if (world.name.equals("museum2")) {
            moveScript = new MoveScriptMuseumWall();
            
            
            
        }
        else{
            world.entities.remove(this);
        }
        if (world.name.equals("baseoutside")) {
            WorldPanel.panel.addMessage(new String[]{"Ah! I can use the vents."});
        }
        //WorldPanel.panel.textTimer = WorldPanel.TICKS_PER_SECOND * 4;
    }
    class MoveScriptMuseumWall extends MoveScript{
        
        @Override
        public void step(Entity entity) {
            if (WorldPanel.panel.player.y < 320){
                WorldPanel.panel.player.frozen = true;
                WorldPanel.panel.player.y += 4;
                WorldPanel.panel.player.xvel = 1;
            }
            else if (time == 0){
                WorldPanel.panel.player.xvel = 0;
                entity.texture = new String[]{"null.png"};
                WorldPanel.panel.addMessage(new String[]{
                    "zayvehead.png A fake wall! This must be how the thieves got in. There's a door behind it, but it's locked.",
                });
                WorldPanel.panel.player.frozen = true;
                WorldPanel.panel.player.facingBack = false;
                time++;
            }
            else if (time < 40){
                WorldPanel.panel.player.x -= 4;
                WorldPanel.panel.player.xvel = 1;
                WorldPanel.panel.player.facingBack = false;
                time++;
            }
            else if (time == 40){
                WorldPanel.panel.player.xvel = 0;
                WorldPanel.panel.addMessage(new String[]{
                    "zayvehead.png Hey, there's a security camera here.",
                    "curatorhead.png Yes. However, our systems malfunctioned before the theft.",
                    "zayvehead.png Hmmm... My cybermask's decompilation software could find out if there's any sort of malware infecting the cameras.",
                    
                    "zayvehead.png ...",
                    "zayvehead.png Got it. It looks like there's malicious files here.",
                    "zayvehead.png Our hacker left an identification file in their code. Hopefully they left some personal information...",
                    "zayvehead.png Looks like it's our lucky day. The author is Devona Martenzel.",
                    "curatorhead.png Very impressive. It appears your reputation is not undeserved.",
                    "zayvehead.png Thanks. Let's see if I can find something about our author... Aha, a Spacebook profile."
                });
                time++;
            }
            else if (time == 41){
                world.entities.remove(entity);
                WorldPanel.panel.showImage(new String[]{"spacebook.png"});
                WorldPanel.panel.currentWorlds.get("haven").unlock();
                WorldPanel.panel.addMessage(new String[]{"zayvehead.png She lives in Haven Hills, that's a little ways southwest of here.", "curatorhead.png I'll see if I can unlock the door behind the false wall while you investigate."});
                WorldPanel.panel.nextObjective = "Go to Haven Hills";
                WorldPanel.panel.player.frozen = false;
                time++;
            }
            
        }
        
    }
}
