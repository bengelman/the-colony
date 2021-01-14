/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items.drugs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import thecolony.AudioPlayer;
import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;
import thecolony.entity.object.EntityPortal;
import thecolony.items.ItemAid;

/**
 *
 * @author pdogmuncher
 */
public class ItemTransneuract extends ItemAid{
    public ItemTransneuract(){
        super("Riftaine", "trans.png", 0);
    }
    @Override
    public boolean use (EntityPlayer player){
        super.use(player);
        player.x = player.lastx;
        player.y = player.lasty;
        player.xvel = 0;
        player.yvel = 0;
        player.world.unload();
        WorldPanel.panel.currentWorlds.get(player.lastWorld).loadWorld(player);

        WorldPanel.panel.saveGame();
        try {
            new AudioPlayer("plasma.wav").play(0);
        } catch (Exception ex) {
            Logger.getLogger(EntityPortal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    @Override
    public String[] getMouseover(){
        return new String[]{name, "Creates a spacetime rift sending the user to the last area they visited", "WARNING: tampers with fabric of universe"};
    }
}
