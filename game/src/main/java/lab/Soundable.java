package lab;

import javafx.scene.media.AudioClip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Soundable {
    default void playSound(String soundName) {
        playSound(soundName, 1.0);
    }

    default void playSound(String soundName, double rate) {
        AudioClip clip = ResourceManager.getAudioClip(getClass(), soundName);
        if (clip != null) {
            clip.setRate(rate);
            clip.play();
        }
    }

    default void playSound(String soundName, double rate, double volume) {
        AudioClip clip = ResourceManager.getAudioClip(getClass(), soundName);
        if (clip != null) {
            clip.setRate(rate);
            clip.play(volume);
        }
    }
}
