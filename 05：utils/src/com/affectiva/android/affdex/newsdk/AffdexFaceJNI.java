//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.nio.ByteBuffer;

import android.annotation.SuppressLint;

@SuppressLint({"DefaultLocale"})
class AffdexFaceJNI {
    private static UnsatisfiedLinkError sUnsatisfiedLinkErr;
    private boolean mHasNativeHandle;

    private native long nativeInstantiate();

    private native long nativeCleanup();

    private native boolean nativeInitialize(AffdexFaceParams var1);

    private native boolean nativeActivate(String var1);

    private native boolean nativeDeactivate(String var1);

    private native boolean nativePushFrame(byte[] var1, int var2, int var3, double var4, float var6);

    private native boolean nativePushFrame(double var1, float var3);

    private native boolean nativeResetFrameBuffer(ByteBuffer var1, int var2, int var3);

    private native boolean nativeProcessImage(byte[] var1, int var2, int var3, double var4);

    private native boolean nativeGetData(String var1, float[] var2);

    private native float[] nativeGetPoints(int[] var1);

    long initWithParams(AffdexFaceParams params) {
        if(params == null) {
            throw new NullPointerException("params must be non-null");
        } else {
            if(this.mHasNativeHandle) {
                this.nativeCleanup();
            }

            this.mHasNativeHandle = this.nativeInstantiate() != 0L;
            boolean isInitialized = this.nativeInitialize(params);
            return isInitialized?0L:1L;
        }
    }

    boolean activate(String output_name) {
        return this.nativeActivate(output_name);
    }

    boolean deactivate(String output_name) {
        return this.nativeDeactivate(output_name);
    }

    boolean pushFrame(byte[] frameData, int width, int height, double targetRotationAngle, float timeStampSec) {
        return this.nativePushFrame(frameData, width, height, targetRotationAngle, timeStampSec);
    }

    boolean pushFrame(double targetRotationAngle, float timeStampSec) {
        return this.nativePushFrame(targetRotationAngle, timeStampSec);
    }

    boolean resetFrameBuffer(ByteBuffer buffer, int width, int height) {
        return this.nativeResetFrameBuffer(buffer, width, height);
    }

    boolean processImage(byte[] imageData, int width, int height, double targetRotationAngle) {
        return this.nativeProcessImage(imageData, width, height, targetRotationAngle);
    }

    boolean getData(String feature, float[] values) {
        return this.nativeGetData(feature, values);
    }

    float[] getPoints(int[] pointIds) {
        return this.nativeGetPoints(pointIds);
    }

    boolean cleanup() {
        this.mHasNativeHandle = this.nativeCleanup() != 0L;
        return !this.mHasNativeHandle;
    }

    AffdexFaceJNI() throws UnsatisfiedLinkError {
        if(sUnsatisfiedLinkErr != null) {
            throw sUnsatisfiedLinkErr;
        }
    }

    static {
        try {
            System.loadLibrary("affdexface_jni");
            sUnsatisfiedLinkErr = null;
        } catch (UnsatisfiedLinkError var1) {
            sUnsatisfiedLinkErr = var1;
        }

    }
}
