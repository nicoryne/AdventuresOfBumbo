package classes.util.handlers;

import classes.util.SoundManager;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public abstract class SoundHandler {

    public static void playAudio(String fileName, int loopCount, float volume) {
        Clip clip = getClip(fileName);

        clip.loop(loopCount);
        setVolume(volume, clip);
        clip.start();
    }

    public static void stopAudio(String fileName) {
        Clip clip = getClip(fileName);

        clip.stop();
    }

    public static void setVolume(float volume, Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private static Clip getClip(String fileName) {
        SoundManager soundManager = SoundManager.getInstance();
        System.out.println(fileName);
        return soundManager.getAudioLibrary().get(fileName);
    }

}
