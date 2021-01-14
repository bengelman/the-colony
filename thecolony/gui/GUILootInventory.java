/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.Main;
import thecolony.WorldPanel;
import thecolony.entity.EntityLoot;
import thecolony.items.Item;
import thecolony.items.ItemAmmoStash;

/**
 *
 * @author pdogmuncher
 */
public class GUILootInventory extends GUI{
    ArrayList<GUIItemButton> buttons = new ArrayList<>();
    public int yOffset = 0;
    EntityLoot loot;
    
    public GUILootInventory(EntityLoot loot){
        this.loot = loot;
        for (Item item : loot.loot){
            boolean selected = false;
            for (GUIItemButton button : buttons){
                if (button.items.get(0).name.equals(item.name)){
                    button.items.add(item);
                    selected = true;
                }
            }
            if (!selected){
                buttons.add(new GUIItemButton(item));
            }
        }
    }
    @Override
    public void close(){
        super.close();
        
    }
    @Override
    public void click(Point p){
        int index = (p.y - 110 + yOffset) / 60;
        if (index < 0){
            
        }
        else if (index > buttons.size()){
            
        }
        else if (index == buttons.size()){
            new AudioPlayer("click.wav").play(0);
            close();
        }
        else{
            new AudioPlayer("click.wav").play(0);
            for (Item item : buttons.get(index).items){
                if (item instanceof ItemAmmoStash){
                    ItemAmmoStash stash = (ItemAmmoStash) buttons.get(index).items.get(0);
                    for (int i = 0; i < stash.amount; i++){
                        WorldPanel.panel.player.inventory.add(stash.type);
                    }
                }
                else{
                    WorldPanel.panel.player.inventory.add(item);
                }
                
                loot.loot.remove(item);
            }
            buttons.remove(index);
            
        }
        //close();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        //g.fillRoundRect(100, 100, WorldPanel.GAMEWIDTH - 200, WorldPanel.GAMEHEIGHT - 200, 10, 10);
        g.setColor(Color.BLACK);
        //g.drawRoundRect(100, 100, WorldPanel.GAMEWIDTH - 200, WorldPanel.GAMEHEIGHT - 200, 10, 10);
        BufferedImage limiter = ImageRegistry.getImage("canvas.png", true);
        Graphics2D limit = limiter.createGraphics();
        for (int index = 0; index < buttons.size(); index++){
            GUIItemButton button = buttons.get(index);
            button.draw(limit, yOffset, index);
        }
        
        limit.setColor(WorldPanel.DARK);
        limit.fillRoundRect(110, 10 + 60*buttons.size() - yOffset, 815, 55, 5, 5);
        limit.setColor(Color.WHITE);
        limit.drawString(Main.createFallbackString("Exit", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 60 + 60*buttons.size() - yOffset);
        g.drawImage(limiter, 0, 100, 1400, 500, null);
    }

    @Override
    public void scroll(int amount) {
        yOffset += amount * 20;
        if (yOffset < 0){
            yOffset = 0;
        }
        if (165 + 60*buttons.size() - yOffset < 600){
            yOffset = 165 + 60*buttons.size() - 600;
        }
    }
    public class GUIItemButton{
        ArrayList<Item> items = new ArrayList<>();
        public GUIItemButton(Item item){
            this.items.add(item);
        }
        public void draw(Graphics2D g, int yOffset, int index){
            g.setColor(WorldPanel.DARK);
            g.fillRoundRect(110, 10 + 60*index - yOffset, 815, 55, 5, 5);
            g.setColor(Color.WHITE);
            g.fillRoundRect(872, 13 + 60*index - yOffset, 48, 48, 5, 5);
            g.drawImage(items.get(0).getImage(null, true), 872, 13 + 60*index - yOffset, 48, 48, null);
            g.setColor(Color.WHITE);
            g.drawString(Main.createFallbackString(items.get(0).name + " x" + items.size(), WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 60 + 60*index - yOffset);
            
        }
    }
}
