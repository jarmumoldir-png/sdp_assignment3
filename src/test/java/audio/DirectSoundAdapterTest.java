package audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import audio.legacy.DirectSoundFault;
import audio.legacy.LegacyDirectSoundBackend;

class DirectSoundAdapterTest {

    @Test
    void successfulPlayReturnsHandle() throws Exception {
        LegacyDirectSoundBackend legacy = mock(LegacyDirectSoundBackend.class);
        when(legacy.playSample(anyInt(), anyInt(), anyInt()))
                .thenReturn(LegacyDirectSoundBackend.OK);

        DirectSoundAdapter adapter = new DirectSoundAdapter(legacy);
        AudioHandle handle = adapter.playSample(SampleId.TAP, 80, 0);

        assertNotNull(handle);
        assertTrue(handle.isActive());
    }

    @Test
    void deviceLostCodeBecomesDeviceLostReason() {
        LegacyDirectSoundBackend legacy = mock(LegacyDirectSoundBackend.class);
        when(legacy.playSample(anyInt(), anyInt(), anyInt()))
                .thenReturn(LegacyDirectSoundBackend.ERR_DEVICE_LOST);

        DirectSoundAdapter adapter = new DirectSoundAdapter(legacy);
        AudioException ex = assertThrows(AudioException.class,
                () -> adapter.playSample(SampleId.TAP, 80, 0));

        assertEquals(AudioException.Reason.DEVICE_LOST, ex.reason());
    }

    @Test
    void bufferOverrunBecomesBufferOverrunReason() {
        LegacyDirectSoundBackend legacy = mock(LegacyDirectSoundBackend.class);
        when(legacy.playSample(anyInt(), anyInt(), anyInt()))
                .thenReturn(LegacyDirectSoundBackend.ERR_BUFFER);

        DirectSoundAdapter adapter = new DirectSoundAdapter(legacy);
        AudioException ex = assertThrows(AudioException.class,
                () -> adapter.playSample(SampleId.TAP, 80, 0));

        assertEquals(AudioException.Reason.BUFFER_OVERRUN, ex.reason());
    }

    @Test
    void badSampleBecomesUnsupportedSampleReason() {
        LegacyDirectSoundBackend legacy = mock(LegacyDirectSoundBackend.class);
        when(legacy.playSample(anyInt(), anyInt(), anyInt()))
                .thenReturn(LegacyDirectSoundBackend.ERR_BAD_SAMPLE);

        DirectSoundAdapter adapter = new DirectSoundAdapter(legacy);
        AudioException ex = assertThrows(AudioException.class,
                () -> adapter.playSample(SampleId.TAP, 80, 0));

        assertEquals(AudioException.Reason.UNSUPPORTED_SAMPLE, ex.reason());
    }

    @Test
    void directSoundFaultBecomesDeviceLost() {
        LegacyDirectSoundBackend legacy = mock(LegacyDirectSoundBackend.class);
        when(legacy.playSample(anyInt(), anyInt(), anyInt()))
                .thenThrow(new DirectSoundFault("device vanished"));

        DirectSoundAdapter adapter = new DirectSoundAdapter(legacy);
        AudioException ex = assertThrows(AudioException.class,
                () -> adapter.playSample(SampleId.TAP, 80, 0));

        assertEquals(AudioException.Reason.DEVICE_LOST, ex.reason());
        assertTrue(ex.getCause() instanceof DirectSoundFault);
    }
}