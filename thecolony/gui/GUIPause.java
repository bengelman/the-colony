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
import thecolony.items.Item;

/**
 *
 * @author pdogmuncher
 */
public class GUIPause extends GUI{
    
    public GUIPause(){
        
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
        switch(index){
            case 0:
                new AudioPlayer("click.wav").play(0);
                close();
                break;
            case 1:
                new AudioPlayer("click.wav").play(0);
                Runnable[] runnables = new Runnable[]{
                    new Runnable(){
                        @Override
                        public void run() {
                            WorldPanel.panel.audio.stop();
                            Main.currentFrame.reInit();
                        }
                    },
                    new Runnable(){
                        @Override
                        public void run() {
                            
                        }
                    }
                };
                WorldPanel.panel.showOptionPane(new String[]{"Quit", "Cancel"}, "Quit? All unsaved progress will be lost.", runnables);
                
                   
                break;
            case 2:
                new AudioPlayer("click.wav").play(0);
                WorldPanel.panel.saveGame();
                WorldPanel.panel.showDialogue(new String[]{"Game saved"}, true);
                break;
            case 3:
                new AudioPlayer("click.wav").play(0);
                Main.fullscreen = !Main.fullscreen;
                Main.settings.updateBoolean("fullscreen", Main.fullscreen);
                Main.currentFrame.dispose();
                Main.currentFrame.setUndecorated(Main.fullscreen);
                Main.currentFrame.setVisible(true);
                
                Main.device.setFullScreenWindow(Main.fullscreen ? Main.currentFrame : null);
                WorldPanel.panel.requestFocus();
                break;
            case 4:
                new AudioPlayer("click.wav").play(0);
                Main.settings.updateBoolean("muted", !Main.settings.getBoolean("muted", Boolean.FALSE));
                WorldPanel.panel.audio.stop();
                WorldPanel.panel.audio = new AudioPlayer(WorldPanel.panel.audio.sound);
                WorldPanel.panel.audio.play(-1);
                break;
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
        
        
        limit.setColor(WorldPanel.DARK);
        limit.fillRoundRect(110, 10 + 60, 815, 55, 5, 5);
        limit.fillRoundRect(110, 10 + 120, 815, 55, 5, 5);
        limit.fillRoundRect(110, 10 + 180, 815, 55, 5, 5);
        limit.fillRoundRect(110, 10 + 240, 815, 55, 5, 5);
        limit.fillRoundRect(110, 10, 815, 55, 5, 5);
        limit.setColor(Color.WHITE);
        limit.drawString(Main.createFallbackString("Resume", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 60);
        limit.drawString(Main.createFallbackString("Main Menu", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 120);
        limit.drawString(Main.createFallbackString("Save Game", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 180);
        limit.drawString(Main.createFallbackString("Enter " + (Main.settings.getBoolean("fullscreen", false) ? "Windowed" : "Fullscreen") + " Mode", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 240);
        limit.drawString(Main.createFallbackString(Main.settings.getBoolean("muted", false) ? "Unmute" : "Mute", WorldPanel.bitFont, WorldPanel.fallbackFont).getIterator(), 120, 300);
        g.drawImage(limiter, 0, 100, 1400, 500, null);
    }

    @Override
    public void scroll(int amount) {}
}
