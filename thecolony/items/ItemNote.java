/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

import thecolony.WorldPanel;
import thecolony.entity.EntityPlayer;

/**
 *
 * @author pdogmuncher
 */
public class ItemNote extends Item{
    public String[] message;
    public ItemNote(String name, String[] message){
        super(name, "note.png");
        this.message = message;
    }
    @Override
    public boolean use(EntityPlayer user){
        super.use(user);
        WorldPanel.panel.showDialogue(message, false);
        if (user.world.name.equals("haven market") && name.equals("Chah Tenn's Note")){
            user.world.unlock();
        }
        return false;
    }
}
