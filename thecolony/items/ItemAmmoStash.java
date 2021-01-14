/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.items;

/**
 *
 * @author pdogmuncher
 */
public class ItemAmmoStash extends Item{
    public Item type;
    public int amount;
    public ItemAmmoStash(Item type, int amount){
        super(type.name + " stash ( " + amount + " )" , "stash.png");
        this.type = type;
        this.amount = amount;
    }
}
