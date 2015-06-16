package com.cafe.imagefilter.util;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CamParaUtils {
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();
	private static CamParaUtils myCamPara = null;
	private CamParaUtils(){

	}
	public static CamParaUtils getInstance(){
		if(myCamPara == null){
			myCamPara = new CamParaUtils();
			return myCamPara;
		}
		else{
			return myCamPara;
		}
	}

	public  Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth){
		Collections.sort(list, sizeComparator);

		int i = 0;
		
		for(Size s:list){
			if((s.width >= minWidth) && equalRate(s, th)){
				break;
			}
			i++;
		}
		if(i == list.size()){
			i = 0;//如果没找到，就选最小的size
		}
		return list.get(i);
	}
	public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
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
	
	public static final int kPhotoMaxSaveSideLen = 1600;
	public static Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
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
	
	
	
	public Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth){
		Collections.sort(list, sizeComparator);

		int i = 0;
		for(Size s:list){
			if((s.width >= minWidth) && equalRate(s, th)){
				break;
			}
			i++;
		}
		if(i == list.size()){
			i = 0;//如果没找到，就选最小的size
		}
		return list.get(i);
	}

	public boolean equalRate(Size s, float rate){
		float r = (float)(s.width)/(float)(s.height);
		if(Math.abs(r - rate) <= 0.03)
		{
			return true;
		}
		else{
			return false;
		}
	}

	//比较器
	public  class CameraSizeComparator implements Comparator<Camera.Size>{
		public int compare(Size lhs, Size rhs) {
			if(lhs.width == rhs.width){
				return 0;
			}
			else if(lhs.width > rhs.width){
				return 1;
			}
			else{
				return -1;
			}
		}
	}
}