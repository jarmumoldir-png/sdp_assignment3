package judge;

import audio.AudioBackend;

public class PerfectOnlyJudgement extends JudgementSystem {

    private final TimingWindow window =
            new TimingWindow(0, 120, 0, 0, 0);

    public PerfectOnlyJudgement(AudioBackend backend) {
        super(backend);
    }

    @Override
    public Judgement judge(TapEvent tap, long noteTimeMs) {
        long delta = Math.abs(tap.timestampMs() - noteTimeMs);
        Judgement result = (delta <= window.perfectMs)
                ? Judgement.PERFECT
                : Judgement.MISS;
        try {
            playFeedback(result);
        } catch (Exception ignored) {}
        return result;
    }
}