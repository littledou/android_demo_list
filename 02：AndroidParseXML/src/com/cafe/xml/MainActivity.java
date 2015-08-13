package com.cafe.xml;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cafe.xml.model.Book;
import com.cafe.xml.parser.BookParser;
import com.cafe.xml.parser.DomBookParser;
import com.cafe.xml.parser.SaxBookParser;

public class MainActivity extends Activity{

	private static final String TAG = "XML";  

	private BookParser parser;  
	private List<Book> books;  

	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		try {  
			InputStream is = getAssets().open("books.xml");  
			parser = new SaxBookParser();  //创建SaxBookParser实例  
//			parser = new DomBookParser();  //创建DomBookParser实例  
//			parser = new PullBookParser();  //创建PullBookParser实例  
			books = parser.parse(is);  //解析输入流  
			for (Book book : books) {  
				Log.i(TAG, book.toString());  
			}  
		} catch (Exception e) {  
			Log.e(TAG, e.getMessage());  
		}  
		
		try {  
			String xml = parser.serialize(books);  //序列化  
			FileOutputStream fos = openFileOutput("books.xml", Context.MODE_PRIVATE);  
			fos.write(xml.getBytes("UTF-8"));  
		} catch (Exception e) {  
			Log.e(TAG, e.getMessage());  
		}  
	}  
}
