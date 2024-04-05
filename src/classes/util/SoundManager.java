package classes.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {

    private static SoundManager soundManagerInstance;
    private HashMap<String, Clip> audioLibrary;
    private SoundManager() {}

    public static SoundManager getInstance() {
        if(soundManagerInstance == null) {
            soundManagerInstance = new SoundManager();
        }

        return soundManagerInstance;
    }


    public void setupAudioLibrary() {
        String filePath = "src" + File.separator + "res" + File.separator + "audio" + File.separator;
        File directory = new File(filePath);
        audioLibrary = new HashMap<>();

        try {
            searchForAudioFiles(directory);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HashMap<String, Clip> getAudioLibrary() {
        return audioLibrary;
    }

    private void searchForAudioFiles(File directory) throws LineUnavailableException, IOException {
        File[] files = directory.listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                searchForAudioFiles(file);
            } else {
                if (isAudioFile(file)) {
                    AudioInputStream audioFile = getAudioFile(file);
                    String fileName = getFileName(file);
                    Clip clip = AudioSystem.getClip();

                    try {
                        clip.open(audioFile);
                    } catch (LineUnavailableException e) {
                        throw new LineUnavailableException();
                    }
                    audioLibrary.put(fileName, clip);
                    System.out.println("LOADED AUDIO: {" + fileName + ", " + audioFile);
                }
            }
        }

    }

    private AudioInputStream getAudioFile(File file) {
        if(file.isFile() && isAudioFile(file)) {
            try {
                return AudioSystem.getAudioInputStream(file);
            }  catch (UnsupportedAudioFileException e) {
                throw new RuntimeException("Audio file is not supported: " + file.getAbsolutePath(), e);
            } catch (IOException e) {
                throw new RuntimeException("Error loading audio from file: " + file.getAbsolutePath(), e);
            }
        }

        return null;
    }

    private boolean isAudioFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".wav");
    }

    private String getFileName(File file) {
        String fileName = file.getName();
        int extensionIndex = fileName.lastIndexOf('.');

        if(extensionIndex > 0) {
            return fileName.substring(0, extensionIndex);
        }

        return fileName;
    }
}
