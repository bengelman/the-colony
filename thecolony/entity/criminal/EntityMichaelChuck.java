/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.criminal;

import java.io.Serializable;
import thecolony.entity.behavior.AIFighter;
import thecolony.entity.behavior.AIPartner;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.Entity;
import thecolony.entity.EntityFighter;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityNPC;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.MoveScript;
import thecolony.items.ItemAid;

/**
 *
 * @author pdogmuncher
 */
public class EntityMichaelChuck extends EntityFighter{
    boolean playedIntro = false;
    public EntityMichaelChuck(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"avery.png", "avery0.png", "avery1.png"}}, 100, 5, 15, "chahEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.CLASSIC});
        
        ItemArmor thiefArmor = new ItemArmor("Kevlar Vest", "A protective bulletproof vest", "swatvest.png", "swatvest.png", 8);
        ItemHelmet thiefHelmet = new ItemHelmet("Michael Chuck's Helmet", "Michael Chuck's personalized helmet", "cyberhelmet.png", "cyberflip.png", 3);
        //this.ai = new AIFighter();
        ai.e = this;
        
        ItemWeapon switchblade = new ItemWeapon("Michael Chuck's Switchblade", "mchuckswitchblade.png", "It's still soaked in blood", 40, 25);
        
        ((AISmart)ai).melee = switchblade;
        armor = thiefArmor;
        helmet = thiefHelmet;
        weapon = switchblade;
        inventory.add(armor);
        inventory.add(helmet);
        inventory.add(weapon);
        inventory.add(new ItemAid("Fries", "fries.png", 100));
        credits = 10;
        if (world.name.equals("haven market")){
            this.moveScript = new MoveScriptFries();
        }
    }
    
    public void update(){
        super.update();
        if (world.name.equals("bunker") && !playedIntro && health > 0){
            freeze = true;
            if (Math.abs(world.player.y - y) < 300 && Math.abs(world.player.x - x) < 500){
                playedIntro = true;
                world.player.frozen = true;
                WorldPanel.panel.addMessage(new String[]{"chuckhead.png Greetings, Zayve. I thought we might meet again.", "zayvehead.png Hey, Michael. Any chance you'll let me through?", "chuckhead.png Sorry. I already let you go once, and besides, I need more human meat. I'm planning to build a meat dragon.", "zayvehead.png A... meat dragon?", "chuckhead.png It will be a true work of art."});
                WorldPanel.panel.runOnText = new Runnable(){

                    @Override
                    public void run() {
                        WorldPanel.panel.addAnim("mchuck", 60, 20, null);
                        freeze = false;
                        world.player.frozen = false;
                    }
                    
                };
                
                
            }
        }
        /*
        if (health <= 0){
            World market = WorldPanel.panel.currentWorlds.get("haven market");
            for (int i = 0; i < market.entities.size(); i++){
                if (market.entities.get(i) instanceof EntityNPC){
                    market.entities.remove(i);
                    break;
                }
            }
        }*/
    }
    class MoveScriptFries extends MoveScript implements Serializable{

        @Override
        public void step(Entity entity) {
            
            if (time == 0){
                if (entity.y > WorldPanel.panel.player.y || Math.abs(entity.y - WorldPanel.panel.player.y) > 500 || Math.abs(entity.x - WorldPanel.panel.player.x) > 100){
                    return;
                }
                WorldPanel.panel.player.frozen = true;
                WorldPanel.panel.addMessage(new String[]{"chuckhead.png Greetings.", "zayvehead.png Hey.", "chuckhead.png I have been ordered to kill you.", "zayvehead.png That's to be expected. You are the Tenn Cartel's psychopathic hit man, after all.", "chuckhead.png Yeah. But I still owe you for those fries. So I'll let you live for now.", "zayvehead.png I'll consider the debt paid."});
            }
            else if (time < 16){
                entity.y += 10;
                entity.xvel = 5;
            }
            else if (time < 200){
                entity.x -= 10;
            }
            else{
                entity.world.entities.remove(entity);
                WorldPanel.panel.player.frozen = false;
            }
            time++;
        }
        
    }
}
