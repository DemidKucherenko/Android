package com.example.dictionary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class UIActivity extends Activity implements OnClickListener {
    Context con ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        con = this;
    }
    String word = null;
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			word = ((EditText)findViewById(R.id.editText1)).getText().toString();
			word = word.trim().toLowerCase();
			if (!word.matches("^[a-z -]+$")) {
				Toast.makeText(this, "������� ���������� �����", Toast.LENGTH_SHORT).show();
				break;
			}
			new getT().execute();

		}
	}

    public class getT extends AsyncTask<Void, Void, Void>
    {
        String result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {

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
            return null;
        }

        @Override
        protected void onPostExecute(Void result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            if (result != null) {
                result = result.split("<br")[0];
                ((TextView)findViewById(R.id.translation)).setText(result);
            }
            else {
                ((TextView)findViewById(R.id.translation)).setText("Error! Can't find translation");
            }
            new getI().execute();


        }
    }
    public class getI extends AsyncTask<Void, Void, Void>
    {
        String result = null;
        Bitmap[] bitmaps = null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            String[] urls = new String[10];
            int count_urls = 0;
            ArrayList<Object> recieved = MyActivity.getImageList(word);
            for (Object i : recieved) {
                urls[count_urls++] = ((MyActivity.GoogleImg) i).Url;
            }
             bitmaps = new Bitmap[10];
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            ImageAdapter adapter = new ImageAdapter(con, bitmaps);
            ((ListView) findViewById(R.id.images)).setAdapter(adapter);



        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Bitmap[] bitmaps;
        private Context context ;

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
