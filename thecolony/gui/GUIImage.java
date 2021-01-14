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
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import thecolony.ImageRegistry;
import thecolony.Main;
import thecolony.WorldPanel;

/**
 *
 * @author pdogmuncher
 */
public class GUIImage extends GUI{
    String[] images;
    int index = 0;
    boolean fullscreen;
    boolean click;
    public GUIImage(String[] images, boolean fullscreen, boolean clickToProgress){
        this.images = images;
        this.fullscreen = fullscreen;
        this.click = clickToProgress;
    }
    @Override
    public void click(Point p){
        if (!click) {
            return;
        }
        index++;
        if (index >= images.length) {
            close();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (fullscreen){
            g.drawImage(ImageRegistry.getImage(images[index], true), 0, 0, WorldPanel.GAMEWIDTH, WorldPanel.GAMEHEIGHT, null);
        }
        else{
            BufferedImage buf = ImageRegistry.getImage(images[index], true);
            int width = WorldPanel.GAMEWIDTH - 200;
            int height = WorldPanel.GAMEHEIGHT - 200;
            double widthRatio = (double)width / (double)buf.getWidth();
            double heightRatio = (double)height / (double)buf.getHeight();
            if (widthRatio > heightRatio){
                g.drawImage(buf, 100 + (width - (int)(buf.getWidth() * heightRatio)) / 2, 100, (int)(buf.getWidth() * heightRatio), (int)(buf.getHeight() * heightRatio), null);
            }
            else{
                g.drawImage(buf, 100, 100 + (height - (int)(buf.getHeight() * widthRatio)) / 2, (int)(buf.getWidth() * widthRatio), (int)(buf.getHeight() * widthRatio), null);
            }
        }
    }

    @Override
    public void scroll(int amount) {
        
    }
}
