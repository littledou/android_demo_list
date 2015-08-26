//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import android.graphics.PointF;

public class Face {
    private float browFurrowScore = 0.0f;
    private float browRaiseScore = 0.0F;
    private float engagementScore = 0.0F;
    private float lipCornerDepressorScore = 0.0F;
    private float smileScore = 0.0F;
    private float valenceScore = 0.0F;
    private PointF[] facePoints;

    public Face() {
    }

    public float getBrowFurrowScore() {
        return this.browFurrowScore;
    }

    public float getBrowRaiseScore() {
        return this.browRaiseScore;
    }

    public float getEngagementScore() {
        return this.engagementScore;
    }

    public float getLipCornerDepressorScore() {
        return this.lipCornerDepressorScore;
    }

    public float getSmileScore() {
        return this.smileScore;
    }

    public float getValenceScore() {
        return this.valenceScore;
    }

    public PointF[] getFacePoints() {
        return this.facePoints;
    }

    void setBrowFurrowScore(float browFurrowScore) {
        this.browFurrowScore = browFurrowScore;
    }

    void setBrowRaiseScore(float browRaiseScore) {
        this.browRaiseScore = browRaiseScore;
    }

    void setEngagementScore(float engagementScore) {
        this.engagementScore = engagementScore;
    }

    void setLipCornerDepressorScore(float lipCornerDepressorScore) {
        this.lipCornerDepressorScore = lipCornerDepressorScore;
    }

    void setSmileScore(float smileScore) {
        this.smileScore = smileScore;
    }

    void setValenceScore(float valenceScore) {
        this.valenceScore = valenceScore;
    }

    void setFacePoints(PointF[] facePoints) {
        this.facePoints = facePoints;
    }
}
