package com.cafe.imagefilter.activity;

import java.io.File;
import java.io.FileNotFoundException;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cafe.imagefilter.BaseActivity;
import com.cafe.imagefilter.R;
import com.cafe.imagefilter.GPUImage.filter.GPUImageFilterTools;
import com.cafe.imagefilter.GPUImage.filter.GPUImageFilterTools.FilterList;

/**
 * 图片操作页面--获取图片进行滤镜操作
 */
public class PubImageSettingACtivity extends BaseActivity implements OnClickListener{

	private Bitmap tagerte_bitmap = null;

	private View filter_back,filter_next;
	private GPUImageView mGPUImageView;
	private LinearLayout filter_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String picturePath = Environment.getExternalStorageDirectory()+ "/faceImage.jpg";
		File tempFile = new File(picturePath);
		try {
			tagerte_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(tagerte_bitmap!=null){
			setContentView(R.layout.activity_filterimage);
			initView();
			initFilter();
			mGPUImageView.setImage(tagerte_bitmap);
		}
	}
	private void initView() {
		filter_back = findViewById(R.id.filter_back);
		filter_next = findViewById(R.id.filter_next);
		mGPUImageView = (GPUImageView) findViewById(R.id.filter_image);
		filter_view  =(LinearLayout) findViewById(R.id.filter_view);
		filter_back.setOnClickListener(this);
	}
	private void initFilter(){
		final FilterList mFilterList = GPUImageFilterTools.initFilterList(mContext);
		for (int i = 0; i < mFilterList.filters.size(); i++) {
			TextView tv = new TextView(mContext);
			tv.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
			tv.setGravity(Gravity.CENTER);
			tv.setText(mFilterList.names.get(i));
			final int count = i;
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switchFilterTo(mFilterList.filters.get(count));
					mGPUImageView.requestRender();
				}
			});
			filter_view.addView(tv);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filter_back:
			finish();
			break;

		default:
			break;
		}
	}
	private GPUImageFilter mFilter;
	private void switchFilterTo(final GPUImageFilter filter) {
		if (mFilter == null
				|| (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
			mFilter = filter;
			mGPUImageView.setFilter(mFilter);
		}
	}
}
