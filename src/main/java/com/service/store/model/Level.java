package com.service.store.model;

public class Level {
    private double nextLevel;
    private double percent;
    private double days;

    public Level(double nextLevel, double percent, double days) {
        this.nextLevel = nextLevel;
        this.percent = percent;
        this.days = days;
    }

    public String toString() {
        return this.nextLevel + " " + this.percent + " " + this.days;
    }

    /**
     * @return the percent
     */
    public double getPercent() {
        return percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(double percent) {
        this.percent = percent;
    }

    /**
     * @return the nextLevel
     */
    public double getNextLevel() {
        return nextLevel;
    }

    /**
     * @param nextLevel the nextLevel to set
     */
    public void setNextLevel(double nextLevel) {
        this.nextLevel = nextLevel;
    }

    /**
     * @return the days
     */
    public double getDays() {
        return days;
    }
}