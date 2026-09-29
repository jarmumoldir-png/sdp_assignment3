package judge;

import audio.AudioBackend;
import audio.AudioException;
import audio.SampleId;

public abstract class JudgementSystem {

    protected final AudioBackend backend;

    protected JudgementSystem(AudioBackend backend) {
        this.backend = backend;
    }

    public abstract Judgement judge(TapEvent tap, long noteTimeMs);

    protected void playFeedback(Judgement j) throws AudioException {
        SampleId id;
        switch (j) {
            case MISS: id = SampleId.MISS_SFX; break;
            default:   id = SampleId.TAP;      break;
        }
        backend.playSample(id, 80, 0);
    }

    public String backendName() {
        return backend.backendName();
    }
}