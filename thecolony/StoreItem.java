/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony;

import thecolony.items.Item;
import java.io.Serializable;

/**
 *
 * @author pdogmuncher
 */
public class StoreItem implements Serializable{
    public Item item;
    public int cost;
    public int quantity;
    public StoreItem(Item item, int cost, int quantity){
        this.item = item;
        this.cost = cost;
        this.quantity = quantity;
    }
}
