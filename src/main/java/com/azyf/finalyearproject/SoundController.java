/**
 * The controller for all the sounds. This all stores the sounds as well.
 */
package com.azyf.finalyearproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


public class SoundController {

       private ArrayList<String> sounds;
       private  File[] files;

       private  MediaPlayer player;
       private  MediaPlayer loopSound;

       private double volumeLevel;

    /**
     * The constructor for the sound controller.
     */
    public SoundController() {
        sounds = new ArrayList<>();
        String filePath = FileController.getAbsolutePath() + "/Assets/Sounds";
        File dir = new File(filePath);
        files = dir.listFiles();


        for(int i =0; i < files.length; i++) {
            sounds.add(files[i].getName());
        }
        volumeLevel = 1.0;

    }

    /**
     * Gets an array of the sounds stored in controller.
     * @return An array list of the sounds.
     */
    public String[] getSoundFileNamesAsArray() {
        String[] soundFileNames = new String[this.sounds.size()];
        for (int i = 0; i < this.sounds.size(); i++) {
            soundFileNames[i] = this.sounds.get(i);
        }
        return soundFileNames;
    }

    /**
     * Plays the specified sound.
     * @param soundName The name of the sound.
     */
    public void playSound(String soundName) {
         player = new MediaPlayer(getMedia(soundName));
         player.play();
    }

    /**
     * Loops the specified sound.
     * @param soundName The name of the sound.
     */
    public void loopSound(String soundName) {

        loopSound = new MediaPlayer(getMedia(soundName));
        loopSound.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                loopSound.seek(Duration.ZERO);
                loopSound.play();
            }
        });

        loopSound.play();
        System.out.println(loopSound.getVolume());

    }

    /**
     * Stops playing the sound.
     */
    public void pressedStopButton() {
        if(loopSound != null) {
            loopSound.stop();
        }
        if(player != null) {
            player.stop();
        }


    }


    /**
     * Gets the sound to play.
     * @param soundName The name of the sound.
     * @return The sound as Media objecy.
     */
    private Media getMedia(String soundName) {
        File soundFile = null;
        for(int i = 0; i < files.length; i++) {
            if(files[i].getName().equals(soundName)) {
                soundFile = files[i].getAbsoluteFile();
            }
        }
        URI tmpURI  = soundFile.toURI();

        Media sound = new Media(tmpURI.toString());
        return  sound;
    }

    /**
     * Increase the volume of the sound controller.
     */
    public void increaseVolume() {
        volumeLevel = volumeLevel + 0.1;
        setVolumeLevel();
    }

    /**
     * Decrease the volume of the sound controller.
     */
    public void decreaseVolume() {
        volumeLevel = volumeLevel - 0.1;
       setVolumeLevel();
    }

    /**
     * Sets the volume level.
     */
    private void setVolumeLevel() {
        if(loopSound != null) {
            loopSound.setVolume(volumeLevel);
        }
        if(player != null) {
           player.setVolume(volumeLevel);

        }
    }


}
