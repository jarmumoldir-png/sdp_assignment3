package audio;

import audio.legacy.DirectSoundFault;
import audio.legacy.LegacyDirectSoundBackend;

public class DirectSoundAdapter implements AudioBackend {

    private final LegacyDirectSoundBackend legacy;
    private int nextHandleId = 5000;

    public DirectSoundAdapter(LegacyDirectSoundBackend legacy) {
        this.legacy = legacy;
    }

    @Override
    public AudioHandle playSample(SampleId id, int volume, int pan)
            throws AudioException {
        int legacyId = toLegacyId(id);

        short code;
        try {
            code = legacy.playSample(legacyId, volume, pan);
        } catch (DirectSoundFault fault) {
            throw new AudioException(
                    AudioException.Reason.DEVICE_LOST,
                    "DirectSound fault: " + fault.getMessage(),
                    fault);
        }

        if (code == LegacyDirectSoundBackend.OK) {
            return new AudioHandle(nextHandleId++);
        }
        throw translate(code);
    }

    @Override
    public void stopAll() {}

    @Override
    public String backendName() {
        return "DirectSound(adapted)";
    }

    private int toLegacyId(SampleId id) {
        switch (id) {
            case TAP:        return 1;
            case HOLD_START: return 2;
            case HOLD_END:   return 3;
            case FLICK:      return 4;
            case MISS_SFX:   return 5;
            default:         return -1;
        }
    }

    private AudioException translate(short code) {
        switch (code) {
            case LegacyDirectSoundBackend.ERR_DEVICE_LOST:
                return new AudioException(AudioException.Reason.DEVICE_LOST,
                        "DirectSound device lost");
            case LegacyDirectSoundBackend.ERR_BUFFER:
                return new AudioException(AudioException.Reason.BUFFER_OVERRUN,
                        "DirectSound buffer overrun");
            case LegacyDirectSoundBackend.ERR_BAD_SAMPLE:
                return new AudioException(AudioException.Reason.UNSUPPORTED_SAMPLE,
                        "DirectSound sample not loaded");
            default:
                return new AudioException(AudioException.Reason.UNKNOWN,
                        "DirectSound returned code " + code);
        }
    }
}