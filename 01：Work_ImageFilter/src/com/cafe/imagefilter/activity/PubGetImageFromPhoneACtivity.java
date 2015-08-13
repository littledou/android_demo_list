package com.cafe.imagefilter.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cafe.imagefilter.BaseActivity;
import com.cafe.imagefilter.R;
import com.cafe.imagefilter.camera.CaptureSensorsObserver;
import com.cafe.imagefilter.enity.ScrnSize;
import com.cafe.imagefilter.util.BitmapUtils;
import com.cafe.imagefilter.util.DLogUtils;
import com.cafe.imagefilter.util.DisplayUtil;

@SuppressLint("NewApi")
public class PubGetImageFromPhoneACtivity extends BaseActivity implements OnClickListener,CaptureSensorsObserver.RefocuseListener{

	private ImageView take_pic;
	private View gridtoggle,trunback,lightmode,take_gallery,camera_back,square_toggle;

	private ScrnSize screInfo;

	private RelativeLayout framelayoutPreview;
	private CameraPreview preview;
	private Camera mCamera;
	private PictureCallback pictureCallBack;
	private Camera.AutoFocusCallback focusCallback;
	private CaptureSensorsObserver observer;
	private View focuseView;

	private int currentCameraId;
	private int frontCameraId;
	private boolean _isCapturing;

	CaptureOrientationEventListener _orientationEventListener;
	private int _rotation;
	public static final int kPhotoMaxSaveSideLen = 1600;

	private static final int REQUEST_PICK_IMAGE = 1;
	private static final int REQUEST_CROP = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		observer = new CaptureSensorsObserver(this);
		_orientationEventListener = new CaptureOrientationEventListener(this);
		screInfo = DisplayUtil.getScreenWidth(this);
		setContentView(R.layout.activity_getimage);
		initView();
		initListener();
		setupDevice();
	}

	private void initView() {
		gridtoggle = findViewById(R.id.gridtoggle);
		trunback = findViewById(R.id.trunback);
		lightmode = findViewById(R.id.lightmode);
		take_gallery = findViewById(R.id.take_gallery);
		camera_back = findViewById(R.id.camera_back);
		take_pic = (ImageView) findViewById(R.id.take_pic);
		framelayoutPreview = (RelativeLayout) findViewById(R.id.cameraPreview);
		focuseView = findViewById(R.id.viewFocuse);
		square_toggle = findViewById(R.id.square_toggle);

		gridtoggle.setOnClickListener(this);
		trunback.setOnClickListener(this);
		lightmode.setOnClickListener(this);
		take_gallery.setOnClickListener(this);
		camera_back.setOnClickListener(this);
		take_pic.setOnClickListener(this);
		observer.setRefocuseListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_pic://拍照
			bnCaptureClicked();
			break;
		case R.id.camera_back:
			finish();
			break;
		case R.id.take_gallery:
			getImageFromSD();
			break;
		case R.id.trunback://切换前后置摄像头
			switchCamera();
			break;
		case R.id.lightmode://闪光灯模式
			setLigntMode();
			break;
		case R.id.gridtoggle://网格显示与否
			if(square_toggle.getVisibility()==View.GONE){
				square_toggle.setVisibility(View.VISIBLE);
			}else {
				square_toggle.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
	private void getImageFromSD(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
	}
	@Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
    				Intent mIntent = new Intent(mContext,CropUserImageActivity.class);
    				Bundle bundle = new Bundle();
    				bundle.putParcelable("mUri",data.getData());
    				mIntent.putExtras(bundle );
    				startActivityForResult(mIntent, REQUEST_CROP);
                } else {
                	//取照片返回预览界面
                }
                break;
            case REQUEST_CROP://裁剪返回
            	if (resultCode == RESULT_OK) {
            		startActivity(new Intent(mContext, PubImageSettingACtivity.class));
            	}else {
            		getImageFromSD();
				}
            	
            	break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
	private void setLigntMode() {
		Parameters Param = mCamera.getParameters();
		String mode= Param.getFlashMode();
		if(mode.equals(Parameters.FLASH_MODE_OFF)){
			Param.setFlashMode(Parameters.FLASH_MODE_ON);
			mCamera.setParameters(Param);
			lightmode.setBackgroundResource(R.drawable.custom_camera_lightopen);
			return;
		}
		if(mode.equals(Parameters.FLASH_MODE_ON)){
			try {
				Param.setFlashMode(Parameters.FLASH_MODE_AUTO);
				mCamera.setParameters(Param);
				lightmode.setBackgroundResource(R.drawable.custom_camera_ightauto);
			} catch (Exception e) {
				Param.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(Param);
				lightmode.setBackgroundResource(R.drawable.custom_camera_lightoff);
			}
			return;
		}
		if(mode.equals(Parameters.FLASH_MODE_AUTO)){
			Param.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(Param);
			lightmode.setBackgroundResource(R.drawable.custom_camera_lightoff);
			return;
		}
	}

	private void bnCaptureClicked() {
		if (_isCapturing) {
			return;
		}
		_isCapturing = true;
		focuseView.setVisibility(View.INVISIBLE);
		/*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
		try {
			mCamera.takePicture(mShutterCallback, null, pictureCallBack);
		} catch (RuntimeException e) {
			e.printStackTrace();
			_isCapturing = false;
		}
	}
	ShutterCallback mShutterCallback = new ShutterCallback() {
		//快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
		public void onShutter() {
		}
	};
	private void initListener() {
		pictureCallBack = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				_isCapturing = false;
				Bitmap bitmap = null;
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length, options);
					options.inJustDecodeBounds = false;
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					//此处就把图片压缩了
					options.inSampleSize = Math.max(options.outWidth
							/ kPhotoMaxSaveSideLen, options.outHeight
							/ kPhotoMaxSaveSideLen);
					bitmap = BitmapUtils.decodeByteArrayUnthrow(data, options);
					if (null == bitmap) {
						options.inSampleSize = Math.max(2, options.inSampleSize * 2);
						bitmap = BitmapUtils.decodeByteArrayUnthrow(data, options);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if (null == bitmap) {
					Toast.makeText(mContext, "内存不足，保存照片失败！", Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					Bitmap addBitmap = BitmapUtils.rotateAndScale(bitmap, _rotation, kPhotoMaxSaveSideLen, true);
					Bitmap finalBitmap = cropPhotoImage(addBitmap);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] datas = baos.toByteArray();
					String picturePath = Environment.getExternalStorageDirectory()+ "/faceImage.jpg";
					FileOutputStream outStream = null;
					try {
						outStream = new FileOutputStream(picturePath);
						outStream.write(datas);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						outStream.close();
					}
					startActivity(new Intent(mContext, PubImageSettingACtivity.class));
				} catch (Exception e) {
					DLogUtils.syso("异常显示");
				}
			}
		};
		focusCallback = new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean successed, Camera camera) {
				focuseView.setVisibility(View.INVISIBLE);
			}
		};
	}
	@Override
	protected void onDestroy() {
		if (null != observer) {
			observer.setRefocuseListener(null);
			observer = null;
		}
		_orientationEventListener = null;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event
		observer.stop();
		_orientationEventListener.disable();
	}

	@Override
	protected void onResume() {
		super.onResume();
		openCamera();
	}
	private void switchCamera() {//切换前后置摄像头
		if (currentCameraId == 0) {
			currentCameraId = frontCameraId;
		} else {
			currentCameraId = 0;
		}
		releaseCamera();
		openCamera();
	}
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}

		if (null != preview) {
			framelayoutPreview.removeView(preview);
			preview = null;
		}
	}
	private void setupDevice() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			int cameraCount = Camera.getNumberOfCameras();

			if (cameraCount < 1) {
				Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else if (cameraCount == 1) {
				trunback.setVisibility(View.INVISIBLE);
			} else {
				trunback.setVisibility(View.VISIBLE);
			}
			currentCameraId = 0;
			frontCameraId = findFrontFacingCamera();
			if (-1 == frontCameraId) {//未找到摄像头么
				gridtoggle.setVisibility(View.INVISIBLE);
			}
		}
	}
	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}
	private void openCamera() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			try {
				mCamera = Camera.open(currentCameraId);
			} catch (Exception e) {
				Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			setCameraDisplayOrientation(this, 0, mCamera);
		} else {
			try {
				mCamera = Camera.open();
			} catch (Exception e) {
				Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}
		lightmode.setVisibility(View.GONE);
		if(currentCameraId==0){//初始化时后置闪光auto
			lightmode.setVisibility(View.VISIBLE);
			Parameters Param = mCamera.getParameters();
			Param.setFlashMode(Parameters.ANTIBANDING_OFF);
			lightmode.setBackgroundResource(R.drawable.custom_camera_lightoff);
			mCamera.setParameters(Param);
		}
		preview = new CameraPreview(this, mCamera);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		framelayoutPreview.addView(preview, params1);
		observer.start();
		_orientationEventListener.enable();
	}

	private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		//LogEx.i("result: " + result);
		camera.setDisplayOrientation(result);
	}
	/**
	 * A basic Camera preview class
	 */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public CameraPreview(Context context, Camera camera) {
			super(context);
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
	//相机旋转监听的类，最后保存图片时用到
	private class CaptureOrientationEventListener extends OrientationEventListener {
		public CaptureOrientationEventListener(Context context) {
			super(context);
		}


		@Override
		public void onOrientationChanged(int orientation) {
			if (null == mCamera)
				return;
			if (orientation == ORIENTATION_UNKNOWN)
				return;

			orientation = (orientation + 45) / 90 * 90;
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				_rotation = (90 + orientation) % 360;
				return;
			}

			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(currentCameraId, info);

			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				_rotation = (info.orientation - orientation + 360) % 360;
			} else { // back-facing camera
				_rotation = (info.orientation + orientation) % 360;
			}
		}
	}
	@Override
	public void needFocuse() {
		//LogEx.i("_isCapturing: " + _isCapturing);
		if (null == mCamera || _isCapturing) {
			return;
		}

		//LogEx.i("autoFocus");
		mCamera.cancelAutoFocus();
		try {
			mCamera.autoFocus(focusCallback);
		} catch (Exception e) {
			return;
		}

		if (View.INVISIBLE == focuseView.getVisibility()) {
			focuseView.setVisibility(View.VISIBLE);
			focuseView.getParent().requestTransparentRegion(preview);
		}
	}
	//根据拍照的图片来剪裁
	private Bitmap cropPhotoImage(Bitmap bmp) {
		int dw = bmp.getWidth();
		int dh = bmp.getHeight();
		int width = screInfo.ScrnW_px;
		int point_w = 0;
		float scale = 1.0f;
		if(dw>dh){
			scale = width * 1.0f / dh;
			point_w = dh;
		}else {
			scale = width * 1.0f / dw;
			point_w = dw;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		try {
			Bitmap b2 = Bitmap.createBitmap(bmp, 0, 0, point_w, point_w, matrix, true);
			if (null != b2 && bmp != b2) {
				bmp.recycle();
				bmp = b2;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		try {
			Bitmap b4 = Bitmap.createScaledBitmap(bmp, width, width, false);
			if (null != b4 && bmp != b4) {
				bmp.recycle();
				bmp = b4;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bmp;
	}
}
