//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.util.Log;

import com.affectiva.android.affdex.newsdk.Frame.ByteArrayFrame;
import com.affectiva.android.affdex.newsdk.Frame.ROTATE;

@SuppressLint({"DefaultLocale"})
class AffdexFaceJNIWrapper {
    private static final String sTAG = "AffdeFaceJNIWrapper";
    private AffdexFaceJNI mJni;
    private float mTimeStampSec = 0.0F;
    private ROTATE mTargetRotation;
    private int mWidth;
    private int mHeight;
    private ByteBuffer mFrameBuffer;
    private boolean isInitialized = false;
    private ClassifierConfig classifierConfig;

    AffdexFaceJNIWrapper(AffdexFaceJNI jni) {
        if(jni == null) {
            throw new NullPointerException("jni must not be null");
        } else {
            this.classifierConfig = new ClassifierConfig(false);
            this.mJni = jni;
        }
    }

    long initWithParams(AffdexFaceParams params) {
        this.mTimeStampSec = -1.0F;
        this.mFrameBuffer = null;
        long result = this.mJni.initWithParams(params);
        this.activateEnabledClassifiers();
        if(result == 0L) {
            this.isInitialized = true;
        }

        return result;
    }

    ClassifierConfig getClassifierConfig() {
        return this.classifierConfig;
    }

    void setClassifierConfig(ClassifierConfig newConfig) {
        if(this.isInitialized) {
            if(!this.classifierConfig.getDetectBrowFurrow() && newConfig.getDetectBrowFurrow()) {
                this.mJni.activate(newConfig.getBrowFurrowId());
            }

            if(this.classifierConfig.getDetectBrowFurrow() && !newConfig.getDetectBrowFurrow()) {
                this.mJni.deactivate(newConfig.getBrowFurrowId());
            }

            if(!this.classifierConfig.getDetectBrowRaise() && newConfig.getDetectBrowRaise()) {
                this.mJni.activate(newConfig.getBrowRaiseId());
            }

            if(this.classifierConfig.getDetectBrowRaise() && !newConfig.getDetectBrowRaise()) {
                this.mJni.deactivate(newConfig.getBrowRaiseId());
            }

            if(!this.classifierConfig.getDetectEngagement() && newConfig.getDetectEngagement()) {
                this.mJni.activate(newConfig.getEngagementId());
            }

            if(this.classifierConfig.getDetectEngagement() && !newConfig.getDetectEngagement()) {
                this.mJni.deactivate(newConfig.getEngagementId());
            }

            if(!this.classifierConfig.getDetectLipCornerDepressor() && newConfig.getDetectLipCornerDepressor()) {
                this.mJni.activate(newConfig.getLipCornerDepressorId());
            }

            if(this.classifierConfig.getDetectLipCornerDepressor() && !newConfig.getDetectLipCornerDepressor()) {
                this.mJni.deactivate(newConfig.getLipCornerDepressorId());
            }

            if(!this.classifierConfig.getDetectSmile() && newConfig.getDetectSmile()) {
                this.mJni.activate(newConfig.getSmileId());
            }

            if(this.classifierConfig.getDetectSmile() && !newConfig.getDetectSmile()) {
                this.mJni.deactivate(newConfig.getSmileId());
            }

            if(!this.classifierConfig.getDetectValence() && newConfig.getDetectValence()) {
                this.mJni.activate(newConfig.getValenceId());
            }

            if(this.classifierConfig.getDetectValence() && !newConfig.getDetectValence()) {
                this.mJni.deactivate(newConfig.getValenceId());
            }
        }

        this.classifierConfig = newConfig;
    }

    private void activateEnabledClassifiers() {
        if(this.classifierConfig.getDetectBrowFurrow()) {
            this.mJni.activate(this.classifierConfig.getBrowFurrowId());
        }

        if(this.classifierConfig.getDetectBrowRaise()) {
            this.mJni.activate(this.classifierConfig.getBrowRaiseId());
        }

        if(this.classifierConfig.getDetectEngagement()) {
            this.mJni.activate(this.classifierConfig.getEngagementId());
        }

        if(this.classifierConfig.getDetectLipCornerDepressor()) {
            this.mJni.activate(this.classifierConfig.getLipCornerDepressorId());
        }

        if(this.classifierConfig.getDetectSmile()) {
            this.mJni.activate(this.classifierConfig.getSmileId());
        }

        if(this.classifierConfig.getDetectValence()) {
            this.mJni.activate(this.classifierConfig.getValenceId());
        }

    }

    boolean pushFrame(ByteArrayFrame frame, float timeStampSec) {
        boolean success = false;
        if(timeStampSec < 0.0F) {
            throw new IllegalArgumentException("time stamp must be >= 0");
        } else if(!Float.isNaN(this.mTimeStampSec) && timeStampSec <= this.mTimeStampSec) {
            String msg = "Time stamps for successive frames not increasing. Previous timestamp=" + Float.toString(this.mTimeStampSec) + ", this time stamp=" + Float.toString(timeStampSec);
            Log.w("AffdeFaceJNIWrapper", msg);
            throw new IllegalArgumentException(msg);
        } else {
            this.mTimeStampSec = timeStampSec;
            this.mTargetRotation = frame.getTargetRotation();
            this.mWidth = frame.getWidth();
            this.mHeight = frame.getHeight();
            if(this.mFrameBuffer != null && this.mFrameBuffer.capacity() == frame.getByteArray().length) {
                this.mFrameBuffer.rewind();
            } else {
                this.mFrameBuffer = ByteBuffer.allocateDirect(frame.getByteArray().length);
                this.mJni.resetFrameBuffer(this.mFrameBuffer, this.mWidth, this.mHeight);
            }

            this.mFrameBuffer.put(frame.getByteArray());
            if(!this.mFrameBuffer.isDirect()) {
                Log.w("AffdeFaceJNIWrapper", "is not direct");
            }

            success = this.mJni.pushFrame(this.mTargetRotation.toDouble(), this.mTimeStampSec);
            return success;
        }
    }

    boolean processImage(ByteArrayFrame image) {
        boolean success = false;
        this.mTargetRotation = image.getTargetRotation();
        this.mWidth = image.getWidth();
        this.mHeight = image.getHeight();
        success = this.mJni.processImage(image.getByteArray(), this.mWidth, this.mHeight, this.mTargetRotation.toDouble());
        return success;
    }

    List<Face> getFaces(boolean rotatePoints) {
        Face face = new Face();
        float[] value = new float[]{0.0F};
        if(this.classifierConfig.getDetectBrowFurrow() && this.mJni.getData(this.classifierConfig.getBrowFurrowId(), value)) {
            face.setBrowFurrowScore(value[0]);
        }

        if(this.classifierConfig.getDetectBrowRaise() && this.mJni.getData(this.classifierConfig.getBrowRaiseId(), value)) {
            face.setBrowRaiseScore(value[0]);
        }

        if(this.classifierConfig.getDetectEngagement() && this.mJni.getData(this.classifierConfig.getEngagementId(), value)) {
            face.setEngagementScore(value[0]);
        }

        if(this.classifierConfig.getDetectLipCornerDepressor() && this.mJni.getData(this.classifierConfig.getLipCornerDepressorId(), value)) {
            face.setLipCornerDepressorScore(value[0]);
        }

        if(this.classifierConfig.getDetectSmile() && this.mJni.getData(this.classifierConfig.getSmileId(), value)) {
            face.setSmileScore(value[0]);
        }

        if(this.classifierConfig.getDetectValence() && this.mJni.getData(this.classifierConfig.getValenceId(), value)) {
            face.setValenceScore(value[0]);
        }

        face.setFacePoints(this.getPoints(new int[0], rotatePoints));
        ArrayList result = new ArrayList();
        if(face.getFacePoints() != null) {
            result.add(face);
        }

        return result;
    }

    boolean getData(String feature, AffdexFaceData affdata) {
        float[] value = new float[]{-999.0F};
        affdata.is_valid = this.mJni.getData(feature, value);
        affdata.value = value[0];
        return affdata.is_valid;
    }

    PointF[] getPoints(int[] point_ids) {
        return this.getPoints(point_ids, false);
    }

    PointF[] getPoints(int[] point_ids, boolean postRotation) {
        PointF[] points = null;
        float[] coordinates = this.mJni.getPoints(point_ids);
        if(coordinates != null) {
            points = new PointF[coordinates.length / 2];
            int i = 0;

            for(int j = 0; i < points.length; j += 2) {
                points[i] = new PointF(coordinates[j], coordinates[j + 1]);
                ++i;
            }

            if(!postRotation) {
                Frame.revertPointRotation(points, this.mWidth, this.mHeight, this.mTargetRotation);
            }
        }

        return points;
    }

    boolean cleanup() {
        this.mFrameBuffer = null;
        this.isInitialized = false;
        return this.mJni.cleanup();
    }
}
