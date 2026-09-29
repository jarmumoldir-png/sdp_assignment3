package audio;

public class AudioHandle {

    private final int id;
    private boolean active = true;

    public AudioHandle(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void stop() {
        active = false;
    }
}