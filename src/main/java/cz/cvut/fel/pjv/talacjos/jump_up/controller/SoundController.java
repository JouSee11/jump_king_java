package cz.cvut.fel.pjv.talacjos.jump_up.controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundController {
    private static SoundController instance;

    private Map<String, AudioClip> soundEffects;
    private MediaPlayer musicPlayer;
    private double effectsVolume = 0.7;
    private double musicVolume = 0.5;
    private boolean muted = false;

    private SoundController() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    public static SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
        }
        return instance;
    }

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
    }

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

    public void playRandomKeyCollect() {
        int randomIndex = (int) (Math.random() * 5) + 1;
        AudioClip clip = soundEffects.get("keyCollect"  + randomIndex);
        clip.play();
    }

    public void playSound(String name) {
        if (muted) return;

        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.play();
        }
    }


    public void playMusic(String musicFile) {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }

        URL resource = getClass().getResource("/sounds/music/" + musicFile);
        if (resource != null) {
            Media music = new Media(resource.toExternalForm());
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setVolume(musicVolume);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            if (!muted) {
                musicPlayer.play();
            }
        }
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
}