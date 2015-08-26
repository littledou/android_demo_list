package com.cafe.utils;

import java.io.File;

public class FileUtils {


	/** 
	 * 递归删除文件和文件夹 
	 *  
	 * @param file 
	 *            要删除的根目录 
	 */  
	public static void DeleteFile(File file) {  
		if (file.exists() == false) {  
			return;  
		} else {  
			if (file.isFile()) {  
				file.delete();  
				return;  
			}  
			if (file.isDirectory()) {  
				File[] childFile = file.listFiles();  
				if (childFile == null || childFile.length == 0) {  
					file.delete();  
					return;  
				}  
				for (File f : childFile) {  
					DeleteFile(f);  
				}  
				file.delete();  
			}  
		}  
	}  
	/**
	 * 创建文件夹
	 */
	public static void file(String filepath) {
		File destDir = new File(filepath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}
}
