/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony.entity.behavior;

import thecolony.items.ItemWeapon;
import java.awt.Cursor;
import thecolony.entity.EntityLiving;
import thecolony.entity.EntityPlayer;
import thecolony.entity.Entity;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import thecolony.ImageRegistry;
import thecolony.Main;
import thecolony.WorldPanel;
import thecolony.items.ItemWeapon.FireType;
import thecolony.items.ItemWeapon.Scope;

/**
 *
 * @author pdogmuncher
 */
/*

*/
public class AIPlayer extends AI implements MouseWheelListener{
    public boolean isMelee = false;
    public boolean isClicking = false;
    public ArrayList<Character> lockedBindings = new ArrayList<>();
    public AIPlayer(){
        super();
        initKeys();
        //WorldPanel.panel.addMouseWheelListener(this);
    }
    @Override
    public void onUpdate() {
        Main.currentFrame.requestFocus();
        if (isClicking && e.weapon.fireType == FireType.AUTO){
            double xp = e.world.getXInWorld(WorldPanel.pointerLocation.x);//WorldPanel.pointerLocation.x + e.x - WorldPanel.panel.getWidth() / 2;
            double yp = e.world.getYInWorld(WorldPanel.pointerLocation.y);//WorldPanel.pointerLocation.y + e.y - WorldPanel.panel.getHeight() / 2;
            e.weapon.fireBullet(new Point((int)xp, (int)yp), e);
        }
    }

    @Override
    public void onCollide(Entity with) {
        if (isMelee && with instanceof EntityLiving && e.meleetimer == 0){
            e.meleetimer = e.weapon.meleetimer;
            EntityLiving ent = (EntityLiving) with;
            ent.takeDamage(e.strength + e.weapon.melee, e);
            ent.xvel = (ent.x - e.x) / 2;
            ent.yvel = (ent.y - e.y) / 2;
        }
            
    }
    public void addKey(Action action, int key, String name, boolean press){
        
        WorldPanel.panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0, !press), name);
        WorldPanel.panel.getActionMap().put(name, action);
    }
    public void initKeys(){
        WorldPanel.panel.getInputMap().clear();
        WorldPanel.panel.getActionMap().clear();
        Main.currentFrame.requestFocus();
        Action up = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) 
            {
                if (lockedBindings.contains(new Character('W')))return;
                lockedBindings.add(new Character('W'));
                e.yvel = -e.maxVelocity;
            }
        };
        addKey(up, KeyEvent.VK_W, "up", true);
        Action down = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('S')))return;
                lockedBindings.add(new Character('S'));
                e.yvel = e.maxVelocity;
            }
        };
        addKey(down, KeyEvent.VK_S, "down", true);
        Action right = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('D')))return;
                lockedBindings.add(new Character('D'));
                e.xvel = e.maxVelocity;
            }
        };
        addKey(right, KeyEvent.VK_D, "right", true);
        Action left = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('A')))return;
                lockedBindings.add(new Character('A'));
                e.xvel = -e.maxVelocity;
            }
        };
        addKey(left, KeyEvent.VK_A, "left", true);
        Action stopy = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('W')))
                    lockedBindings.remove(new Character('W'));
                if (lockedBindings.contains(new Character('S')))
                    lockedBindings.remove(new Character('S'));
                e.yvel = 0;
            }
        };
        addKey(stopy, KeyEvent.VK_W, "uprel", false);
        addKey(stopy, KeyEvent.VK_S, "downrel", false);
        Action stopx = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('D')))
                    lockedBindings.remove(new Character('D'));
                if (lockedBindings.contains(new Character('A')))
                    lockedBindings.remove(new Character('A'));
                e.xvel = 0;
            }
        };
        addKey(stopx, KeyEvent.VK_D, "rightrel", false);
        addKey(stopx, KeyEvent.VK_A, "leftrel", false);
        
        Action inventory = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                
                WorldPanel.panel.openInventory();
            }
        };
        addKey(inventory, KeyEvent.VK_E, "inventory", true);
        
        Action reload = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                //if (lockedBindings.contains(KeyEvent.VK_R))return;
                //lockedBindings.add(KeyEvent.VK_R);
                if (e.weapon != null){
                    e.weapon.reload(e);
                }
                
            }
        };
        addKey(reload, KeyEvent.VK_R, "reload", true);
        
        Action melee = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character(' '))) {
                    return;
                }
                lockedBindings.add(new Character(' '));
                isMelee = true;
                
            }
        };
        addKey(melee, KeyEvent.VK_SPACE, "melee", true);
        Action meleer = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character(' '))) {
                    lockedBindings.remove(new Character(' '));
                }
                isMelee = false;
                
            }
        };
        addKey(meleer, KeyEvent.VK_SPACE, "meleer", false);
        
        
        Action gif = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('g'))) {
                    return;
                }
                lockedBindings.add(new Character('g'));
                ((EntityPlayer)e).takingGif = true;
                
            }
        };
        addKey(gif, KeyEvent.VK_G, "gif", true);
        
        Action gifEnd = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('g')))
                    lockedBindings.remove(new Character('g'));
                ((EntityPlayer)e).takingGif = false;
                
            }
        };
        addKey(gifEnd, KeyEvent.VK_G, "gife", false);
        
        
        
        Action escape = new AbstractAction() {
            public void actionPerformed(ActionEvent ef) {
                WorldPanel.panel.showMenu();
                
            }
        };
        addKey(escape, KeyEvent.VK_ESCAPE, "escape", true);
        
        Action scope = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (lockedBindings.contains(new Character('Q')))return;
                lockedBindings.add(new Character('Q'));
                ((EntityPlayer)e).zoomed = true;
                if (e.weapon.scope == Scope.MAG || e.weapon.scope == Scope.DIGITAL) {
                    ((EntityPlayer)e).zoomLevel = 1;
                }
                else if (e.weapon.scope == Scope.LONGRANGE){
                    ((EntityPlayer)e).zoomLevel = 2;
                }
                if (!(e.weapon.scope == Scope.MAG || e.weapon.scope == Scope.DIGITAL || e.weapon.scope == Scope.LONGRANGE)){
                    ((EntityPlayer)e).zoomed = false;
                }
                if (((EntityPlayer)e).zoomed){
                    
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    BufferedImage image = ImageRegistry.getImage("scope.png", false);
                    Cursor c = toolkit.createCustomCursor(image , new Point(WorldPanel.panel.getX() + 8, 
                               WorldPanel.panel.getY() + 8), "img");
                    
                    WorldPanel.panel.setCursor (c);
                }
                else{
                    WorldPanel.panel.setCursor (Cursor.getDefaultCursor());
                }
            }
        };
        addKey(scope, KeyEvent.VK_Q, "scope", true);
        Action scope2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                ((EntityPlayer)e).zoomed = false;
                WorldPanel.panel.setCursor (Cursor.getDefaultCursor());
                if (lockedBindings.contains(new Character('Q'))) {
                    lockedBindings.remove(new Character('Q'));
                }
            }
        };
        addKey(scope2, KeyEvent.VK_Q, "unscope", false);
        
        Action skip = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (WorldPanel.panel.wordTimer > 0){
                    WorldPanel.panel.wordTimer = 0;
                }
                else{
                    WorldPanel.panel.textTimer = 0;
                }
                
                
            }
        };
        addKey(skip, KeyEvent.VK_ENTER, "skip", true);
        
        Action chip = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ef) {
                if (((EntityPlayer)e).chip != null){
                    ((EntityPlayer)e).chip.activate((EntityPlayer) e);
                }
            }
        };
        addKey(chip, KeyEvent.VK_F, "chip", true);
        
        
        setupMouse();
    }
    public void setupMouse(){
        WorldPanel.panel.addMouseWheelListener(this);
        WorldPanel.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev){
                if (ev.getButton() == MouseEvent.BUTTON1){
                    isClicking = true;
                }
                //WorldPanel.panel.requestFocusInWindow();
            }
            @Override
            public void mouseReleased(MouseEvent ev){
                if (ev.getButton() == MouseEvent.BUTTON1){
                    isClicking = false;
                }
                //WorldPanel.panel.requestFocusInWindow();
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (WorldPanel.panel.click(WorldPanel.pointerLocation)){
                    return;
                }
                if (ev.getButton() == MouseEvent.BUTTON3){
                    for (int i = 0; i < e.world.entities.size(); i++){
                        if (e.world.entities.get(i).getPosOnScreen((int)e.x, (int)e.y).contains(new Point((int)e.world.getXOnScreen(e.world.getXInWorld(WorldPanel.pointerLocation.getX())), (int)e.world.getYOnScreen(e.world.getYInWorld(WorldPanel.pointerLocation.getY()))))){
                            if (Math.abs(e.world.entities.get(i).x - e.x) < 100 && Math.abs(e.world.entities.get(i).y - e.y) < 100){
                                e.world.entities.get(i).onInteract((EntityPlayer) e);
                            }
                            
                        }
                    }
                }
                else if (ev.getButton() == MouseEvent.BUTTON1){
                    if (e.weapon != null){
                        double xp = e.world.getXInWorld(WorldPanel.pointerLocation.x);//WorldPanel.pointerLocation.x + e.x - WorldPanel.panel.getWidth() / 2;
                        double yp = e.world.getYInWorld(WorldPanel.pointerLocation.y);//WorldPanel.pointerLocation.y + e.y - WorldPanel.panel.getHeight() / 2;
                        e.weapon.fireBullet(new Point((int)xp, (int)yp), e);
                        
                    }
                }
                //WorldPanel.panel.requestFocusInWindow();
            }
        });

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (WorldPanel.panel.scroll(mwe.getWheelRotation())) {
            return;
        }
        for (int i = 0; i < Math.abs(mwe.getWheelRotation()); i++){
            if (mwe.getWheelRotation() > 0){
                int index = e.inventory.indexOf(e.weapon) + 1;
                for (; index < e.inventory.size(); index++){
                    if (e.inventory.get(index) instanceof ItemWeapon){
                        e.weapon = (ItemWeapon) e.inventory.get(index);
                        break;
                    }
                }
                if (index == e.inventory.size()){
                    index = 0;
                    for (; index < e.inventory.size(); index++){
                        if (e.inventory.get(index) instanceof ItemWeapon){
                            e.weapon = (ItemWeapon) e.inventory.get(index);
                            break;
                        }
                    }
                }
            }
            else if (mwe.getWheelRotation() < 0){
                int index = e.inventory.indexOf(e.weapon) - 1;
                for (; index >= 0; index--){
                    if (e.inventory.get(index) instanceof ItemWeapon){
                        e.weapon = (ItemWeapon) e.inventory.get(index);
                        break;
                    }
                }
                if (index == -1){
                    index = e.inventory.size() - 1;
                    for (; index >= 0; index--){
                        if (e.inventory.get(index) instanceof ItemWeapon){
                            e.weapon = (ItemWeapon) e.inventory.get(index);
                            break;
                        }
                    }
                }
            }
        }
    }
}
