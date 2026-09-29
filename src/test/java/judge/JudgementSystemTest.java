package judge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import audio.AudioBackend;
import audio.AudioException;
import audio.SampleId;

class JudgementSystemTest {

    @Test
    void perfectOnlyDelegatesToBackendOnHit() throws Exception {
        AudioBackend backend = mock(AudioBackend.class);
        when(backend.playSample(any(SampleId.class), anyInt(), anyInt()))
                .thenReturn(null);

        PerfectOnlyJudgement system = new PerfectOnlyJudgement(backend);
        Judgement j = system.judge(new TapEvent(0, 1000, false), 1010);

        assertEquals(Judgement.PERFECT, j);
        verify(backend, times(1))
                .playSample(eq(SampleId.TAP), anyInt(), anyInt());
    }

    @Test
    void accuracyDelegatesToBackendOnMiss() throws Exception {
        AudioBackend backend = mock(AudioBackend.class);
        when(backend.playSample(any(SampleId.class), anyInt(), anyInt()))
                .thenReturn(null);

        AccuracyJudgement system = new AccuracyJudgement(backend);
        Judgement j = system.judge(new TapEvent(0, 1000, false), 1500);

        assertEquals(Judgement.MISS, j);
        verify(backend, times(1))
                .playSample(eq(SampleId.MISS_SFX), anyInt(), anyInt());
    }

    @Test
    void backendFailureDoesNotBreakJudgment() throws Exception {
        AudioBackend backend = mock(AudioBackend.class);
        when(backend.playSample(any(SampleId.class), anyInt(), anyInt()))
                .thenThrow(new AudioException(
                        AudioException.Reason.DEVICE_LOST, "boom"));

        AccuracyJudgement system = new AccuracyJudgement(backend);
        Judgement j = system.judge(new TapEvent(0, 1000, false), 1010);
        assertEquals(Judgement.PERFECT, j);
    }
}