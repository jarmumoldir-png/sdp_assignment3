import audio.AudioBackend;
import audio.AudioBackendFactory;
import judge.AccuracyJudgement;
import judge.Judgement;
import judge.JudgementSystem;
import judge.TapEvent;

public class Main {

    public static void main(String[] args) {
        AudioBackendFactory factory = new AudioBackendFactory();

        AudioBackend backend =
                factory.forPlatform(AudioBackendFactory.Platform.RETRO);

        JudgementSystem system = new AccuracyJudgement(backend);

        TapEvent tap = new TapEvent(3, 1000, false);
        Judgement result = system.judge(tap, 1020);

        System.out.println("Backend: " + system.backendName());
        System.out.println("Judgment: " + result);
    }
}