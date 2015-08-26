//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.util.Log;
import java.nio.ByteBuffer;

public abstract class Frame {
    private static final int MAX_IMAGE_SIZE = 640;
    protected static final String TAG = "AffdexFrame";
    protected Frame.COLOR_FORMAT colorFormat;
    private Frame.ROTATE targetRotation;
    protected Bitmap originalBitmap;

    public Frame() {
    }

    public static Bitmap rotateImage(Bitmap src, float angleDegrees) {
        if(src == null) {
            throw new NullPointerException("source Bitmap must not be null");
        } else if(angleDegrees == 0.0F) {
            return src;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(angleDegrees);
            Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            return bmp;
        }
    }

    public Bitmap getOriginalBitmapFrame() {
        return this.originalBitmap;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public int getPixelCount() {
        return this.getWidth() * this.getHeight();
    }

    public Frame.COLOR_FORMAT getColorFormat() {
        return this.colorFormat;
    }

    public Frame.ROTATE getTargetRotation() {
        return this.targetRotation;
    }

    public void setTargetRotation(Frame.ROTATE targetRotation) {
        if(targetRotation == null) {
            throw new NullPointerException("target rotation must not be null");
        } else {
            this.targetRotation = targetRotation;
        }
    }

    public void revertPointRotation(PointF[] points) {
        revertPointRotation(points, this.getWidth(), this.getHeight(), this.targetRotation);
    }

    public static void revertPointRotation(PointF[] points, int width, int height, Frame.ROTATE targetRot) {
        if(points == null) {
            throw new NullPointerException("points array must not be null");
        } else {
            --width;
            --height;
            int i;
            PointF p;
            float tmp;
            switch(targetRot.ordinal()) {
            case 1:
                for(i = 0; i < points.length; ++i) {
                    p = points[i];
                    tmp = p.x;
                    p.x = p.y;
                    p.y = (float)height - tmp;
                    points[i] = p;
                }

                return;
            case 2:
                for(i = 0; i < points.length; ++i) {
                    p = points[i];
                    p.x = (float)width - p.x;
                    p.y = (float)height - p.y;
                    points[i] = p;
                }

                return;
            case 3:
                for(i = 0; i < points.length; ++i) {
                    p = points[i];
                    tmp = p.x;
                    p.x = (float)width - p.y;
                    p.y = tmp;
                    points[i] = p;
                }
            }

        }
    }

    public static class ByteArrayFrame extends Frame {
        private byte[] byteArray;
        private int width;
        private int height;

        public ByteArrayFrame(byte[] array, int frameWidth, int frameHeight, Frame.COLOR_FORMAT frameColorFormat) {
            if(array == null) {
                throw new NullPointerException("byte array must not be null");
            } else if(frameColorFormat == null) {
                throw new NullPointerException("frame color format must not be null");
            } else {
                this.byteArray = array;
                this.width = frameWidth;
                this.height = frameHeight;
                this.colorFormat = frameColorFormat;
                int nofPixels = this.getPixelCount();
                String msg;
                if(frameColorFormat == Frame.COLOR_FORMAT.RGBA && array.length < nofPixels * 4) {
                    msg = String.format("Length of data must be == width*height*4 (%d*%d)", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height)});
                    throw new IllegalArgumentException(msg);
                } else if(frameColorFormat == Frame.COLOR_FORMAT.YUV_NV21 && array.length < nofPixels * 3 / 2) {
                    msg = String.format("Length of yuv data must be == w*h*3/2 (%d*%d)", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height)});
                    throw new IllegalArgumentException(msg);
                } else {
                    this.setTargetRotation(Frame.ROTATE.NO_ROTATION);
                }
            }
        }

        public byte[] getByteArray() {
            return this.byteArray;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        private void log() {
            Log.v("AffdexFrame", String.format("byteArray %d", new Object[]{Integer.valueOf(this.byteArray.length)}));
            Log.v("AffdexFrame", String.format("width %d", new Object[]{Integer.valueOf(this.width)}));
            Log.v("AffdexFrame", String.format("height %d", new Object[]{Integer.valueOf(this.height)}));
            Log.v("AffdexFrame", String.format("is c %b", new Object[]{Boolean.valueOf(this.colorFormat == Frame.COLOR_FORMAT.RGBA)}));
            Log.v("AffdexFrame", String.format("nofPixels %d", new Object[]{Integer.valueOf(this.getPixelCount())}));
        }
    }

    public static class BitmapFrame extends Frame {
        private Bitmap bitmap;

        public BitmapFrame(Bitmap bitmap, Frame.COLOR_FORMAT colorFormat) {
            this.setBitmap(bitmap, colorFormat);
            this.setTargetRotation(Frame.ROTATE.NO_ROTATION);
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public void setBitmap(Bitmap bitmap, Frame.COLOR_FORMAT colorFormat) {
            if(bitmap == null) {
                throw new NullPointerException("bitmap must not be null");
            } else if(colorFormat == null) {
                throw new NullPointerException("color format must not be null");
            } else {
                this.originalBitmap = bitmap;
                this.bitmap = scaleBitmapIfNecessary(bitmap);
                this.colorFormat = colorFormat;
            }
        }

        public int getWidth() {
            return this.bitmap.getWidth();
        }

        public int getHeight() {
            return this.bitmap.getHeight();
        }

        private static Bitmap scaleBitmapIfNecessary(Bitmap input) {
            int height = input.getHeight();
            int width = input.getWidth();
            if(height <= 640 && width <= 640) {
                return input;
            } else {
                if(height > width) {
                    width = 640 * width / height;
                    height = 640;
                } else {
                    height = 640 * height / width;
                    width = 640;
                }

                Log.v("AffdexFrame", "Scaling down bitmap from w=" + input.getWidth() + " h=" + input.getHeight() + " to w=" + width + " h=" + height);
                return Bitmap.createScaledBitmap(input, width, height, true);
            }
        }

        public Frame.ByteArrayFrame toByteArrayFrame() throws IllegalArgumentException {
            int byteCount = this.bitmap.getByteCount();
            ByteBuffer byteBuffer = ByteBuffer.allocate(byteCount);
            this.bitmap.copyPixelsToBuffer(byteBuffer);
            byte[] frameData = byteBuffer.array();
            if(this.colorFormat == Frame.COLOR_FORMAT.RGBA && this.bitmap.getConfig() != Config.ARGB_8888) {
                String byteArrayFrame1 = String.format("Unsupported Bitmap.Config for BGR frame. Expecting Bitmap.Config.ARGB_8888.", new Object[0]);
                throw new IllegalArgumentException(byteArrayFrame1);
            } else {
                Frame.ByteArrayFrame byteArrayFrame = new Frame.ByteArrayFrame(frameData, this.getWidth(), this.getHeight(), this.colorFormat);
                byteArrayFrame.setTargetRotation(this.getTargetRotation());
                byteArrayFrame.originalBitmap = this.originalBitmap;
                return byteArrayFrame;
            }
        }
    }

    public static enum ROTATE {
        BY_90_CW(-90.0D),
        BY_180(180.0D),
        BY_90_CCW(90.0D),
        NO_ROTATION(0.0D);

        private double mAngle;

        private ROTATE(double rotation_angle) {
            this.mAngle = rotation_angle;
        }

        public double toDouble() {
            return this.mAngle;
        }
    }

    public static enum COLOR_FORMAT {
        RGBA,
        YUV_NV21,
        UNKNOWN_TYPE;

        private COLOR_FORMAT() {
        }
    }
}
