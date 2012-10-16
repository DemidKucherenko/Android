/*Daniel Galper 2539 Get Links by keywords*/
package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.PublicKey;
import java.util.ArrayList;

public class MyActivity extends Activity {
    private TextView text;
    /**
     * Called when the activity is first created.
     */

    String strSearch;
    private ArrayList<Object> listImages;
    AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.text);
        strSearch = "lemon";
        new getImagesTask().execute();
    }

    public class getImagesTask extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(MyActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                        "v=1.0&q="+strSearch+"&rsz=8"); //&key=ABQIAAAADxhJjHRvoeM2WF3nxP5rCBRcGWwHZ9XQzXD3SWg04vbBlJ3EWxR0b0NVPhZ4xmhQVm3uUBvvRF-VAA&userip=192.168.0.172");

                URLConnection connection = url.openConnection();
                //connection.addRequestProperty("Referer", "http://internet.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");

                listImages = getImageList(resultArray);
                StringBuilder builder = new StringBuilder();
                for (Object listImage : listImages) {
                    builder.append(((GoogleImg)listImage).getUrl()).append("\n");
                }
                text.setText(builder);
//                Toast.makeText(MyActivity.this,builder.toString(),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    public class GoogleImg
    {
        String Url;

        public String getUrl()
        {
            return Url;
        }
        public void setUrl(String url)
        {
            this.Url = url;
        }

    }
    public ArrayList<Object> getImageList(JSONArray resultArray)
    {
        ArrayList<Object> listImages = new ArrayList<Object>();
        GoogleImg googleImg;

        try
        {
            for(int i = 0; i<resultArray.length(); i++)
            {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                googleImg = new GoogleImg();
                googleImg.setUrl(obj.getString("tbUrl"));
                listImages.add(googleImg);
            }
            return listImages;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}




