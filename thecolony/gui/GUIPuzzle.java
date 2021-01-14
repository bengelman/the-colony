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
public abstract class GUIPuzzle extends GUI{
    
    public GUIPuzzle(){
        
    }
    public Runnable success;
    public GUIPuzzle(Runnable success){
        super();
        this.success = success;
    }
    public void success(){
        success.run();
        close();
    }
    
}
