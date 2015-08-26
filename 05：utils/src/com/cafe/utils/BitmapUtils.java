package com.cafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;

public class BitmapUtils {

	/**
	 * 资源文件id获取bitmap
	 */
	public static Bitmap getBitmapFromResouce(Context context,int id){
		return BitmapFactory.decodeResource(context.getResources(), id);
	}

	/**
	 * 图片反转
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
		Matrix matrix = new Matrix();
		matrix.postRotate((float)rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}

	/**
	 * data to bitmap
	 */
	public static Bitmap dataToBitmap(byte[] data){
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
	/** 
	 *  处理图片  
	 */ 
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){   
		// 获得图片的宽高   
		int width = bm.getWidth();   
		int height = bm.getHeight();   
		// 计算缩放比例   
		float scaleWidth = ((float) newWidth) / width;   
		float scaleHeight = ((float) newHeight) / height;   
		// 取得想要缩放的matrix参数   
		Matrix matrix = new Matrix();   
		matrix.postScale(scaleWidth, scaleHeight);   
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
		return newbm;   
	}  
	/**
	 * @param srcBitmap
	 * @return
	 */
	public static Bitmap createReflectedBitmap(Context context,int id){    	
		final int padding = 150;
		Bitmap srcBitmap = getBitmapFromResouce(context, id);
		if (null == srcBitmap){
			return null;
		}

		// The gap between the reflection bitmap and original bitmap. 
		final int REFLECTION_GAP = 4;

		int srcWidth  = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		int reflectionWidth  = srcBitmap.getWidth();
		int reflectionHeight = srcBitmap.getHeight() / 2;

		if (0 == srcWidth || srcHeight == 0){
			return null;
		}

		// The matrix
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		try{
			// The reflection bitmap, width is same with original's, height is half of original's.
			Bitmap reflectionBitmap = Bitmap.createBitmap(
					srcBitmap,
					0, 
					srcHeight / 2,
					srcWidth, 
					srcHeight / 2,
					matrix,
					false);

			if (null == reflectionBitmap)
			{
				return null;
			}

			// Create the bitmap which contains original and reflection bitmap.
			Bitmap bitmapWithReflection = Bitmap.createBitmap(
					reflectionWidth,
					srcHeight + reflectionHeight + REFLECTION_GAP, 
					Config.ARGB_8888);

			if (null == bitmapWithReflection){
				return null;
			}

			// Prepare the canvas to draw stuff.
			Canvas canvas = new Canvas(bitmapWithReflection);

			// Draw the original bitmap.
			canvas.drawBitmap(srcBitmap, 0, padding, null);

			// Draw the reflection bitmap.
			canvas.drawBitmap(reflectionBitmap, 0, padding+srcHeight + REFLECTION_GAP, null);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			LinearGradient shader = new LinearGradient(
					0, 
					srcHeight, 
					0, 
					bitmapWithReflection.getHeight() + REFLECTION_GAP, 
					0x70FFFFFF, 
					0x00FFFFFF,
					TileMode.MIRROR);
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));

			// Draw the linear shader.
			canvas.drawRect(
					0, 
					padding+srcHeight, 
					srcWidth, 
					padding+bitmapWithReflection.getHeight() + REFLECTION_GAP, 
					paint);

			reflectionBitmap.recycle();
			return bitmapWithReflection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	public static InputStream getRequest(String path)  { 
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");  
			conn.setConnectTimeout(5000);  
			if (conn.getResponseCode() == 200) {  
				return conn.getInputStream();  
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;  
	}  
	public static byte[] getBytesFromUrl(String url) throws Exception {  
		return readInputStream(getRequest(url));  
	} 
	/** 
	 * 从流中读取二进制数据 
	 *  
	 * @param inStream 
	 *            输入流 
	 * @return 二进制数据 
	 * @throws Exception 
	 *             异常 
	 */  
	public static byte[] readInputStream(InputStream inStream) throws Exception {  
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
		byte[] buffer = new byte[4096];  
		int len = 0;  
		while ((len = inStream.read(buffer)) != -1) {  
			outSteam.write(buffer, 0, len);  
		}  
		outSteam.close();  
		inStream.close();  
		return outSteam.toByteArray();  
	}  

	public static Bitmap getBitmapFromURL(String url){
		byte[] bytes = null;
		try {
			bytes = getBytesFromUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return byteToBitmap(bytes);  
	}
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	public static Bitmap createBitmapThumbnail(Bitmap bitMap,int size) {  
		int width = bitMap.getWidth();  
		int height = bitMap.getHeight();  
		// 设置想要的大小  
		int newWidth = size;  
		int newHeight = size;  
		// 计算缩放比例  
		float scaleWidth = ((float) newWidth) / width;  
		float scaleHeight = ((float) newHeight) / height;  
		// 取得想要缩放的matrix参数  
		Matrix matrix = new Matrix();  
		matrix.postScale(scaleWidth, scaleHeight);  
		// 得到新的图片  
		Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,  
				matrix, true);  
		return newBitMap;  
	}  
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
