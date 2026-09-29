package audio;

public interface AudioBackend {

    AudioHandle playSample(SampleId id, int volume, int pan)
            throws AudioException;

    void stopAll();

    String backendName();
}