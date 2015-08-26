//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.util.LinkedList;
import java.util.Queue;

class FrameRate {
    private static final float DEFAULT_PROCESSING_RATE = 5.0F;
    private static final int FRAME_WINDOW_SIZE = 100;
    private Queue<FrameRate.FrameRateInfo> frameInfoQueue = new LinkedList();
    private float targetFrameRate = 5.0F;
    private int numProcessed = 0;
    private float previousFrameTimeStamp = -1.0F;

    FrameRate() {
    }

    void setRate(float frameRate) {
        if(!Float.isNaN(frameRate) && frameRate > 0.0F) {
            this.targetFrameRate = frameRate;
        } else {
            throw new IllegalArgumentException("frame rate must be a positive number");
        }
    }

    boolean frameNeedsProcessing(float time) {
        if(time <= this.previousFrameTimeStamp) {
            String var6 = "frame\'s timestamp (" + Float.toString(time) + ") must be higher than previous frame\'s timestamp (" + Float.toString(this.previousFrameTimeStamp) + ")";
            throw new IllegalArgumentException(var6);
        } else if(!Float.isNaN(time) && time >= 0.0F) {
            this.previousFrameTimeStamp = time;
            if(this.frameInfoQueue.isEmpty()) {
                this.frameInfoQueue.add(new FrameRate.FrameRateInfo(time, true));
                ++this.numProcessed;
                return true;
            } else {
                boolean removedOldestElem = false;
                FrameRate.FrameRateInfo oldestElem;
                if(this.frameInfoQueue.size() == 100) {
                    oldestElem = (FrameRate.FrameRateInfo)this.frameInfoQueue.remove();
                    removedOldestElem = true;
                } else {
                    oldestElem = (FrameRate.FrameRateInfo)this.frameInfoQueue.peek();
                }

                float fps = (float)this.numProcessed / (time - oldestElem.timestamp);
                boolean needToProcessFrame = fps < this.targetFrameRate;
                this.frameInfoQueue.add(new FrameRate.FrameRateInfo(time, needToProcessFrame));
                if(needToProcessFrame) {
                    ++this.numProcessed;
                }

                if(removedOldestElem && oldestElem.processed) {
                    --this.numProcessed;
                }

                return needToProcessFrame;
            }
        } else {
            throw new IllegalArgumentException("time stamp must be >= 0");
        }
    }

    private static class FrameRateInfo {
        private float timestamp;
        private boolean processed;

        private FrameRateInfo(float time, boolean willBeProcessed) {
            this.timestamp = time;
            this.processed = willBeProcessed;
        }
    }
}
