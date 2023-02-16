package com.azyf.finalyearproject;

import java.io.File;
import java.util.ArrayList;

public class SoundController {
        ArrayList<String> sounds;

        public void soundController(){
            sounds = new ArrayList<>();
            String filePath = StageInitializer.getAbsolutePath() + "/Assets/Sounds";
            File dir = new File(filePath);
            File[] files = dir.listFiles();

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


}
