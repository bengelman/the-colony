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
import static thecolony.WorldPanel.GAMEHEIGHT;
import static thecolony.WorldPanel.GAMEWIDTH;
import thecolony.items.Item;

/**
 *
 * @author pdogmuncher
 */
public class GUIInventory extends GUI{
    ArrayList<GUIItemButton> buttons = new ArrayList<>();
    public int yOffset = 0;
    
    public GUIInventory(ArrayList<Item> items){
        for (Item item : items){
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
            if (buttons.get(index).items.get(0).use(WorldPanel.panel.player)){
                buttons.get(index).items.remove(0);
                if (buttons.get(index).items.isEmpty()){
                    buttons.remove(index);
                }
            }
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
        g.setColor(WorldPanel.DARK);
        g.fillRoundRect(WorldPanel.GAMEWIDTH - 310, 110, 200, 55, 5, 5);
        g.setColor(Color.WHITE);
        g.drawString(Main.createFallbackString("$" + WorldPanel.panel.player.credits, WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), WorldPanel.GAMEWIDTH - 300, 160);
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
        
        
        double widthRatio = (double)WorldPanel.panel.getWidth() / (double)GAMEWIDTH;
        double heightRatio = (double)WorldPanel.panel.getHeight() / (double)GAMEHEIGHT;
        int x = WorldPanel.pointerLocation.x, y = WorldPanel.pointerLocation.y;
        
        if (widthRatio > heightRatio){
            x /= heightRatio;
            y /= heightRatio;
        }
        else{
            x /= widthRatio;
            y /= widthRatio;
        }
        Point mouse = new Point(x, y);
        
        int index = (mouse.y - 110 + yOffset) / 60;
        if (index >= 0 && index < buttons.size() && x > 110 && x < 925 && buttons.get(index).items.get(0).getMouseover() != null){
            int maxsize = 0;
            
            for (String s : buttons.get(index).items.get(0).getMouseover()){
                int stringLen = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
                if (stringLen > maxsize){
                    maxsize = stringLen;
                }
            }
            g.setColor(Color.YELLOW);
            g.fillRoundRect(mouse.x - 10, mouse.y - 10, maxsize + 20, ((int)g.getFontMetrics().getStringBounds("Test", g).getHeight() + 10) * buttons.get(index).items.get(0).getMouseover().length + 10, 5, 5);
            g.setColor(Color.BLACK);
            int indexe = 0;
            for (String s : buttons.get(index).items.get(0).getMouseover()){
                g.drawString(Main.createFallbackString(s, WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), mouse.x + 10, mouse.y + indexe * ((int)g.getFontMetrics().getStringBounds("Test", g).getHeight() + 10) + 10);
                indexe++;
            }
        }
        
        
        
            
            
        
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
            if(items.get(0).name.equals(WorldPanel.panel.player.weapon.name) || 
                    (WorldPanel.panel.player.chip != null && items.get(0).name.equals(WorldPanel.panel.player.chip.name)) || 
                    items.get(0).name.equals(WorldPanel.panel.player.armor.name) || 
                    items.get(0).name.equals(WorldPanel.panel.player.helmet.name)){
                g.setStroke(new BasicStroke(7));
                g.setColor(Color.GREEN);
                g.drawRoundRect(110, 10 + 60*index - yOffset, 815, 55, 5, 5);
            }
        }
    }
}
