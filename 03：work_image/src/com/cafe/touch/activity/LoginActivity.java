package com.cafe.touch.activity;

import java.io.File;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cafe.touch.BaseActivity;
import com.cafe.touch.BaseApplication;
import com.cafe.touch.ImageConfig;
import com.cafe.touch.R;
import com.cafe.touch.down.DownLoaderTask;
import com.cafe.touch.down.DownLoaderTask.DownLoaderListener;
import com.cafe.touch.down.ZipExtractorTask;
import com.cafe.touch.down.ZipExtractorTask.ZipExtratorListener;
import com.cafe.utils.FileUtils;
import com.cafe.utils.StringUtils;

public class LoginActivity extends BaseActivity implements OnClickListener{

	private final String TAG = "LoginActivity";

	private EditText editText;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		editText = (EditText) findViewById(R.id.edittext);
		button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		String name = editText.getText().toString().trim();
		if(StringUtils.isNotEmpty(name)){
			//login 
			JsonObjectRequest mRequest = new JsonObjectRequest(ImageConfig.login_url,
					null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {//return current filename,next download url
					checkFile(response);
				}
			},  new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

				}
			});
			mRequest.setTag(TAG);
			BaseApplication.getInstence().getmQueue().add(mRequest);
		}
	}
	//1：login success ,check file ,diff then  delete local and download new file
	private void checkFile(JSONObject response){
		//TODO 
		if(!mopo()) return;

		String filename = "";
		String url = "";
		try {
			if(response.has("filename"))filename = response.getString("filename");
			if(response.has("url"))filename = response.getString("url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotEmpty(filename)){
			String pathname = ImageConfig.ROOT_DIR +filename+".zip";
			File file = new File(pathname);
			if (file.exists()) {
				return;
			}
			FileUtils.DeleteFile(new File(ImageConfig.ROOT_DIR));//delete .../zip/
			FileUtils.file(ImageConfig.ROOT_DIR +filename);//new .../zip/filename
			if(StringUtils.isNotEmpty(url)){// start download file
				doDownLoadWork(url,ImageConfig.ROOT_DIR,filename);
			}
		}
	}
	/**
	 * 检测储存卡是否安装
	 */
	private boolean mopo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			new AlertDialog.Builder(mContext).setTitle("提示:")
			.setMessage("非常抱歉！\n您不能正常使用本软件，可能是以下原因所导致。\n⒈未检测到你手机里的存储卡设备。\n⒉软件经过其他人所修改导致安装文件时出错。\n\n按确定退出本软件！ ").setIcon(R.drawable.ic_launcher)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			}).show();
		}
		return false;
	}

	private void doZipExtractorWork(String filename){
		//解压到ImageConfig.ROOT_DIR此文件夹
		ZipExtractorTask task = new ZipExtractorTask(ImageConfig.ROOT_DIR+filename+".zip", ImageConfig.FILE_DIR_NAME, this,new ZipExtratorListener() {
			
			@Override
			public void loadSuccess() {
				//TODO zip extractor success
			}
			
			@Override
			public void loadError() {
				
			}
		}, true);
		task.execute();
	}

	private void doDownLoadWork(String url,String path,final String filename){
		DownLoaderTask task = new DownLoaderTask(url, path,new DownLoaderListener() {
			
			@Override
			public void loadSuccess() {
				doZipExtractorWork(filename);
			}
			
			@Override
			public void loadError() {
				
			}
		},this);
		task.execute();
	}
}
