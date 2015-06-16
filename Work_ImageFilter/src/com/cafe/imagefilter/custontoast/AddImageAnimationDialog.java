package com.cafe.imagefilter.custontoast;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class AddImageAnimationDialog extends Dialog{

	private int style;
	public AddImageAnimationDialog(Context context, int theme,int styleanmi) {
		super(context, theme);
		style = styleanmi;
	}
	private Window window = null;


	public void showDialog(int layoutResID, int x, int y,int gravity){
		setContentView(layoutResID);

		windowDeploy(x, y,gravity);

		//设置触摸对话框意外的地方取消对话框
		setCanceledOnTouchOutside(true);
		show();
	} 

	//设置窗口显示
	public void windowDeploy(int x, int y,int gravity){
		window = getWindow(); //得到对话框
		window.setWindowAnimations(style); //设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		//根据x，y坐标设置窗口需要显示的位置
		wl.x = x; //x小于0左移，大于0右移
		wl.y = y; //y小于0上移，大于0下移 
		//        wl.alpha = 0.6f; //设置透明度
		wl.gravity = gravity;
		//        wl.gravity = Gravity.BOTTOM; //设置重力
		window.setAttributes(wl);
	}

}
