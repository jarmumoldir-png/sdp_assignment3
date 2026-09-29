package judge;

public class TimingWindow {

    public final long marvelousMs;
    public final long perfectMs;
    public final long greatMs;
    public final long goodMs;
    public final long badMs;

    public TimingWindow(long marvelousMs, long perfectMs,
                        long greatMs, long goodMs, long badMs) {
        this.marvelousMs = marvelousMs;
        this.perfectMs = perfectMs;
        this.greatMs = greatMs;
        this.goodMs = goodMs;
        this.badMs = badMs;
    }
}