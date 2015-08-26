//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.util.List;

import android.content.Context;
import android.os.Debug;


public abstract class Detector {
    static final String SDK_VERSION = "1.0";
    private final Context cntx;
    private boolean isRunning;

    Detector(Context context) {
        if(context == null) {
            throw new NullPointerException("null context argument provided");
        } else {
            this.cntx = context.getApplicationContext();
        }
    }

    public void start() {
        this.isRunning = true;
    }

    public void stop() {
        if(!this.isRunning) {
            throw new IllegalStateException("must call start() before stop()");
        } else {
            Debug.stopMethodTracing();
            this.isRunning = false;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
    
    public interface ImageListener {
        void onImageResults(List<Face> var1, Frame var2, float var3);
    }
}
