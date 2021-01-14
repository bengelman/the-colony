/*
 * Sound from http://soundimage.org/
 */
package thecolony;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


/**
 *
 * @author pdogmuncher
 */
public class AudioPlayer implements Serializable{
    Clip clip = null;
    public String sound;
    public AudioPlayer(String sound){
        
        try {
            this.clip = AudioSystem.getClip();
            AudioInputStream stream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream("/thecolony/resources/sound/" + sound)));
            clip.open(stream);
            
        } catch (LineUnavailableException | UnsupportedAudioFileException ex) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error with path " + "/thecolony/resources/sound/" + sound);
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (OutOfMemoryError er){
            ImageRegistry.images.clear();
            Main.currentFrame.setVisible(false);
            
            
            JOptionPane.showMessageDialog(null, "Out of Memory Error - Consider Allocating Java More Memory");
            System.exit(0);
        } 
        this.sound = sound;
    }
    public void play(int repeats){
        if (Main.settings.getBoolean("muted", Boolean.FALSE)){
            return;
        }
        clip.loop(repeats);
        if (sound.equals("bullet.wav")){
            FloatControl gainControl = 
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
                
        }
        if (sound.equals("heartbeat.wav")){
            FloatControl gainControl = 
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0F);
        }
        clip.start();
        
    }
    public void stop(){
        if (clip != null){
            clip.stop();
            clip.close();
        }
        
    }
    
}

