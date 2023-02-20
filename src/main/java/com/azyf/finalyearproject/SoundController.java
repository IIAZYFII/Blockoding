package com.azyf.finalyearproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


public class SoundController {
        ArrayList<String> sounds;
        File[] files;
        MediaPlayer player;
        MediaPlayer loopSound;

    public SoundController() {
        sounds = new ArrayList<>();
        String filePath = StageInitializer.getAbsolutePath() + "/Assets/Sounds";
        File dir = new File(filePath);
        files = dir.listFiles();


        for(int i =0; i < files.length; i++) {
            sounds.add(files[i].getName());
        }


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
        loopSound.getOnRepeat();
        loopSound.play();
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


}
