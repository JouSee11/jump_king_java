package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * The SoundController class is responsible for managing sound effects and background music
 * in the application. It provides methods to play, stop, and adjust the volume of sounds.
 * This class follows the Singleton design pattern to ensure a single instance is used.
 */
public class SoundController {
    private static SoundController instance;

    private Map<String, AudioClip> soundEffects;
    private MediaPlayer musicPlayer;
    private double effectsVolume = 0.7;
    private double musicVolume = 0.5;
    private boolean muted = false;

    private String lastPlayedSound;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the sound effects map and loads the sounds.
     */
    private SoundController() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    /**
     * Returns the singleton instance of the SoundController.
     *
     * @return The SoundController instance.
     */
    public static SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
        }
        return instance;
    }

    /**
     * Loads sound effects into the soundEffects map.
     * Each sound effect is associated with a unique name.
     */
    private void loadSounds() {
        // Load sound effects
        loadEffect("keyCollect1", "/sounds/SFX/voices/keyCollect/keyCollect1.mp3");
        loadEffect("keyCollect2", "/sounds/SFX/voices/keyCollect/keyCollect2.mp3");
        loadEffect("keyCollect3", "/sounds/SFX/voices/keyCollect/keyCollect3.mp3");
        loadEffect("keyCollect4", "/sounds/SFX/voices/keyCollect/keyCollect4.mp3");
        loadEffect("keyCollect5", "/sounds/SFX/voices/keyCollect/keyCollect5.mp3");

        loadEffect("collectSuccess", "/sounds/SFX/collectSuccess.mp3");
        loadEffect("collectedAllKeys", "/sounds/SFX/voices/allKeysCollected.mp3");
        loadEffect("startingMsg", "/sounds/SFX/voices/startingMessage.mp3");

        loadEffect("powerUpCollected", "/sounds/SFX/powerUpCollect.mp3");

        //player movement
        loadEffect("jump", "/sounds/SFX/jump/jump.mp3");
        loadEffect("jump1", "/sounds/SFX/jump/jump1.mp3");
        loadEffect("jump2", "/sounds/SFX/jump/jump2.mp3");
        loadEffect("jump3", "/sounds/SFX/jump/jump3.mp3");
        loadEffect("jump4", "/sounds/SFX/jump/jump4.mp3");
        loadEffect("jump5", "/sounds/SFX/jump/jump5.mp3");

        loadEffect("bump", "/sounds/SFX/bump/bump.mp3");
        loadEffect("bump1", "/sounds/SFX/bump/bump1.mp3");
        loadEffect("bump2", "/sounds/SFX/bump/bump2.mp3");

        loadEffect("fall", "/sounds/SFX/fall.mp3");

        loadEffect("denied", "/sounds/SFX/denied.mp3");
        loadEffect("victory", "/sounds/SFX/voices/victory.mp3");
    }

    /**
     * Loads a single sound effect and adds it to the soundEffects map.
     *
     * @param name The unique name of the sound effect.
     * @param path The file path to the sound effect.
     */
    private void loadEffect(String name, String path) {
        URL resource = getClass().getResource(path);
        if (resource != null) {
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(effectsVolume);
            soundEffects.put(name, clip);
        } else {
            System.err.println("Sound file not found: " + path);
        }
    }

    /**
     * Plays a random key collection sound effect.
     */
    public void playRandomKeyCollect() {
        int randomIndex = (int) (Math.random() * 5) + 1;
        String soundName = "keyCollect"  + randomIndex;
        playSound(soundName,1);
    }


    /**
     * Plays a random jump sound effect.
     */
    public void playRandomJump() {
        playSound("jump", 1);
        int randomIndex = (int) (Math.random() * 5) + 1;
        String jumpEffectName = "jump"  + randomIndex;
        playSound(jumpEffectName,0.2);
    }

    /**
     * Plays a sound effect by its name.
     *
     * @param name   The name of the sound effect to play.
     * @param volume The volume at which to play the sound effect.
     */
    public void playSound(String name, double volume) {
        if (muted || name.equalsIgnoreCase(lastPlayedSound)) return;


        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.play(volume);
            lastPlayedSound = name;
        }
    }

    /**
     * Plays background music from the specified file.
     *
     * @param musicFile The name of the music file to play.
     */
    public void playMusic(String musicFile) {
        if (musicPlayer != null) {
            MediaPlayer oldPlayer = musicPlayer;
            fadeMusic(0, musicPlayer, oldPlayer::stop);
        }

        URL resource = getClass().getResource("/sounds/music/" + musicFile);
        if (resource != null) {
            Media music = new Media(resource.toExternalForm());
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);


            if (!muted) {
                musicPlayer.setVolume(0);
                musicPlayer.play();
                fadeMusic(musicVolume, musicPlayer, null);
            }
        }
    }

    /**
     * Fades the volume of the music player to a target volume over a duration.
     *
     * @param targetVolume The target volume to fade to.
     * @param musicPlayer  The MediaPlayer instance to fade.
     * @param onFinish     A Runnable to execute when the fade is complete.
     */
    private void fadeMusic(double targetVolume, MediaPlayer musicPlayer, Runnable onFinish) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(musicPlayer.volumeProperty(), targetVolume)));

        if (onFinish != null) {
            timeline.setOnFinished(e -> onFinish.run());
        }
        timeline.play();
    }


    public void setEffectsVolume(double volume) {
        this.effectsVolume = volume;
        soundEffects.values().forEach(clip -> clip.setVolume(volume));
    }


    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        if (musicPlayer != null) {
            musicPlayer.setVolume(volume);
        }
    }

    /**
     * Toggles the mute state for all sounds.
     * If muted, all sounds are paused; otherwise, they are resumed.
     */
    public void toggleMute() {
        muted = !muted;
        if (muted) {
            if (musicPlayer != null) {
                musicPlayer.pause();
            }
        } else {
            if (musicPlayer != null) {
                musicPlayer.play();
            }
        }
    }

    /**
     * Stops all currently playing sounds, including sound effects and background music.
     */
    public void stopAllSounds() {
        // Stop any currently playing sound effects
        if (musicPlayer != null) {
            musicPlayer.pause();
        }

        // Stop all sound effects that might be playing
        for (AudioClip clip : soundEffects.values()) {
            clip.stop();
        }

        // Clear last played sound to allow sounds to play again when game restarts
        lastPlayedSound = null;
    }

    /**
     * Resumes all sounds that were previously paused.
     */
    public void resumeAllSounds() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }


}