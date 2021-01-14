/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thecolony;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pdogmuncher
 */
public class Settings implements Serializable{
    File file;
    private TreeMap<String, Boolean> bools;
    public Settings(String file){
        this.file = new File(file);
        ObjectInputStream stream = null;
        if (this.file.exists()){
            try {
                stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.file)));
                bools = (TreeMap<String, Boolean>) stream.readObject();
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            bools = new TreeMap<>();
            save();
        }
        
    }
    public void updateBoolean(String key, boolean value){
        Boolean b = value;
        bools.put(key, b);
        save();
    }
    public boolean getBoolean(String key, Boolean defaultkey){
        if (bools.containsKey(key)){
            return bools.get(key);
        }
        else{
            bools.put(key, defaultkey);
            save();
            return defaultkey;
        }
    }
    public void save(){
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeObject(bools);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
