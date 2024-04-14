package game.util.managers;

import services.LoggerHelper;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SoundManager {

    private static SoundManager soundManagerInstance;

    private final Map<String, Clip> audioLibrary;

    private SoundManager() {
        audioLibrary = new ConcurrentHashMap<>();
        setupAudioLibrary();
    }

    public static synchronized SoundManager getInstance() {
        if(soundManagerInstance == null) {
            soundManagerInstance = new SoundManager();
        }

        return soundManagerInstance;
    }

    public synchronized void setupAudioLibrary() {
        String filePath = "src" + File.separator + "res" + File.separator + "audio" + File.separator;
        File directory = new File(filePath);

        searchForAudioFiles(directory);
    }

    private synchronized void searchForAudioFiles(File directory) {
        File[] files = directory.listFiles();

        if(files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                searchForAudioFiles(file);
            } else if(isAudioFile(file)) {
                addFileToAudioLibrary(file);
            }
        }
    }

    private void addFileToAudioLibrary(File file) {
        AudioInputStream audioFile = getAudioFile(file);

        if(audioFile == null) {
            return;
        }

        String fileName = getFileName(file);
        Clip clip = null;

        try {
            clip = AudioSystem.getClip();
            clip.open(audioFile);
        } catch (LineUnavailableException e) {
            LoggerHelper.logError("Error opening clip from audio file: ", e);
        } catch (IOException e) {
            LoggerHelper.logError("Error opening audio file: " + file.getAbsolutePath(), e);
        }

        audioLibrary.put(fileName, clip);
        LoggerHelper.logInfo("[SoundManager] Loaded audio file: " + file.getName());

        try {
            audioFile.close();
        } catch (IOException e) {
            LoggerHelper.logError("Error closing audio file: " + file.getName(), e);
        }
    }

    private AudioInputStream getAudioFile(File file) {
        if(file.isFile() && isAudioFile(file)) {
            try {
                return AudioSystem.getAudioInputStream(file);
            }  catch (UnsupportedAudioFileException e) {
                LoggerHelper.logError("Audio file is not supported: " + file.getAbsolutePath(), e);
            } catch (IOException e) {
                LoggerHelper.logError("Error loading audio from file: " + file.getAbsolutePath(), e);
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

    public synchronized Map<String, Clip> getAudioLibrary() {
        return audioLibrary;
    }
}
