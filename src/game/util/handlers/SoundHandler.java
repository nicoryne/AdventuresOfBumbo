package game.util.handlers;

import game.util.managers.SoundManager;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public abstract class SoundHandler {

    public static void playAudio(String fileName, int loopCount, float volume) {
        Clip clip = getClip(fileName);

        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                // This is to ensure the clip rewinds properly once finished
                clip.setMicrosecondPosition(0);
            }
        });

        setVolume(volume, clip);
        clip.start();
        clip.loop(loopCount);
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
        return soundManager.getAudioLibrary().get(fileName);
    }

}
