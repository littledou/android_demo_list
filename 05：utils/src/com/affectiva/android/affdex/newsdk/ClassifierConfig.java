//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

class ClassifierConfig {
    private static final String SMILE_ID = "smilemo";
    private static final String ENGAGEMENT_ID = "expressivenessmo";
    private static final String VALENCE_ID = "valencemo";
    private static final String BROW_FURROW_ID = "au04mo";
    private static final String BROW_RAISE_ID = "au02mo";
    private static final String LIP_CORNER_ID = "au15mo";
    private static final String STATIC_SMILE_ID = "smilemo_static";
    private static final String STATIC_ENGAGEMENT_ID = "expressivenessmo_static";
    private static final String STATIC_VALENCE_ID = "valencemo_static";
    private static final String STATIC_BROW_FURROW_ID = "au04mo_static";
    private static final String STATIC_BROW_RAISE_ID = "au02mo_static";
    private static final String STATIC_LIP_CORNER_ID = "au15mo_static";
    private boolean isStatic;
    private boolean detectBrowRaise;
    private boolean detectBrowFurrow;
    private boolean detectEngagement;
    private boolean detectLipCornerDepressor;
    private boolean detectSmile;
    private boolean detectValence;

    ClassifierConfig(boolean isStatic) {
        this.isStatic = isStatic;
    }

    boolean isStatic() {
        return this.isStatic;
    }

    public boolean getDetectEngagement() {
        return this.detectEngagement;
    }

    void setDetectEngagement(boolean detectEngagement) {
        this.detectEngagement = detectEngagement;
    }

    public boolean getDetectLipCornerDepressor() {
        return this.detectLipCornerDepressor;
    }

    void setDetectLipCornerDepressor(boolean detectLipCornerDepressor) {
        this.detectLipCornerDepressor = detectLipCornerDepressor;
    }

    public boolean getDetectSmile() {
        return this.detectSmile;
    }

    void setDetectSmile(boolean detectSmile) {
        this.detectSmile = detectSmile;
    }

    public boolean getDetectValence() {
        return this.detectValence;
    }

    void setDetectValence(boolean detectValence) {
        this.detectValence = detectValence;
    }

    public boolean getDetectBrowRaise() {
        return this.detectBrowRaise;
    }

    void setDetectBrowRaise(boolean detectBrowRaise) {
        this.detectBrowRaise = detectBrowRaise;
    }

    public boolean getDetectBrowFurrow() {
        return this.detectBrowFurrow;
    }

    void setDetectBrowFurrow(boolean detectBrowFurrow) {
        this.detectBrowFurrow = detectBrowFurrow;
    }

    String getBrowFurrowId() {
        return this.isStatic?"au04mo_static":"au04mo";
    }

    String getBrowRaiseId() {
        return this.isStatic?"au02mo_static":"au02mo";
    }

    String getEngagementId() {
        return this.isStatic?"expressivenessmo_static":"expressivenessmo";
    }

    String getLipCornerDepressorId() {
        return this.isStatic?"au15mo_static":"au15mo";
    }

    String getSmileId() {
        return this.isStatic?"smilemo_static":"smilemo";
    }

    String getValenceId() {
        return this.isStatic?"valencemo_static":"valencemo";
    }
}
