package audio;

public class WebAudioBackend implements AudioBackend {

    private int nextHandleId = 1;

    @Override
    public AudioHandle playSample(SampleId id, int volume, int pan)
            throws AudioException {
        if (volume < 0 || volume > 100) {
            throw new AudioException(AudioException.Reason.UNKNOWN,
                    "volume out of range: " + volume);
        }
        return new AudioHandle(nextHandleId++);
    }

    @Override
    public void stopAll() {}

    @Override
    public String backendName() {
        return "WebAudio";
    }
}