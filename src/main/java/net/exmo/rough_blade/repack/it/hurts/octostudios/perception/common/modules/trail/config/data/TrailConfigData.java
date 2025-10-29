package net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data;

import it.hurts.octostudios.octolib.modules.config.annotations.Prop;

import org.joml.Vector3f;


public class TrailConfigData {
    private float size = 0.1F;
    private int maxPoints = 5;
    private float minSpeed = 0.001F;

    public TrailConfigData(float size, int maxPoints, float minSpeed, int updateFrequency, String fadeInColor, String fadeOutColor, Vector3f positionOffset, float backwardShift, float motionShift) {
        this.size = size;
        this.maxPoints = maxPoints;
        this.minSpeed = minSpeed;
        this.updateFrequency = updateFrequency;
        this.fadeInColor = fadeInColor;
        this.fadeOutColor = fadeOutColor;
        this.positionOffset = positionOffset;
        this.backwardShift = backwardShift;
        this.motionShift = motionShift;
    }

    private int updateFrequency = 1;
    private String fadeInColor = "FFFFFFFF";
    private String fadeOutColor = "FFFFFFFF";
    private Vector3f positionOffset = new Vector3f(0F, 0F, 0F);
    private float backwardShift = 0F;
    private float motionShift = 0.25F;

    public TrailConfigData() {
    }

    public float getSize() {
        return size;
    }

    public TrailConfigData setSize(float size) {
        this.size = size;
        return this;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public TrailConfigData setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
        return this;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public TrailConfigData setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
        return this;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public TrailConfigData setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
        return this;
    }

    public String getFadeInColor() {
        return fadeInColor;
    }

    public TrailConfigData setFadeInColor(String fadeInColor) {
        this.fadeInColor = fadeInColor;
        return this;
    }

    public String getFadeOutColor() {
        return fadeOutColor;
    }

    public TrailConfigData setFadeOutColor(String fadeOutColor) {
        this.fadeOutColor = fadeOutColor;
        return this;
    }

    public Vector3f getPositionOffset() {
        return positionOffset;
    }

    public TrailConfigData setPositionOffset(Vector3f positionOffset) {
        this.positionOffset = positionOffset;
        return this;
    }

    public float getBackwardShift() {
        return backwardShift;
    }

    public TrailConfigData setBackwardShift(float backwardShift) {
        this.backwardShift = backwardShift;
        return this;
    }

    public float getMotionShift() {
        return motionShift;
    }

    public TrailConfigData setMotionShift(float motionShift) {
        this.motionShift = motionShift;
        return this;
    }
}