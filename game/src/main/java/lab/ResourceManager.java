package lab;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static lab.NameGenerator.RANDOM;

@Log4j2
@UtilityClass
public class ResourceManager {

    private static final Map<String, Image> images = new HashMap<>();
    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static final Map<String, Media> music = new HashMap<>();

    public static Image getImage(Class<?> clazz, String name) {
        if (images.containsKey(name)) {
            return images.get(name);
        } else {
            try {
                if (clazz.getResource(name) == null) {
                    log.error("Image not found: {}", name);
                    return null;
                }
                Image image = new Image(Objects.requireNonNull(clazz.getResourceAsStream(name)));
                images.put(name, image);
                return image;
            } catch (Exception e) {
                log.error("Error loading image: {}", name, e);
                return null;
            }
        }
    }

    public static AudioClip getAudioClip(Class<?> clazz, String name) {
        if (sounds.containsKey(name)) {
            return sounds.get(name);
        } else {
            try {
                var resource = clazz.getResource(name);
                if (resource == null) {
                    log.error("Audio not found: {}", name);
                    return null;
                }

                String path = resource.toExternalForm();
                AudioClip clip = new AudioClip(path);
                sounds.put(name, clip);
                return clip;
            } catch (Exception e) {
                log.error("Error loading audio: {}", name, e);
                return null;
            }
        }
    }

    public static Media getMedia(Class<?> clazz, String name) {
        if (music.containsKey(name)) {
            return music.get(name);
        } else {
            try {
                var resource = clazz.getResource(name);
                if (resource == null) {
                    log.error("Music file not found: {}", name);
                    return null;
                }

                String path = resource.toExternalForm();
                Media media = new Media(path);
                music.put(name, media);
                return media;
            } catch (Exception e) {
                log.error("Error loading music: {}", name, e);
                return null;
            }
        }
    }

    public static <T> T getRandomElement(List<T> elements) {
        if(elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("List elements cannot be null or empty.");
        }
        return elements.get(RANDOM.nextInt(elements.size()));
    }
}
