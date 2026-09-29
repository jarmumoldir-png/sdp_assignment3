package audio;

public class AudioException extends Exception {

    public enum Reason {
        DEVICE_LOST,
        BUFFER_OVERRUN,
        UNSUPPORTED_SAMPLE,
        UNKNOWN
    }

    private final Reason reason;

    public AudioException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public AudioException(Reason reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}