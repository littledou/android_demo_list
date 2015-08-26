//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;
import android.view.SurfaceView;

import com.affectiva.android.affdex.newsdk.CameraHelper.CameraFacade;
import com.affectiva.android.affdex.newsdk.CameraHelper.CameraManager;
import com.affectiva.android.affdex.newsdk.CameraHelper.SurfaceViewFacadeFactory;
import com.affectiva.android.affdex.newsdk.Frame.ByteArrayFrame;
import com.affectiva.android.affdex.newsdk.Frame.ROTATE;

public class CameraDetector extends Detector {
    CameraDetector.CameraHelperFactory cameraHelperFactory;
    FrameRate frameRate;
    Display defaultDisplay;
    Resources resources;
    private CameraHelper cameraHelper;
    private long timeOfFirstFrame;
    private boolean initialized = false;
    private CameraDetector.CameraType cameraType;
    private SurfaceView cameraPreviewView;
    private int deviceDefaultOrientation;

    public CameraDetector(Context context, CameraDetector.CameraType cameraType, SurfaceView cameraPreviewView) {
        super(context);
        this.cameraType = cameraType;
        this.cameraPreviewView = cameraPreviewView;
    }

    public void setMaxProcessRate(float maxFramesPerSecond) {
        if(maxFramesPerSecond < 0.0F) {
            throw new IllegalArgumentException("maxFramesPerSecond must not be less than zero.");
        } else {
            this.frameRate.setRate(maxFramesPerSecond);
        }
    }


    public void start() {
        super.start();
        this.initialized = false;
        this.cameraHelper = this.cameraHelperFactory.create(this, this.cameraPreviewView);
        this.cameraHelper.startDetection(this.cameraType);
    }

    public void stop() {
        super.stop();
        this.cameraHelper.stopDetection();
    }

    void detectInFrame(byte[] bytes, int width, int height) {
        float timestamp = 0.0F;
        ROTATE rotation = this.getRotation();
        ByteArrayFrame imageFrame = new ByteArrayFrame(bytes, width, height, Frame.COLOR_FORMAT.YUV_NV21);
        imageFrame.setTargetRotation(rotation);
        long currentTime = System.nanoTime();
        if(!this.initialized) {
            this.initialized = true;
            this.timeOfFirstFrame = currentTime;
            timestamp = 0.0F;
        } else {
            timestamp = (float)(currentTime - this.timeOfFirstFrame) / 1.0E9F;
        }

        
    }

    private ROTATE getRotation() {
        int rotation = this.defaultDisplay.getRotation();
        ROTATE direction;
        if(2 == this.getDeviceDefaultOrientation(rotation)) {
            switch(rotation) {
            case 0:
            default:
                direction = ROTATE.NO_ROTATION;
                break;
            case 1:
                direction = ROTATE.BY_90_CCW;
                break;
            case 2:
                direction = ROTATE.BY_180;
                break;
            case 3:
                direction = ROTATE.BY_90_CW;
            }
        } else {
            switch(rotation) {
            case 0:
            default:
                direction = ROTATE.BY_90_CW;
                break;
            case 1:
                direction = ROTATE.NO_ROTATION;
                break;
            case 2:
                direction = ROTATE.BY_90_CCW;
                break;
            case 3:
                direction = ROTATE.BY_180;
            }
        }

        return direction;
    }

    private int getDeviceDefaultOrientation(int rotation) {
        if(this.deviceDefaultOrientation == 0) {
            Configuration config = this.resources.getConfiguration();
            if((rotation != 0 && rotation != 2 || config.orientation != 2) && (rotation != 1 && rotation != 3 || config.orientation != 1)) {
                this.deviceDefaultOrientation = 1;
            } else {
                this.deviceDefaultOrientation = 2;
            }
        }

        return this.deviceDefaultOrientation;
    }

    static class CameraHelperFactory {
        private Context context;
        private CameraManager cameraManager;
        private CameraFacade cameraFacade;
        private SurfaceViewFacadeFactory surfaceViewFacadeFactory;
        private Display defaultDisplay;

        CameraHelperFactory(Context context, CameraManager cameraManager, CameraFacade cameraFacade, SurfaceViewFacadeFactory surfaceViewFacadeFactory, Display defaultDisplay) {
            this.context = context;
            this.cameraManager = cameraManager;
            this.cameraFacade = cameraFacade;
            this.surfaceViewFacadeFactory = surfaceViewFacadeFactory;
            this.defaultDisplay = defaultDisplay;
        }

        CameraHelper create(CameraDetector detector, SurfaceView cameraPreviewView) {
            return new CameraHelper(this.context, detector, this.cameraManager, this.cameraFacade, this.surfaceViewFacadeFactory, cameraPreviewView, this.defaultDisplay);
        }
    }

    public static enum CameraType {
        CAMERA_FRONT,
        CAMERA_BACK;

        private CameraType() {
        }
    }
}
