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
import java.util.logging.Level;
import java.util.logging.Logger;
import thecolony.AudioPlayer;
import thecolony.ImageRegistry;
import thecolony.Main;
import thecolony.WorldPanel;
import thecolony.items.Item;

/**
 *
 * @author pdogmuncher
 */
public class GUIOptionMenu extends GUI{
    
    public GUIOptionMenu(){
        
    }
    public String question;
    public String[] options;
    public String choice = null;
    public Runnable[] runnables;
    public GUIOptionMenu(String question, String[] options, Runnable[] runnables){
        super();
        this.question = question;
        this.options = options;
        this.runnables = runnables;
    }
    @Override
    public void close(){
        super.close();
        
    }
    @Override
    public void click(Point p){
        /*
        double widthRatio = (double)WorldPanel.panel.getWidth() / (double)WorldPanel.GAMEWIDTH;
        double heightRatio = (double)WorldPanel.panel.getHeight() / (double)WorldPanel.GAMEHEIGHT;
        
        if (widthRatio > heightRatio){
            p.y += ((WorldPanel.panel.getWidth() - (int)(WorldPanel.GAMEWIDTH * heightRatio)) / 2) / heightRatio;
        }
        else{
            p.x += ((WorldPanel.panel.getHeight() - (int)(WorldPanel.GAMEHEIGHT * widthRatio)) / 2) / widthRatio;
        }
        */
        
        
        int index = (p.y - 110) / 60;
        if (index >= 0 && index < options.length){
            this.choice = options[index];
            runnables[index].run();
            this.close();
        }
        //close();
    }

    @Override
    public void draw(Graphics2D g) {
        
        BufferedImage limiter = ImageRegistry.getImage("canvas.png", true);
        
        Graphics2D limit = limiter.createGraphics();
        g.setColor(Color.WHITE);
        g.drawString(Main.createFallbackString(question, WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 100);
        for (int i = 0; i < options.length; i++){
            
            String option = options[i];
            limit.setColor(WorldPanel.DARK);
            limit.fillRoundRect(110, 10 + 60 * i, 815, 55, 5, 5);
            limit.setColor(Color.WHITE);
            limit.drawString(Main.createFallbackString(option, WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 60 + 60 * i);
        }
        
        g.drawImage(limiter, 0, 100, 1400, 500, null);
    }

    @Override
    public void scroll(int amount) {}
}
