package com.example.dictionary;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		String word = getIntent().getStringExtra("word");
		getTranslation(word);
		getImages(word);
		//fakeGetTranslation(word);
		//fakeGetImages(word);
	}
	
	public void getTranslation(String word) {
		String result = null;
		try {
			result = MainActivity.translate(word);		
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result != null) {
			((TextView)findViewById(R.id.translation)).setText(result);
		}
		else {
			((TextView)findViewById(R.id.translation)).setText("Error! Can't find translation");
		}
	}
	
	public void fakeGetTranslation(String word) {
		if (word.equals("football")) {
			((TextView)findViewById(R.id.translation)).setText("футбол");
		}
		else {
			((TextView)findViewById(R.id.translation)).setText("Error! Can't find translation");
		}
	}
	
	public void getImages(String word) {
		String[] urls = new String[10];
		int count_urls = 0;
		ArrayList<Object> recieved = MyActivity.getImageList(word);
		for (Object i : recieved) {
			urls[count_urls++] = ((MyActivity.GoogleImg) i).Url;
		}
		Bitmap[] bitmaps = new Bitmap[10];
		for (int i = 0; i < count_urls; i++) {
			try {
				HttpURLConnection connection;
				connection = (HttpURLConnection) new URL(urls[i]).openConnection();
				connection.setDoInput(true);
	            connection.connect();
	            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
	            bitmaps[i] = bitmap;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		ImageAdapter adapter = new ImageAdapter(this, bitmaps);
		((ListView) findViewById(R.id.images)).setAdapter(adapter);
	}
	
	public void fakeGetImages(String word) {
		if (!word.equals("football")) {
			return;
		}
		String[] urls = new String[10];
		int count_urls = 10;
		urls[0] = "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSp9vZOdlGtlZjZhsoSkndixhbIgF6p_TKpaHDpiVyOOz-QD2pt";
		urls[1] = "http://cs402730.userapi.com/v402730655/4d3b/ljrZLlKP9G0.jpg";
		urls[2] = "http://cs407823.userapi.com/v407823713/3d0b/WpwZ3S_PV0s.jpg";
		Bitmap[] bitmaps = new Bitmap[10];
		for (int i = 0; i < count_urls; i++) {
			try {
				HttpURLConnection connection;
				connection = (HttpURLConnection) new URL(urls[i]).openConnection();
				connection.setDoInput(true);
	            connection.connect();
	            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
	            bitmaps[i] = bitmap;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		ImageAdapter adapter = new ImageAdapter(this, bitmaps);
		((ListView) findViewById(R.id.images)).setAdapter(adapter);
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Bitmap[] bitmaps;
	    private Context context;

	    public ImageAdapter(Context context, Bitmap[] bitmaps) {
	        this.bitmaps = bitmaps;
	        this.context = context;
	    }

	    @Override
	    public int getCount() {
	        return bitmaps.length;
	    }

	    @Override
	    public Object getItem(int i) {
	        return bitmaps[i];
	    }

	    @Override
	    public long getItemId(int i) {
	        return 0;
	    }

	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup viewGroup) {
	        ImageView v;
	        if (convertView == null) {
	            v = new ImageView(context);
	            v.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        } else {
	            v = (ImageView) convertView;
	        }
	        v.setImageBitmap(bitmaps[position]);
	        return v;
	    }
	}
}
