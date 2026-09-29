package judge;

public class TapEvent {

    private final int lane;
    private final long timestampMs;
    private final boolean hold;

    public TapEvent(int lane, long timestampMs, boolean hold) {
        this.lane = lane;
        this.timestampMs = timestampMs;
        this.hold = hold;
    }

    public int lane() { return lane; }
    public long timestampMs() { return timestampMs; }
    public boolean isHold() { return hold; }
}