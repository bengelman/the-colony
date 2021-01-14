/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.triad;

import thecolony.entity.behavior.AIFighter;
import thecolony.items.ItemAid;
import thecolony.items.ItemArmor;
import thecolony.items.ItemHelmet;
import thecolony.items.ItemWeapon;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;
import thecolony.World;
import thecolony.WorldPanel;
import thecolony.entity.EntityBullet;
import thecolony.entity.EntityExplosion;
import thecolony.entity.EntityFighter;
import thecolony.entity.behavior.AISmart;
import thecolony.entity.behavior.AISmart.CombatStyle;

/**
 *
 * @author pdogmuncher
 */
public class EntityThug extends EntityFighter{
    
    public EntityThug(World world, int x, int y){
        super(world, x, y, 36, 25, 48, new String[][]{{"thug.png", "thug0.png", "thug1.png"}, {"thugalt.png", "thugalt0.png", "thugalt1.png"}}, 50, 3, 15, "triadEnemies", new AISmart.CombatStyle[]{AISmart.CombatStyle.BLITZER, CombatStyle.CLASSIC});
        ItemArmor thiefArmor = new ItemArmor("Triad Uniform", "The uniform of triad thugs", "thuguniform.png", "thuguniform.png", 5);
        ItemHelmet thiefHelmet = new ItemHelmet("Cap", "A comfy hat", "thugcap.png", "thugcap.png", 1);
        ItemWeapon switchblade = new ItemWeapon("Switchblade", "switchblade.png", "The switchblade is effective at improving melee damage", 10, 25);
        ItemAid snacPac = new ItemAid("SnacPac", "snac.png", 25);
        ItemWeapon plasmaRevolver;
        
        if (style == CombatStyle.BLITZER) {
            plasmaRevolver= new ItemWeapon("L60 SMG", "smg.png", EntityBullet.BULLET, "The L60 sprays lots of bullets in a short period of time", 12, 2, 2, 2, 500, "bullet", "pistol", 25, ItemWeapon.NINEMM, 30, 1, 0, FireType.AUTO, Scope.NONE, false);
            armor = thiefArmor;
            helmet = thiefHelmet;
            weapon = plasmaRevolver;
            inventory.add(plasmaRevolver);
            inventory.add(armor);
            inventory.add(helmet);
            inventory.add(switchblade);
            inventory.add(snacPac);
            for (int i = 0; i < 30; i++) {
                inventory.add(ItemWeapon.NINEMM);
            }
            
        }
        else {
            
            plasmaRevolver= new ItemWeapon("L87 Carbine", "carbine.png", EntityBullet.BULLET, "The carbine is a scoped mid-range rifle", 20, 6, 8, 0, 1000, "bullet", "rifle", 30, ItemWeapon.THIRTY, 10, 1, 0, FireType.SEMI, Scope.MAG, false);
            armor = thiefArmor;
            helmet = thiefHelmet;
            weapon = plasmaRevolver;
            inventory.add(plasmaRevolver);
            inventory.add(armor);
            inventory.add(helmet);
            inventory.add(switchblade);
            inventory.add(snacPac);
            for (int i = 0; i < 20; i++) {
                inventory.add(ItemWeapon.THIRTY);
            }
        }
        
        //((AIFighter)ai).firearm = plasmaRevolver;
        ((AISmart)ai).melee = switchblade;
        credits = 10;
        
        
    }
    @Override
    public void update(){
        super.update();
        if (world.name.equals("cannon1")){
            if ((int)(Math.random() * 2000) == 0){
                EntityExplosion explosion = new EntityExplosion(world, this.x, this.y, 50, 50, 50, world.player, 10, "exp");
                world.entities.add(explosion);
            }
        }
    }
}
