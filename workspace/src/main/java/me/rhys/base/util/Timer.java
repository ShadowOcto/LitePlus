package me.rhys.base.util;

public class Timer {

    private long lastTime;

    public Timer() {
        reset();
    }

    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }

    public boolean hasReached(double miliseconds) {
        return ((System.currentTimeMillis() - lastTime) >= miliseconds);
    }

    public long getLastTime() {
        return lastTime;
    }
}
