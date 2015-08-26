package com.cafe.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 用于自定义的数据适配器的viewholder
 * @author dou
 *
 */
public class ViewHolder {

	public static <T extends View> T get(View view ,int id){
		SparseArray< View> viewHolder = (SparseArray<View>) view.getTag();
		
		if(viewHolder == null){
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if(childView == null){
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
