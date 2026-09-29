package audio;

import audio.legacy.LegacyDirectSoundBackend;

public class AudioBackendFactory {

    public enum Platform {
        BROWSER,
        DESKTOP,
        RETRO
    }

    public AudioBackend forPlatform(Platform platform) {
        switch (platform) {
            case BROWSER: return new WebAudioBackend();
            case DESKTOP: return new NativeAudioBackend();
            case RETRO:   return new DirectSoundAdapter(new LegacyDirectSoundBackend());
            default:      throw new IllegalArgumentException("Unknown platform: " + platform);
        }
    }
}