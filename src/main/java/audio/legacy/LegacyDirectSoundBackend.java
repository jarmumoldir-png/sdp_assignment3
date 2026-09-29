package audio.legacy;

public class LegacyDirectSoundBackend {

    public static final short OK              = 0;
    public static final short ERR_DEVICE_LOST = -1;
    public static final short ERR_BUFFER      = -2;
    public static final short ERR_BAD_SAMPLE  = -3;

    public short playSample(int sampleId, int volume, int pan) {
        if (sampleId < 0) {
            return ERR_BAD_SAMPLE;
        }
        if (volume < 0 || volume > 100) {
            return ERR_BUFFER;
        }
        if (pan == 999) {
            throw new DirectSoundFault("DirectSound device vanished");
        }
        return OK;
    }
}