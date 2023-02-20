package com.azyf.finalyearproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


public class SoundController {
<<<<<<< HEAD
       private ArrayList<String> sounds;
       private  File[] files;

       private  MediaPlayer player;
       private  MediaPlayer loopSound;

       private double volumeLevel;

=======
        ArrayList<String> sounds;
        File[] files;
        MediaPlayer player;
        MediaPlayer loopSound;
>>>>>>> accb1db992d791c84ee4f8a4aac1cd133ad6d039

    public SoundController() {
        sounds = new ArrayList<>();
        String filePath = StageInitializer.getAbsolutePath() + "/Assets/Sounds";
        File dir = new File(filePath);
        files = dir.listFiles();


        for(int i =0; i < files.length; i++) {
            sounds.add(files[i].getName());
        }
        volumeLevel = 1.0;

    }

    public String[] getSoundFileNamesAsArray() {
        String[] soundFileNames = new String[this.sounds.size()];
        for (int i = 0; i < this.sounds.size(); i++) {
            soundFileNames[i] = this.sounds.get(i);
        }
        return soundFileNames;
    }

    public void playSound(String soundName) {
         player = new MediaPlayer(getMedia(soundName));
         player.play();
    }


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

    public void pressedStopButton() {
        if(loopSound != null) {
            loopSound.stop();
        }
        if(player != null) {
            player.stop();
        }


    }


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

    public void increaseVolume() {
        volumeLevel = volumeLevel + 0.1;
        setVolumeLevel();
    }

    public void decreaseVolume() {
        volumeLevel = volumeLevel - 0.1;
       setVolumeLevel();
    }

    private void setVolumeLevel() {
        if(loopSound != null) {
            loopSound.setVolume(volumeLevel);
        }
        if(player != null) {
           player.setVolume(volumeLevel);

        }
    }


}
