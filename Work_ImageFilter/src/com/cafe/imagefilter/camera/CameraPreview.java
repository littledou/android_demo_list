package com.cafe.imagefilter.camera;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/*
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	public static final int kPhotoMaxSaveSideLen = 1600;
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context,attrs);
	}

	@SuppressWarnings("deprecation")
	public void setCamera(Camera camera){
		mCamera = camera;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw
		// the preview.
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your
		// activity.
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();// 停止预览
			mCamera.release(); // 释放摄像头资源
			mCamera = null;
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the
		// requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}

		return optimalSize;
	}

	private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
		final double ASPECT_TOLERANCE = 0.05;

		if (sizes == null)
			return null;

		Size optimalSize = null;
		int optimalSideLen = 0;
		double optimalDiffRatio = Double.MAX_VALUE;

		for (Size size : sizes) {

			int sideLen = Math.max(size.width, size.height);
			//LogEx.i("size.width: " + size.width + ", size.height: " + size.height);
			boolean select = false;
			if (sideLen < kPhotoMaxSaveSideLen) {
				if (0 == optimalSideLen || sideLen > optimalSideLen) {
					select = true;
				}
			} else {
				if (kPhotoMaxSaveSideLen > optimalSideLen) {
					select = true;
				} else {
					double diffRatio = Math.abs((double) size.width / size.height - targetRatio);
					if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
						select = true;
					} else if (diffRatio < optimalDiffRatio + ASPECT_TOLERANCE && sideLen < optimalSideLen) {
						select = true;
					}
				}
			}

			if (select) {
				optimalSize = size;
				optimalSideLen = sideLen;
				optimalDiffRatio = Math.abs((double) size.width / size.height - targetRatio);
			}
		}

		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events
		// here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		try {
			Camera.Parameters parameters = mCamera.getParameters();

			List<Size> sizes = parameters.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, w, h);
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			double targetRatio = (double) w / h;
			sizes = parameters.getSupportedPictureSizes();
			optimalSize = getOptimalPictureSize(sizes, targetRatio);
			parameters.setPictureSize(optimalSize.width, optimalSize.height);
			parameters.setRotation(0);
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
