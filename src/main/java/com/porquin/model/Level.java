package com.porquin.model;

public class Level {
    private double nextLevel;
    private double percent;

    public Level(double nextLevel, double percent) {
        this.nextLevel = nextLevel;
        this.percent = percent;
    }

    public String toString() {
        return this.nextLevel + " " + this.percent;
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
}