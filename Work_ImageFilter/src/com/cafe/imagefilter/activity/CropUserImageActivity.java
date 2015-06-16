package com.cafe.imagefilter.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.cafe.imagefilter.BaseActivity;
import com.cafe.imagefilter.BaseApplication;
import com.cafe.imagefilter.R;
import com.cafe.imagefilter.camera.ClipImageLayout;

/**
 * 
 * @author cafe_wang
 * 图片裁剪
 */
public class CropUserImageActivity extends BaseActivity{
	private ClipImageLayout mClipImageLayout;
	private Intent mIntent;
	private int Swidth;
	private Uri uri;

	private Button corp_ok;
	private Bitmap mBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.crop_user_ac);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		mContext = this;
		Swidth = BaseApplication.getInstence().scrn.ScrnW_px;
		mIntent = getIntent();
		Bundle bunde = mIntent.getExtras();
		uri = (Uri) bunde .getParcelable("mUri");

		corp_ok = (Button) findViewById(R.id.corp_ok);
		try {
			mBitmap = getThumbnail();
			mClipImageLayout.setImageBitmap(mBitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		corp_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Bitmap bitmap = mClipImageLayout.clip();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] datas = baos.toByteArray();
					String picturePath = Environment.getExternalStorageDirectory()+ "/faceImage.jpg";
					FileOutputStream outStream = null;
					try {
						outStream = new FileOutputStream(picturePath);
						outStream.write(datas);
						outStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
					}
					setResult(RESULT_OK, mIntent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Bitmap getThumbnail() throws FileNotFoundException, IOException{
		InputStream input = this.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither=true;//optional
		onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

		double ratio = (originalSize > Swidth) ? (originalSize/Swidth) : 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither=true;//optional
		bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
		input = getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}
	private int getPowerOfTwoForSampleRatio(double ratio){
		int k = Integer.highestOneBit((int)Math.floor(ratio));
		if(k==0) return 1;
		else return k;
	}
}
