package judge;

import audio.AudioBackend;

public class AccuracyJudgement extends JudgementSystem {

    private final TimingWindow window =
            new TimingWindow(0, 50, 100, 150, 200);

    public AccuracyJudgement(AudioBackend backend) {
        super(backend);
    }

    @Override
    public Judgement judge(TapEvent tap, long noteTimeMs) {
        long delta = Math.abs(tap.timestampMs() - noteTimeMs);
        Judgement result;
        if (delta <= window.perfectMs)      result = Judgement.PERFECT;
        else if (delta <= window.greatMs)   result = Judgement.GREAT;
        else if (delta <= window.goodMs)    result = Judgement.GOOD;
        else if (delta <= window.badMs)     result = Judgement.BAD;
        else                                result = Judgement.MISS;
        try {
            playFeedback(result);
        } catch (Exception ignored) {
        }
        return result;
    }
}